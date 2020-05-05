package com.jacky.executors.debug;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
        new LinkedBlockingQueue<>(5);
    private static final ThreadPoolExecutor.DiscardOldestPolicy handler =
        new ThreadPoolExecutor.DiscardOldestPolicy() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                super.rejectedExecution(r, e);
                final RunnableWrapper runnableWrapper = (RunnableWrapper) r;
                Log.i("MainActivity", "reject#" + runnableWrapper.id);
            }
        };
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "jack-" + mCount.getAndIncrement());
        }
    };

    private final ExecutorService executor = new ThreadPoolExecutor(
        2, Integer.MAX_VALUE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
        sPoolWorkQueue, sThreadFactory);

    final AtomicInteger task = new AtomicInteger(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 100; i++) {
            executor.execute(new RunnableWrapper(i));
        }
    }

    private class RunnableWrapper implements Runnable {
        final int id;

        RunnableWrapper(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                Log.i("MainActivity", "Thread#" + Thread.currentThread().getName()
                    + " run " + task.getAndIncrement());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
