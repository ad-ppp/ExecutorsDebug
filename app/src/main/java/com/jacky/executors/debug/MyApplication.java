package com.jacky.executors.debug;

import android.app.Application;
import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MyApplication extends Application {
    private static final String tag = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        final File file = new File("/proc/sys/kernel/threads-max");
        final String an = FileUtil.analyzeFile(file);
        Log.d(tag, an);
        dumpFile(file);

        final String s = FileUtil.analyzeFile(new File("/proc/" + Process.myPid() + "/task"));
        Log.d(tag, "task:\n" + s);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                dumpThreadMax();
                dumpLimitsSafe();
                dumpPDs();
                dumpStatus();
                dumpTask();
            }
        });
    }

    private void dumpThreadMax() {
        final File file = new File("/proc/sys/kernel/threads-max");
        Log.d(tag, "start dump threads-max," + file.getAbsolutePath());
        try {
            final String an = FileUtil.analyzeFile(file);
            Log.d(tag, an);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(tag, e.getMessage());
        }
        Log.d(tag, "end dump threads-max");
    }

    private void dumpLimitsSafe() {
        final File file = new File("/proc/" + Process.myPid() + "/limits");
        Log.d(tag, "start dump limits," + file.getAbsolutePath());
        try {
            if (file.isFile()) {
                final String result = dumpFile(file);
                if (result != null) {
                    Log.d(tag, "\n" + result + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(tag, e.getMessage());
        }

        Log.d(tag, "end dump limits");
    }

    private void dumpPDs() {
        final File file = new File("/proc/" + Process.myPid() + "/fd");
        Log.d(tag, "start dump pds," + file.getAbsolutePath());
        if (file.isDirectory()) {
            Log.d(tag, "打开fd的个数" + file.listFiles().length);
        }

        Log.d(tag, "end dump pds");
    }

    private void dumpStatus() {
        final File file = new File("/proc/" + Process.myPid() + "/status");
        Log.d(tag, "start dump status," + file.getAbsolutePath());
        if (file.isFile()) {
            final String result = dumpFile(file);
            if (result != null) {
                Log.d(tag, result);
            }
        }

        Log.d(tag, "end dump status");
    }

    private void dumpTask() {
        final File file = new File("/proc/" + Process.myPid() + "/task");
        Log.d(tag, "start dump task," + file.getAbsolutePath());
        if (file.isFile()) {
            final String result = dumpFile(file);
            if (result != null) {
                Log.d(tag, result);
            }
        } else {
            Log.d(tag, FileUtil.analyzeFile(file));
        }

        Log.d(tag, "end dump task");
    }

    private String dumpFile(File file) {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            final StringBuilder stringBuilder = new StringBuilder();
            String line;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(tag, "Dump file error :" + e.getMessage());
        }

        return null;
    }
}
