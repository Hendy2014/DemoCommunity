package com.lhy.comm;

import org.junit.Test;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Runnable ,Callable, Future, FutureTask
 * Created by luohy on 2018/8/28.
 */
public class CollectionTest {

    @Test
    public void testReference() {
        // 四种引用类型
        // 原理：除了强引用，是直接new操作出来的，直接引用。而其它三个都是被包装类引用的，
        // 使用者拿着包装类去引用真正的对象，是间接引用罢了。
        // 当然，不是简单的包装，包装类使用了本地方法，结合虚拟机GC机制实现了回收机制。

        //1. 强引用（Strong Reference）， 只要强引用存在则GC时则必定不被回收。
        Object obj = new Object();

        //2. 软引用（Soft Reference）
        // 用于描述还有用但非必须的对象，当堆将发生OOM（Out Of Memory）时则会回收软引用所指向的内存空间，
        // 若回收后依然空间不足才会抛出OOM。
        // 一般用于实现内存敏感的高速缓存。

        //用法一：直接使用
        SoftReference sr = new SoftReference(obj);

        // 下次使用时
        obj = (Object)sr.get();
        if (obj == null){
            // 当软引用被回收后才重新获取
            obj = new Object();
        }

        // 清理被收回后剩下来的软引用对象
        SoftReference ref = null;

        //用法二：结合ReferenceQueue
        // 软引用可以和一个引用队列（ReferenceQueue）联合使用，如果软引用所引用的对象被垃圾回收器回收，
        // Java虚拟机就会把这个软引用加入到与之关联的引用队列中。
        ReferenceQueue q = new ReferenceQueue();
        // 获取数据并缓存
        obj = new Object();
        sr = new SoftReference(obj, q);

        // 下次使用时
        obj = (Object)sr.get();
        if (obj == null){
            // 当软引用被回收后才重新获取
            obj = new Object();
        }

        // 清理被收回后剩下来的软引用对象
        ref = null;
        while((ref = (SoftReference<Object>)q.poll()) != null){
            // 清理工作
        }

        //扩展：间接引用，那还不是一样持有强引用，只是包装类的实例比较小是吗？不占内存是吗？那用间接引用去引用小对象，岂不是很亏？
    }

    @Test
    public void testCollection() {
        Collection<String> c = new ArrayList<String>();
        c.add("hello");
        c.add("world");
        c.add("!");

        c.isEmpty();
        c.remove("hello");

        c.addAll(c);
        c.clear();
        Iterator iterator = c.iterator();

        //具体实现类继承contains方法，通常，比较对象，使用equals()方法，因此，最终取决于具体集合类，
        // 如ArrayList.equals()方法来确定是否对象相等。
        c.contains("aa");
        //结合使用哈希值的地方使用，如HashMap
        c.hashCode();

    }

