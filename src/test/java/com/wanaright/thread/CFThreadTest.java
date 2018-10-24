package com.wanaright.thread;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CFThreadTest {

    @Test
    public void threadLocal() {
        String object = "hello world";

        CFThread.putThreadObject(object);

        String getObj = CFThread.getThreadObject();
        Assert.assertEquals(getObj, object);
    }

    @Test
    public void threadLocalFalse() throws InterruptedException {
        final String object = "hello world";

        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            CFThread.putThreadObject(object);
            latch.countDown();
        }).start();

        latch.await();

        String getObj = CFThread.getThreadObject();
        Assert.assertNull(getObj);
    }

    @Test
    public void paralleTasks() {
        CFThread.prepareParalleTasks(true)
                .addTask(() -> System.out.println("Task 1"))
                .addTask(() -> System.out.println("Task 2"))
                .addTask(() -> {
                    CFThread.sleep(2000, false);
                    System.out.println("Task 3");
                })
                .addTask(() -> System.out.println("Task 4"))
                .execute();

        System.out.println("all tasks done");
    }

    @Test
    public void sleep() {
        LocalDateTime now = LocalDateTime.now();

        CFThread.sleep(TimeUnit.SECONDS.toMillis(3), false);

        Assert.assertTrue(now.plusSeconds(3).isBefore(LocalDateTime.now()));
    }
}