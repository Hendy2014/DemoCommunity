package com.lhy.comm;

import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;

/**
 * Basic
 * Created by luohy on 2018/8/28.
 */
public class JavaProgrameTest {

    @Test
    public void testMain(){
//        testOOPMalloc();
//        testOOPBasic1();
//        testArgument();
//        testInnerClass();
        new BaseGenericType<String>().testWildcard();
    }

    @Test
    public void testModifier() {
        //public default protected private
    }

    //内部类的四种限定修饰符
    public class A{}
    private class B{}
    protected class C{}
    class D{}


    //接口是一种规范协定
    interface BaseOOPInterface{
        //接口中成员变量，都是public static final的，因此，看到编译器提示public static final任意一个都是多余的
        public static final int BASE_MEMBER = 0;

        //内部类
        class InnerClassI{}
        //内部接口
        interface InnerInterface{
            void setBaseMember(int a);
        }

        //接口方法
        int getBaseMember();

//    //类方法，须java8才支持
//    public static int getMaxSN(){
//        return 100;
//    }

        //默认方法，须java8才支持
//    default int getMinSN(){
//        return 1;
//    }
    }

    //接口支持多继承
    public class MultiInterfaceInherit implements
            BaseOOPInterface, BaseOOPInterface.InnerInterface{

        @Override
        public int getBaseMember() {
            return 0;
        }

        @Override
        public void setBaseMember(int a) {

        }
    }

    public void setInterface(BaseOOPInterface.InnerInterface a){
        a.setBaseMember(BaseOOPInterface.BASE_MEMBER);
    }

    public void testInterface(){
        BaseOOPInterface.InnerInterface a = new BaseOOPInterface.InnerInterface(){
            @Override
            public void setBaseMember(int a) {
                out.println(a);
            }
        };

        setInterface(a);
    }

    //程序的三种基本结构
    public void testBasicFlow(){
        //flow
        int a1 = 1;
        int a2 = a1;
        int a3 = a2;
        int a4 = a1 + a2+ a3;
        out.println(a4);

        //if/else
        if(a1 == 1){
            a2 = a2 + a3;
        }else{
            a3 = a2 + a3;
        }

        //loop
        while(a3<100){
            a3++;
        }
    }

    //理解对象的创建、内存分配过程
    //局部变量的存储，在JAVA方法栈上
    //对象构建细节：分配对象内存空间 -> static静态初始化块(类加载时间点级别) -> 普通{}初始块(对象内存创建时间点级别) -> 构造方法(对象内存分配完毕，进入对象构造时间点级别)，示例见下面的testOOPBasic()的Animal示例
    public void testOOPMalloc(){
        //栈内存中上为变量a分配空间，就是一个引用，32/64位
        A a;
        //堆内存中创建A类的实例对象，其实就是对象数据，主要是成员属性，方法依旧在方法区。
        a = new A();
        //栈内存中为变量b分配空间，赋值为a引用的值
        //此时，a,b是不同的变量，具有相同的值，都是引用值，其值能告诉虚拟机指向堆中刚刚new创建的A的实例对象
        A b = a;

        //以上，a, b均为局部变量，均存储在JAVA方法栈上
    }


    //面向对象的三个特征：封装，继承，多态
    public void testOOPBasic1(){
        //封装，包括成员属性和方法，确定类的设计后，便使用四大限定性修饰符达成目的
        Animal a = new Animal(10);
        out.println("Animal a=" + a);
        out.println("Animal a.age=" + a.age);
        a.sleep();
        a.privateMethod();

        //继承
        Cat c = new Cat(10);
        out.println("Cat c=" + c);
        c.sleep();
        c.privateMethod();

        //多态
        Animal c2 = new Cat(10);
        out.println("Animal c2=" + c2);
        c2.sleep();
        //下面这一行，最终执行Animal.privateMethod()，即Cat.privateMethod()被认为方法重载，而非方法覆盖导致多态运行。具体看源码区分下修饰符
        c2.privateMethod();

        // 习以为常的，使用List<>父类声明，使用ArrayList<>子类创建，
        // 最后使用父类引用去操作，调用方法，其实就是多态，最终执行ArrayList<>的add/get方法等
        List<String> list = new ArrayList<String>(32);
        list.add("hello 多态, List<String> list = new ArrayList<String>(32); out.println(list.get(0));");
        out.println(list.get(0));


        // 重难点，易混淆点
        // 多态与继承，代码上，仅区别于声明时，是用父类声明，还是子类本类声明；
        // 运行时区别在于背后的找实际运行方法的曲折，多态实际是父类对象，找实际内容是堆中子类对象的实际方法而执行之。
    }

    public class Animal{
        {
            out.println("Animal 普通初始化块");
        }

        protected int age = 0;

