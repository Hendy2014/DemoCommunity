package com.lhy.comm;


import android.database.Cursor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.lang.System.out;

/**
 * Created by luohy on 2018/9/19.
 */
public class DesignPatternInAndroid {

    @Test
    public void testDesignPatternInandroid() {
        //SingleInstance，单例模式
//        WindowManager wm = (WindowManager)getSystemService(getApplication().WINDOW_SERVICE);

        //Builder, 建造者，组装模式
//        AlertDialog.Builer builder=new AlertDialog.Builder(context);
//        builder.setIcon(R.drawable.icon)
//                .setTitle("title")
//                .setMessage("message")
//                .setPositiveButton("Button1",
//                        new DialogInterface.OnclickListener(){
//                            public void onClick(DialogInterface dialog,int whichButton){
//                                setTitle("click");
//                            }
//                        })
//                .create()

        //原型模式
//        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//        Intent copyIntent=(Intetn)shareIntent.clone();

        //工厂模式
//        public Object getSystemService(String name) {
//            if (getBaseContext() == null) {
//                throw new IllegalStateException("System services not available to Activities before onCreate()");
//            }
//            //........
//            if (WINDOW_SERVICE.equals(name)) {
//                return mWindowManager;
//            } else if (SEARCH_SERVICE.equals(name)) {
//                ensureSearchManager();
//                return mSearchManager;
//            }
//            //.......
//            return super.getSystemService(name);
//        }

        testSimpleFacotry();

        //工厂方法模式
        testFactoryMethod();

        //策略模式
        //Android在属性动画中使用时间插值器的时候就用到了策略模式。在使用动画时，
        // 你可以选择线性插值器LinearInterpolator、加速减速插值器AccelerateDecelerateInterpolator、
        // 减速插值器DecelerateInterpolator以及自定义的插值器。
        // 这些插值器都是实现根据时间流逝的百分比来计算出当前属性值改变的百分比。通过根据需要选择不同的插值器，实现不同的动画效果。
        //RecyclerView和它的LayoutManager也应用了策略模式，ItemDecoration也是。

        //适配器模式
        //有篇帖子写得很好，就三种，转换类，转换对象，转换接口。

        //状态模式
        //状态模式中，行为是由状态来决定的，不同状态下有不同行为。状态模式和策略模式的结构几乎是一模一样的，主要是他们表达的目的和本质是不同。
        //Android中的典型案例是StateMachine类，其不止使用了状态模式，还封装了整个消息处理的状态机，在wifi/蓝牙/GSM通信消息协议处理中均有应用
        testSimpleState();

        //责任链模式
        //在Android处理点击事件时，父View先接收到点击事件，如果父View不处理则交给子View，把责任依次往下传递；
        //还有Java的异常捕获机制也是责任链模式的一种体现
        {
            abstract class Handler {
                protected Handler successor;
                public abstract void handleRequest();

                public Handler getSuccessor() {
                    return successor;
                }
                public void setSuccessor(Handler successor) {
                    this.successor = successor;
                }
            }
        }

        //解释器模式
        //这个用到的地方也不少，其一就是Android的四大组件需要在AndroidManifest.xml中定义，其实AndroidManifest.xml就定义了，等标签（语句）的属性以及其子标签，规定了具体的使用（语法），通过PackageManagerService（解释器）进行解析。

        //命令模式?

        //观察者模式
        //场景一：但凡实现listener监听接口的，就是观察者，拥有listener实例对象成员变量的，就是被观察者，当变化时会通知观察者。
        //场景二：notify通知，如listview的adapter数据变化会通知listview进行ui更新。
        //案例一：EventBus

        //备忘录模式
        //在不破坏封闭的前提下，捕获一个对象的内部状态，并在对象之外保存这个状态，这样，以后就可将对象恢复到原先保存的状态中。
        //应用场景：运行时的现场保存与恢复。
        //Android案例: Activity的onSaveInstanceState和onRestoreInstanceState就是用到了备忘录模式，分别用于保存和恢复。

        //迭代器模式
        //提供一种方法顺序访问一个容器对象中的各个元素，而不需要暴露该对象的内部表示。
        //容器，可能是一维数组、一维列表、一维键值对列表、二维列表。目前广泛应用的就是所有集合类、数据库的游标Cursor类。
        ArrayList<String> list = new ArrayList<>();
        Iterator<String> ite = list.iterator();
        Cursor cursor;

        //模板方法
        // 定义一个算法框架，让子类继承后实现其算法内容，但无法改变算法的框架。
        //广泛来讲，是定义一套流程框架，子类实现具体环节的逻辑。
        //但一定只是涉及流程，定义方法框架，继承成员属性不算是模板模式吗？ 如果继承成员属性，那应当就退化成简单的继承了吧。而且该设计模式，名字都叫模板方法啦。
        //Android应用案例：四大组件的生命周期，都是流程框架。所有继承之的具体应用实现类，都是使用了模板方法模式。
        //应用场景：算法框架，生命周期回调框架，流程回调框架，(对，流程流程流程，框架框架框架)


        //中介者模式
        //中介者模式包装了一系列对象相互作用的方式，使得这些对象不必相互明显调用，从而使他们可以轻松耦合。
        //中介者对象是将系统从网状结构转为以调停者为中心的星型结构。
        //应用案例：Android在Binder机制中，就用到了中介者模式。MVP架构里面P层其实就是一个中介者，负责协调V和M


        //外观模式
        //基于原有类实现新的接口或功能的包装类。
        //案例: Android中的ContextWrapper。但凡Wrapper类，多是外观、门面包装模式。

        //代理模式
        //分为静态代理与动态代理。  动态代理是在运行时确定代理类与代理关系。
        //通常命名为Delegate，Proxy类。

        //中介者、代理、外观模式三者的区别

        //装饰模式
        //目的与作用：扩展或升级功能，不改变原接口或类接口，然后新增包装类，在其内部修改实现逻辑来达到方法功能增强或变更的能力
        //案例: JDK中的文件流系列类


        //组合模式
        //将对象组成成树形结构，以表示“部分-整体”的层次结构，使得用户对单个对象和组合对象的使用具有一致性。

        //享元模式，略
        //桥接模式，略
    }

