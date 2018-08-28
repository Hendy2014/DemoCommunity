package com.lhy.comm;

import org.junit.Test;

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
public class ConcurrentTest {

    @Test
    public void testCallableFuture() {
        Callable<String> callable = new CallableImpl("mu");

        //Callable的第一种用法： Thread + FutureTask + Callable + Runnable
        //FutureTask继承了RunnableFuture,线程取Runnable.run()去执行，即执行了FutureTask.run()方法
        //在FutureTask.run()，封装了Callable任务的执行，结果返回，任务状态处理等机制
        FutureTask<String> task = new FutureTask<>(callable);
        new Thread(task).start();
        long beginTime = System.currentTimeMillis();

        String result = null;
        try {
            result = task.get();//该方法是阻塞的
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            //执行过程状态异常，返回结果时报异常
            e.printStackTrace();
        } catch (CancellationException e) {
            //执行过程可能被取消了
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("hello : " + result);
        System.out.println("cost : " + (endTime - beginTime) + " ms");
        System.out.println("说明FutureTask.get()方法是阻塞的，被任务线程阻塞了1秒");

        //Callable的第二种用法： Executors + ExecutorService + Future + Callable
        //Executors是线程池的工厂方法，用于创建(生产)各种各样的线程池的实例
        // 定义3个Callable类型的任务

        MyCallable task1 = new MyCallable(0);
        MyCallable task2 = new MyCallable(1);
        MyCallable task3 = new MyCallable(2);

        // 创建一个执行任务的服务
        ExecutorService es = Executors.newFixedThreadPool(3);
        try {
            // 提交并执行任务，任务启动时返回了一个Future对象，
            // 如果想得到任务执行的结果或者是异常可对这个Future对象进行操作
            Future future1 = es.submit(task1);
            // 获得第一个任务的结果，如果调用get方法，当前线程会等待任务执行完毕后才往下执行
            beginTime = System.currentTimeMillis();
            System.out.println("task1 start ..." );
            System.out.println("task1 result: " + future1.get());
            endTime = System.currentTimeMillis();
            System.out.println("task1 end, cost time:" + (endTime - beginTime));
            System.out.println("说明callable任务丢给Executor线程池返回的Future对象，Future是可获取执行结果的");

            Future future2 = es.submit(task2);
            // 等待5秒后，再停止第二个任务。因为第二个任务进行的是无限循环
            Thread.sleep(5000);
            System.out.println("task2 cancel: " + future2.cancel(true));
            System.out.println("说明callable任务丢给Executor线程池返回的Future对象，Future是可取消的，取消后工作线程的死循环被中断了");

            // 获取第三个任务的输出，因为执行第三个任务会引起异常
            // 所以下面的语句将引起异常的抛出
            Future future3 = es.submit(task3);
            System.out.println("task3: " + future3.get());
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("说明callable任务丢给Executor线程池返回的Future对象，用Future获取执行结果，是可能报异常的，异常原因可能是任务被取消，任务状态机制异常等");
        }

        // 停止任务执行服务
        es.shutdownNow();
    }

    public static class MyCallable implements Callable {
        private int flag = 0;

        public MyCallable(int flag) {
            this.flag = flag;
        }

        public String call() throws Exception {
            if (this.flag == 0) {
                return "flag = 0";
            }

            if (this.flag == 1) {
                try {
                    while (true) {
                        System.out.println("looping.");
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
                return "false";
            } else {
                throw new Exception("Bad flag value!");
            }
        }
    }

    public class CallableImpl implements Callable<String> {
        private String acceptStr;

        public CallableImpl(String acceptStr) {
            this.acceptStr = acceptStr;
        }

        @Override
        public String call() throws Exception {
            // 任务阻塞 1 秒
            Thread.sleep(1000);
            return this.acceptStr + " append some chars and return it!";
        }
    }
}
