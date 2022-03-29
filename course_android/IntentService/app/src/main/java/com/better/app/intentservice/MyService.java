package com.better.app.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * https://stackoverflow.com/questions/22485298/stopself-vs-stopselfint-vs-stopserviceintent
 * 为什么连续启动两次IntentService，同一个IntentService可以执行两次onHandleIntent
 * I hope this will help you:
 * <p>
 * A started service must manage its own lifecycle. That is, the system does not stop
 * or destroy the service unless it must recover system memory and the service continues
 * to run after onStartCommand() returns. So, the service must stop itself by calling stopSelf()
 * or another component can stop it by calling stopService().
 * <p>
 * Once requested to stop with stopSelf() or stopService(), the system destroys the service as soon as possible.
 * <p>
 * However, if your service handles multiple requests to onStartCommand() concurrently,
 * then you shouldn't stop the service when you're done processing a start request,
 * because you might have since received a new start request (stopping at the end of
 * the first request would terminate the second one). To avoid this problem, you can
 * use stopSelf(int) to ensure that your request to stop the service is always based on the most recent start request.
 * <p>
 * That is, when you call stopSelf(int), you pass the ID of the start request (the startId
 * delivered to onStartCommand()) to which your stop request corresponds. Then if the service
 * received a new start request before you were able to call stopSelf(int), then the ID will
 * not match and the service will not stop.
 * <p>
 * Here's a simplified description:
 * 1. stopSelf() is used to always stop the current service.
 * 2. stopSelf(int startId) is also used to stop the current service, but only if startId was the ID specified the last time the service was started.
 * 3. stopService(Intent service) is used to stop services, but from outside the service to be stopped.
 */
public class MyService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * name Used to name the worker thread, important only for debugging.
     */
    public MyService() {
        super("Test-IntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        /**
         * 虽然 onHandleIntent()调用后，立马调用了stopSelf(startIf)。看起来ServiceHandler只能处理一个消息就销毁服务了。
         * 但是实际情况，并不会立马销毁当前的服务，因为stopSelf有两种调用方式：
         * 1. stopSelf() 立马销毁当前服务
         * 2. stopSelf(startId) 这个是根据onStatCommand()来匹配进行销毁。如果startId是最后一次启动服务传递的startId才销毁。
         * 所以同时启动两次IntentService，才会调用两次 {onHandleIntent()}
         * 所以IntentService是可以完成这样的工作：做完一个任务后，在做下一个任务。
         */
        if (null == intent) {
            Log.d("Better", "onHandleIntent null intent");
            return;
        }
        String taskId = intent.getStringExtra("task_id");
        Log.d("Better", MyService.this + " task: " + taskId);
        if (TextUtils.equals("task01", taskId)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("Better", MyService.this + " task: " + taskId + " done!!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Better", MyService.this + "onDestroy()");
    }
}
