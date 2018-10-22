package com.wanaright.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CFThread {
    private static volatile ThreadLocal<Object> THREAD_LOCAL;
    private List<Runnable> paralleTasks;
    private CountDownLatch countDownLatch;

    public static void putThreadObject(Object object) {
        newThreadLocal();
        THREAD_LOCAL.set(object);
    }

    public static <T> T getThreadObject() {
        if (THREAD_LOCAL == null) {
            return null;
        }
        return (T) THREAD_LOCAL.get();
    }

    public static void clearThreadObject() {
        if (THREAD_LOCAL != null) {
            THREAD_LOCAL.remove();
        }
    }

    public static CFThread prepareParalleTasks(int taskNumber) {
        CFThread cfThread = new CFThread();
        cfThread.setParalleTasks(new ArrayList<>(taskNumber));
        cfThread.setCountDownLatch(new CountDownLatch(taskNumber));
        return cfThread;
    }

    public static CFThread prepareParalleTasks() {
        CFThread cfThread = new CFThread();
        cfThread.setParalleTasks(new ArrayList<>());
        return cfThread;
    }

    public CFThread addTask(Runnable task) {
        this.paralleTasks.add(task);
        return this;
    }

    public CFThread execute() {
        if (this.countDownLatch == null) {
            countDownLatch = new CountDownLatch(this.paralleTasks.size());
        }

        paralleTasks.parallelStream()
                .map(task -> new Thread(() -> {
                    task.run();
                    countDownLatch.countDown();
                }))
                .forEach(Thread::run);
        return this;
    }

    public void waitingForTasksDone() {
        try {
            this.countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void waitingForTasksDone(long timeout, TimeUnit unit) {
        try {
            this.countDownLatch.await(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParalleTasks(List<Runnable> paralleTasks) {
        this.paralleTasks = paralleTasks;
    }

    private void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    //    /**
//     * @param releaseCPU if false means thread will keep running,
//     *                   if true means release cup resource, other thread can take cpu resource and running
//     */
//    public static void sleep(long mills, boolean releaseCPU) {
//        try {
//            if (releaseCPU) {
//                Thread.sleep(mills);
//            } else {
//                long weakTime = System.currentTimeMillis() + mills;
//                while (true) {
//                    if (System.currentTimeMillis() >= weakTime) {
//                        break;
//                    }
//                }
//            }
//        } catch (InterruptedException e) {
//            //nothing
//        }
//    }

    private static void newThreadLocal() {
        if (THREAD_LOCAL == null) {
            synchronized (CFThread.class) {
                if (THREAD_LOCAL == null) {
                    THREAD_LOCAL = new ThreadLocal<>();
                }
            }
        }
    }

    private static void notNull(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }
}