        public Animal(int age){
            out.println("Animal 构造器");
            this.age = age;
        }

        public void sleep(){
            out.println("Animal sleep!");
        }

        //极端点：覆盖前提是方法签名相同，下面两行与Cat子类的privateMethod()方法签名不同，因此，会被JVM认为是重载。
//        private final void privateMethod(){
        private void privateMethod(){
//        public void privateMethod(){
            out.println("Animal privateMethod!");
        }

        @Override
        public String toString(){
            return super.toString() + " Animal, age=" + age;
        }
    }

    public class Cat extends Animal{
        {
            out.println("Cat 普通初始化块");
        }

        public Cat(int age) {
            // 构造方法一定要调用父类构造方法，
            // 未显式用super()调用，则编译器会默认调用父类无参构造器，若父类没有无参构造器，则提示出错。
            // 原则上是先调用，后实现自己的特殊构造内容。
            super(age);
            this.age = age + 1;
            out.println("Cat 构造器");
        }

        @Override
        public void sleep(){
            out.println("Cat sleep!");
        }

        public void privateMethod(){
            super.privateMethod();
            out.println("Cat privateMethod!");
        }

        @Override
        public String toString(){
            return super.toString() + " Cat, age=" + age;
        }
    }

    //UML，类之间的基本关系: 关联(组合，聚合)、泛化、依赖
    //关联：部分与整体的关系。难点：区分组合与聚合，UML中的空心与实心菱形的表示。见李刚书
    //泛化：继承，接口
    //依赖：牵一发动全身。改类，依赖类跟着改。

    //this引用、super引用

    //传参，Java只有值传递，包括引用的值
    public void testArgument(){
        Animal c = new Cat(10);
        out.println("Cat c=" + c);
        passObjectArgument(c);
        out.println("Cat c=" + c);
    }

    public void passObjectArgument(Animal a){
        a.age = 90;
    }


    //静态内部类，非静态内部类，外部类，外围类
    //在外部类/外围类/对象中使用
    public static class OutterClass{
        int mOutterClassMember = 0;

        public class InnerClass{
            public void testInnerClass(){
                out.println("I'm InnerClass");
                out.println("My OutterClass's Member is " + mOutterClassMember);
            }
        }

        static class StaticInnerClass{
            public void testStaticInnerClass(){
                out.println("I'm StaticInnerClass");
            }
        }

        public void testInnerClass(){
            new InnerClass().testInnerClass();
//            new StaticInnerClass().testStaticInnerClass();
        }
    }

    public void testInnerClass(){
        //静态内部类
        // 静态，此指类级别，不依赖对象。
        // 因此，可以直接创建静态内部类的实例对象，不需要先创建其外部类，对，不是外围类。
        OutterClass.StaticInnerClass staticInnerClass = new OutterClass.StaticInnerClass();
        staticInnerClass.testStaticInnerClass();

        //非静态内部类
        // 非静态，即动态，此指对象级别，须依赖对象而存在
        // 因此，只能先创建外围类，对，外围类，而后，才能去依赖外围类去创建非静态内部类实例对象，
        // 且创建后，非静态内部类实例对象自动拥有其外围类的引用，可自由访问其外围类成员
        OutterClass outterClass = new OutterClass();
        outterClass.testInnerClass();

        //在外围类外部代码，创建其非静态内部类对象
        //这句代码，意思其实就是，给我创建个非静态内部类对象，其外围类对象是outterClass
        OutterClass.InnerClass innerClass = outterClass.new InnerClass();
        innerClass.testInnerClass();

        /**
         * 小结：
         * 静态内部类，类级别，可自由创建，不依赖其外部类是否有创建。可在类内部创建，也可在类外部创建。也因此，没有所谓的外围类引用。
         * 非静态内部类，对象级别，只能在其外围类创建后，才能创建，无论是在外围类内部还是外部代码创建，都需要依赖于外围类对象来创建，且创建后，即拥有外围类对象的引用。
         *
         *
         * 都生成class文件，如 JavaProgrameTest$OutterClass$InnerClass.class， JavaProgrameTest$OutterClass$StaticInnerClass.class
         * 可见，本质还是类，就是在编译期限定了访问规则。
         * (实际上，静态内部类，在定义上稍有区别，关键是加个static关键字，但使用上，编译后，本质跟普通类的定义与使用无差)
         *
         * 区别：
         * 类级别，对象级别 （这是一切的根本，本质区别，其它一切不同，均因此而来）
         * 外围类及其引用
         *
         * 使用场景：
         * 静态内部类
         * 仅当该类会被共用，且是使用其外部类时才用，那么，作为外部类的一种附属器般，即静态内部类，是合适的，使得代码整洁，关联，内聚。
         * 非静态内部类
         *
         * 非静态内部类中访问外围类对象
         * 使用OutterClassName.this访问外围类对象
         */
    }

