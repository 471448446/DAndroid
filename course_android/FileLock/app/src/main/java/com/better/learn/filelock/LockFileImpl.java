package com.better.learn.filelock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class LockFileImpl implements LockFile {
    private final File file;
    private FileOutputStream fileOutputStream;
    private FileChannel fileChannel;
    private FileLock fileLock;

    public LockFileImpl(File file) {
        this(file, false);
    }

    public LockFileImpl(File file, boolean lazyLock) {
        this.file = file;
        if (!lazyLock) {
            onCreate();
        }
    }

    @Override
    public boolean tryLock() {
        if (null != fileLock && fileLock.isValid()) {
            return true;
        }
        try {
            fileLock = fileChannel.tryLock();
            return null != fileLock;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean lock() {
        if (null != fileLock && fileLock.isValid()) {
            return true;
        }
        try {
            fileLock = fileChannel.lock();
            return null != fileLock;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void unLock() {
        try {
            fileLock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileLock = null;
    }

    @Override
    public void onCreate() {
        try {
            fileOutputStream = new FileOutputStream(file);
            fileChannel = fileOutputStream.getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        unLock();
        try {
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileChannel = null;
        fileOutputStream = null;
    }
}