    @Test
    public void testList() {
        //ArrayList底层使用数组实现的
        //默认容量是10，构造时可以指定初始容量，扩展容量时，公式是(ori * 3)/2 + 1
        //但容量是容量，与list.size()元素总量是两回事
        //大数组时，容量决定着list的内存占用大小
        List<String> list = new ArrayList<>(30);

        //List相关的API如下，重点关注clone()，索引操作特点，对象比较相等条件，序列化
//        // AbstractCollection中定义的API
//        void                add(int location, E object)
//        boolean             addAll(int location, Collection<? extends E> collection)
//        E                   get(int location)
//        int                 indexOf(Object object)
//        int                 lastIndexOf(Object object)
//        ListIterator<E>     listIterator(int location)
//        ListIterator<E>     listIterator()
//        E                   remove(int location)
//        E                   set(int location, E object)
//        List<E>             subList(int start, int end)
//        // ArrayList新增的API
//        Object               clone()
//        void                 ensureCapacity(int minimumCapacity)
//        void                 trimToSize()
//        void                 removeRange(int fromIndex, int toIndex)

        //基本操作
        for (int i = 0; i < 100; i++) {
            list.add(new Integer(i).toString());
        }

        //元素访问
        //三种方式，fori + get()进行随机访问，iterator，foreach
        //可想而知，fori + get()是最快的，因为底层是数组，通过索引访问最快，O(1)速度
        //因此，ArrayList进行元素访问，get()方法是最具效率的写法
        for (int i = 0; i < 100; i++) {
            System.out.println(list.get(i));
        }

        //TODO: toArray()存在类型转换的问题？
        vectorToArray1(new ArrayList<Integer>(5));
        vectorToArray2(new ArrayList<Integer>(5));
        vectorToArray3(new ArrayList<Integer>(5));

//        fail-fast 机制是java集合(Collection)中的一种错误机制。
// 当多个线程对同一个集合的内容进行操作时，就可能会产生fail-fast事件。
//        例如：当某一个线程A通过iterator去遍历某集合的过程中，若该集合的内容被其他线程所改变了；
// 那么线程A访问集合时，就会抛出ConcurrentModificationException异常，产生fail-fast事件。
        //简单讲，就是两个线程，拿同一个ArrayList对象，调用它的Iterator()方法，产生了两个ArrayList.Itr对象，
        //拿去操作list，一个删元素，一个读元素，Itr最终也是操作ArrayList对象，是同一个
        //因此，存在线程同步问题，但看Itr操作，get()方法，均没见有同步方面的措施
        //因此，有个疑问，
        // TODO: 虽然有fail-fast机制，但没有同步，难道就不会说checkModification()方法之后，恰好通过，然后线程1停止，线程2去删除 ，线程1回来继续读取而出错吗？

        //ArrayList总结：
        // 底层使用数组实现，通过索引访问元素是最高效的，
        // 容量问题：初始容量，扩展容量公式，最大容量
        // 数组转换存在类型转换问题？
    }

    // toArray(T[] contents)调用方式一
    public static Integer[] vectorToArray1(ArrayList<Integer> v) {
        Integer[] newText = new Integer[v.size()];
        v.toArray(newText);
        return newText;
    }

    // toArray(T[] contents)调用方式二。最常用！
    public static Integer[] vectorToArray2(ArrayList<Integer> v) {
        Integer[] newText = (Integer[]) v.toArray(new Integer[0]);
        return newText;
    }

    // toArray(T[] contents)调用方式三
    public static Integer[] vectorToArray3(ArrayList<Integer> v) {
        Integer[] newText = new Integer[v.size()];
        Integer[] newStrings = (Integer[]) v.toArray(newText);
        return newStrings;
    }

