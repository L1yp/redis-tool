package com.l1yp.concurrent;

import lombok.Data;
import lombok.SneakyThrows;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Lyp
 * @Date 2020-07-31
 * @Email l1yp@qq.com
 */
public class Main {

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);

            t.setUncaughtExceptionHandler((t1, e) -> {
                System.out.println("线程:" + t1.getName() + ",发生异常");
                e.printStackTrace();
            });
            return t;
        }
    }

    @Data
    private static class User {
        private String name;

        public void setName(String name){
            this.name = name;
            System.out.println("name = " + name);
        }
    }

    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setName("");
        System.out.println(user.getName());
        System.exit(0);
        Thread t1 = new Thread(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            throw new RuntimeException("test");
        });
        System.out.println(t1.getState());
        t1.start();

        for (int i = 0; i < 10; i++) {
            System.out.println(t1.getState());
            System.out.println(t1.isAlive());
            Thread.sleep(500);
        }


    }


}
