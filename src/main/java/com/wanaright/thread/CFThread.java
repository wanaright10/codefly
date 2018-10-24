package com.wanaright.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CFThread {
    private static volatile ThreadLocal<Object> THREAD_LOCAL;
    private List<Runnable> paralleTasks;
    private CountDownLatch countDownLatch;
    private boolean block;

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
        return cfThread;
    }

    public static CFThread prepareParalleTasks(boolean block, int taskNumber) {
        CFThread cfThread = prepareParalleTasks(taskNumber);
        if (block) {
            cfThread.setBlock(true);
            cfThread.setCountDownLatch(new CountDownLatch(taskNumber));
        }
        return cfThread;
    }

    public static CFThread prepareParalleTasks(boolean block) {
        CFThread cfThread = prepareParalleTasks();
        cfThread.setBlock(block);
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

    public void execute() {
        runAllTasks();
        if (this.block) {
            waitingForTasksDone();
        }
    }

    public void execute(long timeout, TimeUnit unit) {
        runAllTasks();
        if (this.block) {
            waitingForTasksDone(timeout, unit);
        }
    }

    private void runAllTasks() {
        if (this.countDownLatch == null && this.block) {
            countDownLatch = new CountDownLatch(this.paralleTasks.size());
        }

        paralleTasks.parallelStream()
                .map(task -> new Thread(() -> {
                    task.run();
                    if (this.block) {
                        countDownLatch.countDown();
                    }
                }))
                .forEach(Thread::start);
    }

    private void waitingForTasksDone() {
        try {
            this.countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void waitingForTasksDone(long timeout, TimeUnit unit) {
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

    private void setBlock(boolean block) {
        this.block = block;
    }

    /**
     * block current thread for a while
     *
     * @param releaseCPU if false means thread will keep running, release lock
     *                   if true means release cup resource, other thread can take cpu resource and running, keep lock
     */
    public static void sleep(long mills, boolean releaseCPU) {
        try {
            if (releaseCPU) {
                Thread.sleep(mills);
            } else {
                ReentrantLock lock = new ReentrantLock();
                Condition condition = lock.newCondition();
                lock.lockInterruptibly();
                condition.await(mills, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            //nothing
        }
    }

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
