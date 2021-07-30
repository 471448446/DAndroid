package com.better.learn.filelock;

public interface LockFile {
    /**
     * 尝试获取文件锁，直到获取(非阻塞)
     *
     * @return true 成功，false 失败
     */
    boolean tryLock();

    /**
     * 尝试获取文件锁，阻塞当前线程，直到获取(阻塞)
     *
     * @return true 成功，false 失败
     */
    boolean lock();

    /**
     * 是否文件锁
     */
    void unLock();

    /**
     * 初始化文件资源
     */
    void onCreate();

    /**
     * 释放文件资源
     */
    void onDestroy();
}
