package com.l1yp.concurrent;

import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Lyp
 * @Date   2020-07-07
 * @Email  l1yp@qq.com
 */
@Slf4j
public class HashThreadPoolExecutor {

    private final int corePoolSize;
    private final ThreadFactory threadFactory;
    private final Worker[] workers;

    /**
     * 哈希线程池, 每个线程对应一个worker
     *
     * @param corePoolSize  线程数
     */
    public HashThreadPoolExecutor(int corePoolSize) {
        this(corePoolSize, new DefaultThreadFactory("hash-pool"));
    }

    /**
     * 哈希线程池, 每个线程对应一个worker
     *
     * @param corePoolSize  线程数
     * @param threadFactory
     */
    public HashThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
        if (corePoolSize <= 0){
            throw new IllegalArgumentException("The corePoolSize must great equals zero");
        }
        this.corePoolSize = corePoolSize;
        this.threadFactory = threadFactory;
        this.workers = new Worker[corePoolSize];
        advanceRunState(RUNNING);
    }

    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    private final Object lock = new Object();

    public void execute(int idx, Runnable task){
        if (idx < 0 || idx >= this.corePoolSize){
            throw new IndexOutOfBoundsException("expected: 0 <= idx <= " + (corePoolSize - 1));
        }
        int state = ctl.get();
        if (runStateOf(state) != RUNNING){
            return;
        }

        if (this.workers[idx] == null || this.workers[idx].thread == null || !this.workers[idx].thread.isAlive()){
            synchronized (lock){
                if (this.workers[idx] == null){
                    do {
                        Thread.yield();
                    }while (compareAndIncrementWorkerCount(state));
                    this.workers[idx] = new Worker(task);
                    this.workers[idx].thread.start();
                    // log.info("new Worker: {}", idx);
                }
            }
            return;
        }
        this.workers[idx].addTask(task);
    }

    private final class Worker implements Runnable {

        transient LinkedBlockingQueue<Runnable> tasks;
        /** Thread this worker is running in.  Null if factory fails. */
        transient final Thread thread;
        /** Initial task to run.  Possibly null. */
        transient Runnable firstTask;
        /** Per-thread task counter */
        transient long completedTasks;

        volatile transient boolean isRunning;


        /**
         * Creates with given first task and thread from ThreadFactory.
         * @param firstTask the first task (null if none)
         */
        Worker(Runnable firstTask) {
            this.firstTask = firstTask;
            this.tasks = new LinkedBlockingQueue<>();
            this.thread = getThreadFactory().newThread(this);
        }

        void addTask(Runnable task) {
            tasks.offer(task);
        }

        Runnable getTask() {
            try {
                return tasks.take();
            } catch (InterruptedException e) {
                return null;
            }
        }

        /** Delegates main run loop to outer runWorker  */
        public void run() {
            isRunning = false;
            Runnable task = firstTask;
            while (task != null || (task = getTask()) != null) {
                try {
                    beforeExecute(thread, task);
                    Throwable thrown = null;
                    try {
                        isRunning = true;
                        task.run();
                    } catch (RuntimeException | Error x) {
                        thrown = x; throw x;
                    } catch (Throwable x) {
                        thrown = x; throw new Error(x);
                    } finally {
                        isRunning = false;
                        afterExecute(task, thrown);
                    }
                } finally {
                    task = null;
                    completedTasks++;
                }
            }

            int state = ctl.get();
            if (runStateOf(state) == SHUTDOWN && latch != null){
                latch.countDown();
            }
            do { }while (compareAndDecrementWorkerCount(state));
            state = ctl.get();
            if (workerCountOf(state) == 0) {
                advanceRunState(TERMINATED);
            }

        }

    }

    /**
     * The main pool control state, ctl, is an atomic integer packing
     * two conceptual fields
     *   workerCount, indicating the effective number of threads
     *   runState,    indicating whether running, shutting down etc
     *
     * In order to pack them into one int, we limit workerCount to
     * (2^29)-1 (about 500 million) threads rather than (2^31)-1 (2
     * billion) otherwise representable. If this is ever an issue in
     * the future, the variable can be changed to be an AtomicLong,
     * and the shift/mask constants below adjusted. But until the need
     * arises, this code is a bit faster and simpler using an int.
     *
     * The workerCount is the number of workers that have been
     * permitted to start and not permitted to stop.  The value may be
     * transiently different from the actual number of live threads,
     * for example when a ThreadFactory fails to create a thread when
     * asked, and when exiting threads are still performing
     * bookkeeping before terminating. The user-visible pool size is
     * reported as the current size of the workers set.
     *
     * The runState provides the main lifecycle control, taking on values:
     *
     *   RUNNING:  Accept new tasks and process queued tasks
     *   SHUTDOWN: Don't accept new tasks, but process queued tasks
     *   STOP:     Don't accept new tasks, don't process queued tasks,
     *             and interrupt in-progress tasks
     *   TIDYING:  All tasks have terminated, workerCount is zero,
     *             the thread transitioning to state TIDYING
     *             will run the terminated() hook method
     *   TERMINATED: terminated() has completed
     *
     * The numerical order among these values matters, to allow
     * ordered comparisons. The runState monotonically increases over
     * time, but need not hit each state. The transitions are:
     *
     * RUNNING -> SHUTDOWN
     *    On invocation of shutdown(), perhaps implicitly in finalize()
     * (RUNNING or SHUTDOWN) -> STOP
     *    On invocation of shutdownNow()
     * SHUTDOWN -> TIDYING
     *    When both queue and pool are empty
     * STOP -> TIDYING
     *    When pool is empty
     * TIDYING -> TERMINATED
     *    When the terminated() hook method has completed
     *
     * Threads waiting in awaitTermination() will return when the
     * state reaches TERMINATED.
     *
     * Detecting the transition from SHUTDOWN to TIDYING is less
     * straightforward than you'd like because the queue may become
     * empty after non-empty and vice versa during SHUTDOWN state, but
     * we can only terminate if, after seeing that it is empty, we see
     * that workerCount is 0 (which sometimes entails a recheck -- see
     * below).
     */
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS; // -536870912
    private static final int SHUTDOWN   =  0 << COUNT_BITS; //  0
    private static final int STOP       =  1 << COUNT_BITS; //  536870912
    private static final int TIDYING    =  2 << COUNT_BITS; // 1073741824
    private static final int TERMINATED =  3 << COUNT_BITS; // 1610612736

    // Packing and unpacking ctl
    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }

    private CountDownLatch latch = null;

    public void shutdown(){
        advanceRunState(SHUTDOWN);
        for (Worker worker : this.workers) {
            if (worker != null){
                worker.thread.interrupt();
            }
        }
        int state = ctl.get();
        int wc = workerCountOf(state);
        latch = new CountDownLatch(wc);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void advanceRunState(int targetState) {
        for (;;) {
            int c = ctl.get();
            if (runStateAtLeast(c, targetState) ||
                    ctl.compareAndSet(c, ctlOf(targetState, workerCountOf(c))))
                break;
        }
    }

    private static boolean runStateAtLeast(int c, int s) {
        return c >= s;
    }

    private boolean compareAndIncrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect + 1);
    }

    private boolean compareAndDecrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect - 1);
    }

    public boolean isShutdown(){
        return runStateOf(ctl.get()) == SHUTDOWN;
    }

    protected void beforeExecute(Thread t, Runnable r) { }

    protected void afterExecute(Runnable r, Throwable t) { }

}