    //匿名内部类
    public void testAnonymousClass(){
        testInterface();

        /**
         * Android编程中的内部类现象
         * 凡Listener接口编程中，都涉及匿名内部类
         * 匿名内部类中访问外围类对象
         * 使用OutterClassName.this访问外围类对象
         */
    }

    public void testGC(){
        //以下两句是等效的,都是建议JVM去回收,但不是马上,而是尽快而已
        System.gc();
        Runtime.getRuntime().gc();

        // 类的finalize方法,类似于C++的析构,但使用上有四点
        // 不要主动调用,它是给JVM在GC该对象时调用的,且不保证一定调用，
        // 它用于GC前的处理动作,该方法出现异常时,程序继续运行,JVM不报告的
        // 可以使用以下接口，建议JVM尽快去对所有对象执行finalize()
        Runtime.getRuntime().runFinalization();
    }

    public void testSystem() throws IOException {
        System.gc();
        System.out.println();
//        System.arraycopy(null, 0, null, 0, 0);
//        System.nanoTime();
//        System.currentTimeMillis();
//        System.getenv("Java");
//        System.loadLibrary("");

        Runtime.getRuntime().gc();
        Runtime.getRuntime().addShutdownHook(new Thread());
        Runtime.getRuntime().exec("ls .");
        long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().runFinalization();
        Runtime.getRuntime().availableProcessors();
    }

    //对象的几个常用机制，clone, 序列化，equals，hashCode，compare
    public void testClone(){

    }

    public void testJava8(){
        //Objects
        //NIO
        //String, StringBuffer, StringBuilder
        //Method, Random, BigDecimal
        //Time: Date,DateFormat, SimpleDateFormat, Calendar ...
        //String.matches()及正则表达式
    }

    //剩下模块
    //集合
    //泛型
    //类加载机制、反射
    //网络
    //多线程
    //IO流

    @Test
    public void testInteger() throws Exception {
        assertEquals(4, 2 + 2);
        Integer integer7 = 7;
        Integer integer899 = 899;
        Integer integer7_2 = 7;
        Integer integer899_2 = 899;

        System.out.println("integer7 == new Integer(7): " + (integer7 == new Integer(7)));
        System.out.println("integer7.equals new Integer(7): " + (integer7.equals(new Integer(7))));

        System.out.println("integer899 == new Integer(899): " + (integer899 == new Integer(899)));
        System.out.println("integer899.equals new Integer(899): " + (integer899.equals(new Integer(899))));

        System.out.println("integer7_2 == new Integer(7): " + (integer7_2 == new Integer(7)));
        System.out.println("integer7_2.equals new Integer(7): " + (integer7_2.equals(new Integer(7))));

        System.out.println("integer899_2 == new Integer(899): " + (integer899_2 == new Integer(899)));
        System.out.println("integer899_2.equals new Integer(899): " + (integer899_2.equals(new Integer(899))));


        System.out.println("integer7 == integer7_2: " + (integer7 == integer7_2));
        System.out.println("integer899 == integer899_2: " + (integer899 == integer899_2));

        System.out.println("integer7.equals integer7_2: " + (integer7.equals(integer7_2)));
        System.out.println("integer899_2.equals integer899: " + (integer899_2.equals(integer899)));

        Integer integerv1 = Integer.valueOf(7);
        Integer integerv2 = Integer.valueOf(7);
        System.out.println("integerv1.equals integerv2: " + (integerv1.equals(integerv2)));

        try {
            Integer.decode("");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }



}

//注意：这是定义在类外部，不是内部类。只是定义在同一个源文件而已
//而且编译器提示，public类只能单独定义在同名源文件下，因此，这样定义，只能是使用默认限定，即包可见
//果然，同包路径下的源文件类，可访问，其它包路径下，无法访问
class ImNotInnerClass{

}

/**
 * 泛型，泛化类型也。
 * 即形式参数上升到最极端层面，泛指某种类型，某些类型，甚至任意类型
 * 目的：更通用的编程，防止类型转换的安全风险(将类型转换提到编译期，并用泛型的一些编译检查规则规避之)
 *
 * 重难点：通配符。全通，类型限定上限，下限，反射
 */
class BaseGenericType<E>{
    public <S> void testType(S s){

    }

    //------------------ ?通配符 ------------------
    //?通配符单独使用时，只能作为类型实参，传递给泛型类的类型形参，如List<?>
    //且这种用法，只能用来get()获取元素，返回值类型的，而add()等其它作为方法参数的均无法使用。
//    public void wildcardOnlyUsedAsReferenceParam_thisIsWrong(? a){}
    public void addAnything(List<?> a){
        out.println(a.get(0));
        //以下均编译不通过
//        List<Integer> strlist = new ArrayList<Integer>();
//        strlist.addAll(a);
//        a.add(new Object());
    }