    private void testSimpleState() {
        new ClientUser().testUserAction();
    }

    private void testSimpleFacotry() {

    }

    private void testFactoryMethod() {
        List<String> list = new ArrayList<String>();
        Iterator<String> ite = list.iterator();

        Set<String> set = new HashSet<String>();
        ite = set.iterator();
    }


    //状态模式相关类
    public interface TvState {
        public void nextChannel();

        public void prevChannel();

        public void turnUp();

        public void turnDown();
    }

    public class PowerOffState implements TvState {
        public void nextChannel() {
            out.println("power off, can't receive signal !");
        }

        public void prevChannel() {
            out.println("power off, can't receive signal !");
        }

        public void turnUp() {
            out.println("power off, can't receive signal !");
        }

        public void turnDown() {
            out.println("power off, can't receive signal !");
        }
    }

    public class PowerOnState implements TvState {
        public void nextChannel() {
            System.out.println("power on, 接收到信号：下一频道");
        }

        public void prevChannel() {
            System.out.println("power on, 接收到信号：上一频道");
        }

        public void turnUp() {
            System.out.println("power on, 接收到信号：调高音量");
        }

        public void turnDown() {
            System.out.println("power on, 接收到信号：调低音量");
        }
    }

    public class TvSet{
        public TvSet(TvState state) {
            setmTvState(state);
        }

        public TvState getmTvState() {
            return mTvState;
        }

        public void setmTvState(TvState mTvState) {
            this.mTvState = mTvState;
        }

        private TvState mTvState;
    }

    public class TvController {
        public void nextChannel(TvState mTvState) {
            mTvState.nextChannel();
        }

        public void prevChannel(TvState mTvState) {
            mTvState.prevChannel();
        }

        public void turnUp(TvState mTvState) {
            mTvState.turnUp();
        }

        public void turnDown(TvState mTvState) {
            mTvState.turnDown();
        }
    }

    public class ClientUser{
        public void testUserAction(){
            TvController mTvController = new TvController();
            TvSet tv = new TvSet(new PowerOffState());

            out.println("tv power off");
            mTvController.turnUp(tv.getmTvState());
            mTvController.turnDown(tv.getmTvState());
            mTvController.prevChannel(tv.getmTvState());
            mTvController.nextChannel(tv.getmTvState());

            out.println("tv power on");
            tv.setmTvState(new PowerOnState());

            mTvController.turnUp(tv.getmTvState());
            mTvController.turnDown(tv.getmTvState());
            mTvController.prevChannel(tv.getmTvState());
            mTvController.nextChannel(tv.getmTvState());
        }

    }

}
