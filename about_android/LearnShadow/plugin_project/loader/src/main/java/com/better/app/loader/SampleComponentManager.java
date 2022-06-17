package com.better.app.loader;

import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.tencent.shadow.core.loader.infos.ContainerProviderInfo;
import com.tencent.shadow.core.loader.managers.ComponentManager;

public class SampleComponentManager extends ComponentManager {

    /**
     * sample-runtime 模块中定义的壳子Activity，需要在宿主AndroidManifest.xml注册
     */
    private static final String DEFAULT_ACTIVITY = "com.better.app.runtime.PluginDefaultProxyActivity";
    private static final String SINGLE_INSTANCE_ACTIVITY = "com.better.app.runtime.PluginSingleInstance1ProxyActivity";
    private static final String SINGLE_TASK_ACTIVITY = "com.better.app.runtime.PluginSingleTask1ProxyActivity";

    private Context context;

    public SampleComponentManager(Context context) {
        this.context = context;
        Log.d("BetterLearnShadow", "SampleComponentManager() create");
    }


    /**
     * 配置插件Activity 到 壳子Activity的对应关系
     *
     * @param pluginActivity 插件Activity
     * @return 壳子Activity
     */
    @Override
    public ComponentName onBindContainerActivity(ComponentName pluginActivity) {
        /**
         * 这里配置对应的对应关系
         */
        String which = DEFAULT_ACTIVITY;
        switch (pluginActivity.getClassName()) {
            case "com.better.app.plugin1.MainActivity":
            case "com.better.app.plugin1.MainActivityJava":
                which = SINGLE_TASK_ACTIVITY;
                break;
        }
        Log.d("BetterLearnShadow", "onBindContainerActivity:" + pluginActivity.getClassName() + "->" + which);
        return new ComponentName(context, which);
    }

    /**
     * 配置对应宿主中预注册的壳子contentProvider的信息
     */
    @Override
    public ContainerProviderInfo onBindContainerContentProvider(ComponentName pluginContentProvider) {
        return new ContainerProviderInfo(
                "com.tencent.shadow.core.runtime.container.PluginContainerContentProvider",
                context.getPackageName() + ".contentprovider.authority.dynamic");
    }
//
//    @Override
//    public List<BroadcastInfo> getBroadcastInfoList(String partKey) {
//        List<ComponentManager.BroadcastInfo> broadcastInfos = new ArrayList<>();
//
//        //如果有静态广播需要像下面代码这样注册
////        if (partKey.equals(Constant.PART_KEY_PLUGIN_MAIN_APP)) {
////            broadcastInfos.add(
////                    new ComponentManager.BroadcastInfo(
////                            "com.tencent.shadow.demo.usecases.receiver.MyReceiver",
////                            new String[]{"com.tencent.test.action"}
////                    )
////            );
////        }
//        return broadcastInfos;
//    }

}
