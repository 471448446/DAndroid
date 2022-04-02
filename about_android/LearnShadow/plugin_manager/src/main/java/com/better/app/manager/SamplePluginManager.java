package com.better.app.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.better.app.plugin_constant.Constant;
import com.tencent.shadow.core.manager.installplugin.InstalledPlugin;
import com.tencent.shadow.dynamic.host.EnterCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SamplePluginManager extends FastPluginManager {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Context mCurrentContext;

    public SamplePluginManager(Context context) {
        super(context);
        mCurrentContext = context;
    }

    /**
     * @return PluginManager实现的别名，用于区分不同PluginManager实现的数据存储路径
     */
    @Override
    protected String getName() {
        return "test-dynamic-manager";
    }

    /**
     * @return 宿主so的ABI。插件必须和宿主使用相同的ABI。
     */
    @Override
    public String getAbi() {
        return "";
    }

    /**
     * @return 宿主中注册的PluginProcessService实现的类名
     */
    @Override
    protected String getPluginProcessServiceName(String partKey) {
        if ("sample-plugin-app".equals(partKey)) {
            return "com.tencent.shadow.sample.host.PluginProcessPPS";
        } else if ("sample-plugin-app2".equals(partKey)) {
            return "com.tencent.shadow.sample.host.Plugin2ProcessPPS";//在这里支持多个插件
        } else {
            //如果有默认PPS，可用return代替throw
            throw new IllegalArgumentException("unexpected plugin load request: " + partKey);
        }
    }

    @Override
    public void enter(final Context context, long fromId, Bundle bundle, final EnterCallback callback) {
        if (fromId == Constant.FROM_ID_START_ACTIVITY) {
            onStartActivity(context, bundle, callback);
        } else {
            throw new IllegalArgumentException("不认识的fromId==" + fromId);
        }
    }

    private void onStartActivity(final Context context, Bundle bundle, final EnterCallback callback) {
        final String pluginZipPath = bundle.getString(Constant.KEY_PLUGIN_ZIP_PATH);
        final String partKey = bundle.getString(Constant.KEY_PLUGIN_PART_KEY);
        final String className = bundle.getString(Constant.KEY_ACTIVITY_CLASSNAME);
        if (className == null) {
            throw new NullPointerException("className == null");
        }
        final Bundle extras = bundle.getBundle(Constant.KEY_EXTRAS);

        if (callback != null) {
            final View view = LayoutInflater.from(mCurrentContext).inflate(R.layout.activity_load_plugin, null);
            callback.onShowLoadingView(view);
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    InstalledPlugin installedPlugin = installPlugin(pluginZipPath, null, true);
                    Intent pluginIntent = new Intent();
                    pluginIntent.setClassName(
                            context.getPackageName(),
                            className
                    );
                    if (extras != null) {
                        pluginIntent.replaceExtras(extras);
                    }

                    startPluginActivity(installedPlugin, partKey, pluginIntent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (callback != null) {
                    callback.onCloseLoadingView();
                }
            }
        });
    }
}
