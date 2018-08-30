#include <jni.h>
#include <string>
#include <sys/mman.h>
#include <fcntl.h>

#include <string>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <thread>
#include <chrono>
#include <android/log.h>
#include <unistd.h>
#include <sys/types.h>

#define TAG    "native_cpp" // 这个是自定义的LOG的标识
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__) // 定义LOGD类型

//TODO: 文件首个字节会是0 ?

extern "C"
JNIEXPORT jstring JNICALL
Java_com_lhy_comm_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


int mapfd = -1;
char *map_addr = (char *) MAP_FAILED;
char *cur_map_addr = (char *) MAP_FAILED;
char * g_log_file_path = NULL;
int s_map_size = 4*1024;
int s_page_size = 4*1024;

void expandFile(int fd, off64_t end_offset);
off64_t findEndString(int mapfd);
void remap_file(int mapfd);
int write_mmap(std::string pString, int startPos);

void remap_file(int mapfd) {
    off64_t end_offset = lseek(mapfd,0,SEEK_END);

    //bug: 入参需要严格把控，比如位置计算可能出现无效数值。因此，加入以下判断
    int startPage = ((end_offset/s_map_size) -1);
    if(startPage<0){
        return;
    }

    map_addr = (char *)mmap(NULL, s_map_size , PROT_READ|PROT_WRITE, MAP_SHARED , mapfd , startPage*4096);
    cur_map_addr = map_addr;
}

/**
 * 空间不足，写满，扩展日志文件，再递归接续写，直到递归完毕返回，要么扩展失败，要么最后成功写完返回。空间足够，写后返回0
 * @param log_str
 * @return
 */
int write_mmap(std::string log_str, int startPos) {
    int ret = 0;
    int minlen = 0;

    int left_size = s_map_size - (int)(cur_map_addr-map_addr);
    int left_log_size = (int)(log_str.length()-startPos);
    if(left_size -  left_log_size<=0) {
        //剩余日志空间比剩余要写的日志长度小
        minlen = left_size;
        ret = -1;
    }else{
        //剩余日志空间大过要写的日志长度，尽管写
        minlen = log_str.length();
        ret = 0;
    }

    for (int i = 0; i < minlen; i++) {
        cur_map_addr[i] = (log_str.c_str())[i];
    }
    cur_map_addr += minlen;

    if(ret == -1){
        //剩余日志空间不足的情况，要扩展日志文件了
        ret = 0;

        if(g_log_file_path != NULL) {
            munmap(map_addr, s_map_size);

            mapfd = open(g_log_file_path, O_RDWR);
            if (mapfd < 0) {
                LOGE("error open, path=%s\n", g_log_file_path);
                ret = -1;
            }

            expandFile(mapfd, -1);
            remap_file(mapfd);
            close(mapfd);

            if (map_addr == MAP_FAILED) {
                ret = -1;
            }
        } else{
            ret = -1;
        }

        if(ret == 0){
            if(startPos + minlen >= log_str.length()){
                return 0;
            }else{
                return write_mmap(log_str, startPos + minlen);
            }
        }
    }

    return ret;
}

//TODO: 查找算法可替换成更有效率的
off64_t findEndString(int fd) {
    if(fd<0){
        return -1;
    }

    char buffer[1024];
    ssize_t len = -1;
    int line = 0;
    int index = -1;

    off64_t end_offset = lseek(mapfd,0,SEEK_END);
    lseek(mapfd, end_offset-s_map_size, SEEK_SET);
    while ((len = read(fd, buffer, 1024)) > 0)
    {
        for(int i=0; i<len; i++){
            if(buffer[i] == 0){
                index = i;
                break;
            }
        }

        if(index>=0) break;

        line++;
    }

    if(index>=0){
        return end_offset-s_map_size + line*1024 + index;
    }

    return -1;
}

void expandFile(int fd, off64_t end_offset) {
    if(fd<0){
        return;
    }

    if(end_offset == -1){
        end_offset = lseek(fd,0, SEEK_END);
        lseek(fd,end_offset + s_map_size -1, SEEK_SET);
    } else{
        lseek(fd,end_offset + s_map_size -1, SEEK_SET);
    }

    write(fd,"\0",1);
    //bug: write不够，需要fsync，相当于fflush()。因为没有执行写入磁盘，导致后面的代码读文件长度依然为原长度，比如依然为0，继而导致后续逻辑，如文件映射起始位置计算出错。
    //bug2: 为啥怎么sync都不行? 因为前面首次创建文件时，没有重新以读写方式打开呀！！！
    fsync(fd);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_lhy_comm_mars_MyLogUtil_openMyLog(
        JNIEnv* env,
        jobject obj, jstring path) {

    std::string str_path;
    const char *ch;
    ch = env->GetStringUTFChars(path, NULL);
    str_path = ch;
    g_log_file_path = (char *) malloc(str_path.length()+1);
    if(g_log_file_path!=NULL){
        memcpy(g_log_file_path, ch, str_path.length());
        g_log_file_path[str_path.length()] = 0;
    }
    env->ReleaseStringUTFChars(path, ch);

    mapfd=open(str_path.c_str(), O_RDWR);
    if(mapfd<0){
        LOGE("error open, path=%s\n", str_path.c_str());
        mapfd = open(str_path.c_str(), O_CREAT);

        if(mapfd<0){
            LOGE("error create, path=%s\n", str_path.c_str());
            return;
        } else{
            close(mapfd);
        }
    }

    //bug fixed: 无文件先创建,但要重新打开,以读写方式打开,否则后面的读写操作无权限进行
    mapfd=open(str_path.c_str(), O_RDWR);
    if(mapfd<0) {
        LOGE("error open, path=%s\n", str_path.c_str());
        return;
    }

    s_page_size = sysconf(_SC_PAGESIZE);
    s_map_size = s_page_size;

    off64_t end_offset = lseek(mapfd,0,SEEK_END);
    if(end_offset <= s_map_size){
        expandFile(mapfd, end_offset);
    }

    if(end_offset>=10*1024*1024){
        LOGE("error open, path=%s\n", str_path.c_str());
        char s[] = "error open log file, file length exceeded 10MB\n";
        write(mapfd, s, sizeof(s));
        close(mapfd);
        return;
    }

    remap_file(mapfd);
    close(mapfd);

    if(map_addr == MAP_FAILED){
        return;
    }

    int startNull = 0;
    while(cur_map_addr[startNull++] != 0){

    }
    cur_map_addr += startNull;
}



extern "C"
JNIEXPORT void JNICALL
Java_com_lhy_comm_mars_MyLogUtil_writeMyLog(
        JNIEnv* env,
        jobject obj, jint level, jstring tag, jstring msg) {

    if(map_addr == MAP_FAILED){
        return;
    }

    std::string str_tag;
    char *ch_str_tag;
    {
        const char *ch;
        ch = env->GetStringUTFChars(tag, NULL);
        str_tag = ch;
        ch_str_tag = (char *) ch;
        env->ReleaseStringUTFChars(tag, ch);
    }

    std::string str_msg;
    char *ch_str_msg;
    {
        const char *ch;
        ch = env->GetStringUTFChars(msg, NULL);
        str_msg = ch;
        ch_str_msg = (char *) ch;
        env->ReleaseStringUTFChars(msg, ch);
    }

    std::string log_str = str_tag + ", " + str_msg + "\n";
    write_mmap(log_str, 0);
}