    @Test
    public void testLinkedList() {
        LinkedList<String> link = new LinkedList<String>();
        for (int i = 0; i < 1000000; i++) {
            link.add(new Integer(i).toString());
        }

        List<String> list = Collections.synchronizedList(link);
        System.out.println("list.size :" + list.size());

        long startTime = System.currentTimeMillis();
        for (String str : list) {
            ;
        }
        long endTime = System.currentTimeMillis();
        System.out.println("time1 :" + (endTime - startTime));

        startTime = System.currentTimeMillis();
        while (link.pollFirst() != null) {
            ;
        }
        endTime = System.currentTimeMillis();
        System.out.println("time2 :" + (endTime - startTime));

        //TODO: 理论上是随机访问很慢，但实际结果却是时间0，始终是0？ 哪里有问题？
        startTime = System.currentTimeMillis();
        for (int i = 0; i < link.size(); i++) {
            link.get(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("time3 :" + (endTime - startTime));
    }

    @Test
    public void testStack() {
        Stack<String> stack = new Stack<>();
        stack.push("1");
        stack.push("2");
        System.out.println("push 2 items: \n" + Arrays.toString(stack.toArray()));
        String s2 = stack.pop();
        System.out.println("pop 1 item: \n" + Arrays.toString(stack.toArray()));
        s2 = stack.peek();
        System.out.println("peek 1 item: \n" + Arrays.toString(stack.toArray()));

        //JDK说了：Deque 接口及其实现提供了 LIFO 堆栈操作的更完整和更一致的 set，应该优先使用此 set，而非此类。例如：
        Deque<Integer> stack2 = new ArrayDeque<Integer>();
        for (int i = 0; i < 10; i++) {
            //使用push，则是LIFO
            stack2.push(i);
        }

        System.out.println("push items: \n" + Arrays.toString(stack2.toArray()));
        stack2.poll();
        System.out.println("poll 1 item: \n" + Arrays.toString(stack2.toArray()));
        stack2.pop();
        System.out.println("pop 1 item: \n" + Arrays.toString(stack2.toArray()));
    }

    @Test
    public void testArrayDeque() {
        Deque<Integer> stack2 = new ArrayDeque<Integer>();
        for (int i = 0; i < 10; i++) {
            //使用push，则是LIFO
//            stack2.push(i);
            //使用offer，则是FIFO
            stack2.offer(i);
        }

        System.out.println("push items: \n" + Arrays.toString(stack2.toArray()));
        stack2.poll();
        System.out.println("poll 1 item: \n" + Arrays.toString(stack2.toArray()));
        stack2.pop();
        System.out.println("pop 1 item: \n" + Arrays.toString(stack2.toArray()));
    }

    
    @Test
    public void testStackOnSquareArea(){
        int[] height = {2,1,4,5,1,3,3};
//        int[] height = {2,1,5,4,1,3,3};
//        int[] height = {1,2,3,4,5,6};
//        int[] height = {6,5,4,3,2,1};
//        int[] height = {1,2,3,5,6,4,2,3,5,6,4,1,8,9,12,40,50,30,20,1,90,80,20,50,20,60,20,90,20,80,20,40,20,30,20};
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> stack_count = new Stack<>();

        int maxArea = 0;
        int totalWidth = 0;
        for (int i = 0; i < height.length; i++) {
            //遇小柱，栈，计算右扩性累计矩形，找出最大者记录，相当于此小柱前的所有可能的矩形组合都计算完了
            totalWidth = 0;
            while(!stack.empty() && stack.peek().intValue() >= height[i]){
                //弹栈，计算右扩性最大矩形
                int space = stack.pop().intValue() * (stack_count.peek().intValue() + totalWidth);
                if(space>maxArea){
                    maxArea = space;
                }
                //记录遇小柱时，弹栈时，柱高是从高到低的，因此累加计宽，用于弹出的低柱计算右扩累计宽，
                // 即可计算最大连接矩形，即上面那句+totalWidth的作用
                totalWidth += stack_count.pop();
            }
            
            //否则，是递增柱或者弹栈后剩下的遇见小柱，入栈
            stack.push(height[i]);
            stack_count.push(totalWidth + 1);
        }

        //可能最后几个都是入栈，所以需要出栈处理
        int totalWidth2 = 0;
        while(!stack.empty()){
            int space = stack.pop().intValue() * (stack_count.peek().intValue() + totalWidth2); //计算
            if(space>maxArea){
                maxArea = space;
            }
            totalWidth2 += stack_count.pop();//记录遇小柱时，小柱的右扩累计宽，同时也是弹栈时，用于计算右扩累计宽
        }

        System.out.println("max area=" + maxArea);
    }

    @Test
    public void testHashMap() {
        HashMap<String, String> hashMap = new HashMap<>();

        //1、键值对存取操作
        //允许使用 null 值和 null 键
        hashMap.put("1", "Jack");
        hashMap.put(null, "Jack");
        hashMap.put("2", null);
        hashMap.put(null, null);

        System.out.println("hashMap.containsKey(null)=" + hashMap.containsKey(null));
        System.out.println("hashMap.get(null)" + hashMap.get(null));
        System.out.println("hashMap.get(2)" + hashMap.get("2"));

        //遍历键值对， 单独遍历键、值，使用map.keySet()，map.values();
        Map map = hashMap;
        String integ = null;
        String key;
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            key = (String)entry.getKey();
            integ = (String)entry.getValue();
            System.out.println("[" + key + "," + integ + "]");
        }

        System.out.println("TreeMap----------------");
        Map<String, String> map2 = new TreeMap<>();
        map2.put("21", "Jack 2");
//        map2.put(null, "Jack 3"); //TreeMap不支持null值的key，会报错
        map2.put("23", null);
        iter = map2.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            key = (String)entry.getKey();
            integ = (String)entry.getValue();
            System.out.println("[" + key + "," + integ + "]");
        }

        //WeakHashMap
        System.out.println("WeakHashMap----------------");
        Map<String, String> map3 = new WeakHashMap<>();
        map3.put("31", "Jack 2");
        map3.put(null, "Jack 3");
        map3.put("33", null);
        iter = map3.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            key = (String)entry.getKey();
            integ = (String)entry.getValue();
            System.out.println("[" + key + "," + integ + "]");
        }

    }
}
