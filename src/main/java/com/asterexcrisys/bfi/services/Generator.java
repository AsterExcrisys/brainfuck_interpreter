package com.asterexcrisys.bfi.services;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

@SuppressWarnings("unused")
public abstract class Generator<T> implements Iterable<T>, AutoCloseable {

    private static final ThreadGroup THREAD_GROUP;

    private final SynchronousQueue<T> queue;
    private final BlockingQueue<Object> signalQueue;
    private final ExecutorService executor;

    private volatile Throwable taskException;
    private volatile boolean isCompleted;

    static {
        THREAD_GROUP = new ThreadGroup(Generator.class.getName());
    }

    public Generator() {
        this.queue = new SynchronousQueue<>();
        this.signalQueue = new ArrayBlockingQueue<>(1);
        this.executor = Executors.newSingleThreadExecutor(task -> {
            Thread thread = new Thread(THREAD_GROUP, task);
            thread.setDaemon(true);
            return thread;
        });
        this.isCompleted = false;
        this.taskException = null;
    }

    public Iterator<T> iterator() {
        initialize();
        return new Iterator<>() {

            private T nextItem;
            private boolean hasNext = fetchNext();

            public boolean hasNext() {
                return hasNext;
            }

            public T next() {
                if (!hasNext) {
                    throw new NoSuchElementException();
                }
                T result = nextItem;
                hasNext = fetchNext();
                return result;
            }

            private boolean fetchNext() {
                if (isCompleted) {
                    return false;
                }
                try {
                    signalQueue.put(new Object());
                    nextItem = queue.take();
                    return true;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    isCompleted = true;
                    return false;
                } catch (Exception exception) {
                    isCompleted = true;
                    throw new RuntimeException("Unexpected exception thrown by the generator", exception);
                }
            }

        };
    }

    public void close() throws Exception {
        executor.shutdownNow();
        if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            throw new InterruptedException("Failed to terminate executor before timeout elapsed");
        }
        if (taskException != null) {
            throw new ExecutionException("Exception thrown by the runnable task during execution", taskException);
        }
    }

    public abstract void run() throws Exception;

    public void yield(T element) throws InterruptedException {
        queue.put(element);
        signalQueue.take();
    }

    private void initialize() {
        executor.submit(() -> {
            try {
                signalQueue.take();
                run();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            } catch (Throwable throwable) {
                taskException = throwable;
            } finally {
                isCompleted = true;
                queue.offer(null);
            }
        });
    }

}