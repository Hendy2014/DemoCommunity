//
// Created by luohy on 2018/9/4.
//
//
#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG    "native_cpp" // 这个是自定义的LOG的标识
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__) // 定义LOGD类型

/* This code is illegal */
jclass stringClass = NULL;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_lhy_comm_test_JNITest_MyNewString(
        JNIEnv* env,
        jobject /* this */) {
        /* This code is illegal */
        //static jclass stringClass = NULL;

        jmethodID cid;
        jcharArray elemArr;
        jstring result;
        if (stringClass == NULL) {
            stringClass = (env)->FindClass("java/lang/String");
            if (stringClass == NULL) {
                return NULL; /* exception thrown */
            }
        }
    
        /* It is wrong to use the cached stringClass here,
           because it may be invalid. */
        cid = (env)->GetMethodID(stringClass, "<init>", "([C)V");
        elemArr = (env)->NewCharArray( 10);
        result = (jstring)(env)->NewObject(stringClass, cid, elemArr);
        (env)->DeleteLocalRef( elemArr);

        return result;
}



/* This code is OK */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_lhy_comm_test_JNITest_MyNewGlobalString(
        JNIEnv* env, jclass clazz)
{
    static jclass stringClass = NULL;
    
    if (stringClass == NULL) {
        jclass localRefCls =
                (jclass)(env)->FindClass( "java/lang/String");
        if (localRefCls == NULL) {
            return NULL; /* exception thrown */
        }
        /* Create a global reference */
        stringClass = (jclass)(env)->NewGlobalRef( localRefCls);
        /* The local reference is no longer useful */
        (env)->DeleteLocalRef( localRefCls);
        /* Is the global reference created successfully? */
        if (stringClass == NULL) {
            return NULL; /* out of memory exception thrown */
        }
    }

    jmethodID cid;
    jcharArray elemArr;
    jstring result;
    cid = (env)->GetMethodID(stringClass, "<init>", "([C)V");
    elemArr = (env)->NewCharArray( 10);
    result = (jstring)(env)->NewObject(stringClass, cid, elemArr);
    (env)->DeleteLocalRef( elemArr);

    return result;
}


/* This code is OK */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_lhy_comm_test_JNITest_MyNewWeakGlobalRefString(
        JNIEnv* env, jclass clazz)
{
    static jclass stringClass = NULL;

    if (stringClass == NULL) {
        jclass localRefCls =
                (jclass)(env)->FindClass( "java/lang/String");
        if (localRefCls == NULL) {
            return NULL; /* exception thrown */
        }
        /* Create a global reference */
        stringClass = (jclass)(env)->NewWeakGlobalRef( localRefCls);
        /* The local reference is no longer useful */
        (env)->DeleteLocalRef( localRefCls);
        /* Is the global reference created successfully? */
        if (stringClass == NULL) {
            return NULL; /* out of memory exception thrown */
        }

        //test object compare
        if((env)->IsSameObject( stringClass, localRefCls)){
            LOGE(TAG, "IsSameObject true");
        }
    }

    jmethodID cid;
    jcharArray elemArr;
    jstring result;
    cid = (env)->GetMethodID(stringClass, "<init>", "([C)V");
    elemArr = (env)->NewCharArray( 10);
    result = (jstring)(env)->NewObject(stringClass, cid, elemArr);
    (env)->DeleteLocalRef( elemArr);

    return result;
}