    public void testWildcard(){
        List<String> strlist = new ArrayList<String>();
        List<?> a1 = strlist;
        List<?> b1 = new ArrayList<String>();
        //这两句不通过
//        List<Object> a = strlist;
//        List<Object> b = new ArrayList<String>();

        strlist.add("hello");
        addAnything(strlist);
        //以下代码与传参给方法是一样的，其实都是父类引用转换，即将List<String>引用赋给父类List<?>引用
        List<?> a = strlist;
        out.println(a.get(0));
//        //以下均编译不通过
//        List<Integer> intList = new ArrayList<Integer>();
//        intList.addAll(a);
//        a.add(new Object());
    }

    //------------------ ? super 和 ? extends 通配符 ------------------
    // 1、上下限通配符作为方法传入参数

    //由于适配的对象是E的父类，而本类使用的是E类，所以，E的父类赋值给E类型对象，显然不行，类型向下转换有问题
    //具体讲就是，c里的对象类型，是E的父类，获取出来，赋值给mEme，是E类型，父类赋给子类，显然不行
    E mEme;
    public void setSub(List<? super E> c){
        //下面这句编译不通过，提示类型不匹配
//        mEme = c.get(0);
        //这句就能过,类型适配得上呀
        c.set(0, mEme);
    }

    //由于适配的对象是E的子类，而本类使用的是E类，所以，E的子类赋值给E类型对象，显然可以，类型向上转换没毛病
    //具体讲就是，c里的对象类型，是E的子类，获取出来，赋值给mEme，是E类型，子类赋给父类，类型向上转换没毛病
    //以下方法正确
    public void addSub(List<? extends E> c){
        mEme = c.get(0);
    }

    //2、上下限通配符作为方法返回参数

    //至于方法返回中指定上下限，好像没用,反正返回的时候，都是E类，都在支持范围，只是调用者注意咯。
    //以下方法均编译通过
    //例子，Class.forName()的应用
    ArrayList<E> mListE;
    public Collection<? extends E> getSub(){
        return new ArrayList<E>();
    }

    public Collection<? super E> getSub2(){
        return mListE;
    }

    public Collection<?> getSub3(){
        return mListE;
    }

    public void testReflect() throws ClassNotFoundException {
        //Class类是JDK封装的一个类,用于表示在虚拟机中加载的类的信息。它有一系列接口让你获取指定类的信息，如方法、成员属性、继承关系等
        //三种获得Class对象的方式，显示指定类、通过类名(有点像工厂方法)、通过对象(实际是Object的final共用getClass方法)

        //这种方式，实际上是会被编译器翻译成？
        Class<String> a = String.class;

        Class<String> b = (Class<String>) Class.forName("String");
        Class<BaseGenericType> c = (Class<BaseGenericType>) this.getClass();

        //void、基本类型、基本类型数组，这些在虚拟机中的类信息，也由Class类实例对象来表示
        Class<Boolean> d = boolean.class;
        Class<int[]> e = int[].class;
        //Void类专门用于虚拟机中代表关键字void的信息的类
        Class<Void> f = void.class;

        //总结：以上，本质上就是.class字节码文件里面的类的信息，封装成Class泛型类，用Class类来实例化对象，代表虚拟机加载的类的信息
        //让你可以访问这些类的信息，并操作之，但是在运行期，通过这个Class的对象信息，告诉虚拟机，间接去创建。
        //这也是为什么会比new创建语句，更慢一些的原因。
        //具体来说：
        // 反射进行创建对象，过程是：在运行期动态创建对象，代码执行过程是创建目标类的Class信息对象，找到其构造方法的信息，告诉虚拟机，按着这个构造方法，这个类信息，去加载了创建类对象。
        // new操作进行创建对象，过程是：编译期翻译成创建对象的字节码命令，运行期直接一两条命令语句即可完成对象的创建。
        /**
         * 【Java反射，Java高级，Java深入细节】
         * 为什么通过反射创建对象比new语句创建对象要慢？答案如上。
         */
    }
}


/**
 * JVM
 * 重点：JVM内存模型，垃圾回收、类加载
 *
 */
//细节：JVM参数，内存相关崩溃及防止StackOverFlow/OOM，正确使用不同内存区域，为了性能又如何使用内存
//GC相关：内存抖动及分析解决，主动GC的两个调用，四种引用模型与GC及缓存应用
//双亲委派模型的类加载机制，classloader判断类相同的条件(加载器+类名)，
class JVMTest{
    public void testJVM(){
        //object allocation on heap
        //SOF on java/native stack，就是递归
        //OOM on 堆、栈、常量池、方法区、直接内存
    }
}


