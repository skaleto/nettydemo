package im.server.task;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FutureTask extends Thread {

    //需要一个队列来存放需要处理的消息
//    private ConcurrentLinkedQueue<Runnable> taskQueue=new ConcurrentLinkedQueue<>();
    //需要一个线程池来执行任务
    private static ExecutorService pool= Executors.newFixedThreadPool(10);

    public static void addTask(Runnable runnable){
        pool.submit(runnable);
    }

}
