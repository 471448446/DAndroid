package better.wmoney;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * getRootInActiveWindow()
 * event.getSource() 一样
 * 在微信列表页面，获取不到每个列表的文字信息，在聊天界面点击红包后获取不到页面的布局信息，也就获取不到红的文字信息。所以也就没有上面卵用
 * Created by better on 2017/1/22.
 */
public class HelpAccessibilityService extends AccessibilityService {
    private static final String TAGS = "HelpAccessibility";
    private static final String WECHAT_DETAILS_EN = "Details";
    private static final String WECHAT_DETAILS_CH = "红包详情";
    private static final String WECHAT_BETTER_LUCK_EN = "Better luck next time!";
    private static final String WECHAT_BETTER_LUCK_CH = "手慢了";
    private static final String WECHAT_EXPIRES_CH = "已超过24小时";
    private static final String WECHAT_VIEW_SELF_CH = "查看红包";
    private static final String WECHAT_VIEW_OTHERS_CH = "领取红包";
    private static final String WECHAT_NOTIFICATION_TIP = "[微信红包]";
    private static final String WECHAT_LUCKMONEY_GENERAL_ACTIVITY = "LauncherUI";
    private static final String WECHAT_LUCKMONEY_CHATTING_ACTIVITY = "ChattingUI";

    /* 界面初始化，准备检查红包  --better 2017/1/23 11:03. */
    private static final int S_INIT = 10;
    /* 检查到红包  --better 2017/1/23 11:04. */
    private static final int S_RED_ONE = S_INIT + 1;
    /* 打开红包可以点击  --better 2017/1/23 11:20. */
    private static final int S_RED_CLICK = S_RED_ONE + 1;
    /* 没有检查到红包，指的是红包被抢完，过期  --better 2017/1/23 11:04. */
    private static final int S_RED_NONE = S_RED_CLICK + 1;

    private int mCurrentRedStatus = S_INIT;
    /* 已经获取到的  --better 2017/1/24 09:45. */
    private List<String> fetchedIdentifiers = new ArrayList<>();
    private String currentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
    private AccessibilityNodeInfo rootNodeInfo, mReceiveNode, mUnpackNode;
    private boolean mLuckyMoneyReceived;
    private boolean mMutex = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 通过这个函数可以接收系统发送来的AccessibilityEvent，接收来的AccessibilityEvent是经过过滤的，过滤是在配置工作时设置的。
        Log.d(TAGS, event.toString());
        Log.d(TAGS, event.getText().toString());
        setCurrentActivityName(event);
        if (getSettingNotify() && watchNotifications(event) ||
                getSettingList() && watchList(event)) {
            l("---返回，不消耗这次事件");
            return;
        }
        l("---准备监控聊天界面");
        if (getSettingChat()) watchWChat(event);

    }

    private boolean getSettingChat() {
//        return sharedPreferences.getBoolean("pref_watch_chat", false);
        return true;
    }

    private boolean getSettingList() {
//        return sharedPreferences.getBoolean("pref_watch_list", false);
        return true;
    }

    private boolean getSettingNotify() {
//        return sharedPreferences.getBoolean("pref_watch_notification", false);
        return true;
    }

    private boolean getSettingUseSelfe() {
//        return sharedPreferences.getBoolean("pref_watch_self", false);
        return true;
    }

    private void setCurrentActivityName(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }

        try {
            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(),
                    event.getClassName().toString()
            );

            getPackageManager().getActivityInfo(componentName, 0);
            currentActivityName = componentName.flattenToShortString();
            l("" + currentActivityName);
        } catch (PackageManager.NameNotFoundException e) {
            currentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
        }
    }

    private boolean watchNotifications(AccessibilityEvent event) {
        // Not a notification
        if (event.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            l("不监控通知");
            return false;
        }
        l("监控通知");
        // Not a hongbao
        String tip = event.getText().toString();
        if (!tip.contains(WECHAT_NOTIFICATION_TIP)) return true;
        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) event.getParcelableData();
            PendingIntent pendingIntent = notification.contentIntent;
            try {
                if (null != pendingIntent)
                    pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private boolean watchList(AccessibilityEvent event) {
        AccessibilityNodeInfo eventSource = event.getSource();
        // Not a message
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || eventSource == null) {
            l("不监控列表");
            return false;
        }
        l("监控列表");
        List<AccessibilityNodeInfo> nodes = eventSource.findAccessibilityNodeInfosByText(WECHAT_NOTIFICATION_TIP);
        //增加条件判断currentActivityName.contains(WECHAT_LUCKMONEY_GENERAL_ACTIVITY)
        //避免当订阅号中出现标题为“[微信红包]拜年红包”（其实并非红包）的信息时误判
        if (!nodes.isEmpty() && currentActivityName.contains(WECHAT_LUCKMONEY_GENERAL_ACTIVITY)) {
            AccessibilityNodeInfo nodeToClick = nodes.get(0);
            if (nodeToClick == null) return false;
            CharSequence contentDescription = nodeToClick.getContentDescription();
            if (TextUtils.isEmpty(contentDescription)) {
                nodeToClick.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            }
        }
        return false;
    }

    private void watchWChat(AccessibilityEvent event) {
        l3("进入watchWChat");
        this.rootNodeInfo = getRootInActiveWindow();
        if (rootNodeInfo == null) return;
        initStatus(event);
        l3("当前状态=" + mCurrentRedStatus);
        switch (mCurrentRedStatus) {
            case S_INIT:
                if (isNoRedInVisible()) return;
                break;
            case S_RED_ONE:
                 /* 如果已经接收到红包并且还没有戳开 */
                if (mLuckyMoneyReceived && (mReceiveNode != null)) {
                    mMutex = true;
                    mReceiveNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    mLuckyMoneyReceived = false;
                    fetchedIdentifiers.add(getHongbaoHash(mReceiveNode));
                    l3("打开红包");
                }
                break;
            case S_RED_CLICK:
                /* 如果戳开但还未领取 */
                l("戳开？？？?" + String.valueOf((mUnpackNode != null)));
                if (mUnpackNode != null) {
                    int delayFlag = 0;
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    try {
                                        mUnpackNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    } catch (Exception e) {
                                        mMutex = false;
                                    }
                                }
                            },
                            delayFlag);
                }
                break;
            case S_RED_NONE:
                l3("红包抢完了");
                mMutex = false;
                performGlobalAction(GLOBAL_ACTION_BACK);
                mCurrentRedStatus = S_INIT;
                break;
        }

    }

    /**
     * Des 当前范围没有红包
     * Create By better on 2017/1/23 13:21.
     */
    private boolean isNoRedInVisible() {
        List<AccessibilityNodeInfo> reds =
                getSettingUseSelfe() ?
                        this.getTheVisibleNode(WECHAT_VIEW_OTHERS_CH, WECHAT_VIEW_SELF_CH) : this.getTheVisibleNode(WECHAT_VIEW_OTHERS_CH);
        return null == reds || reds.isEmpty();
    }

    private void initStatus(AccessibilityEvent event) {
        /* 聊天会话窗口，遍历节点匹配“领取红包”和"查看红包" */
        AccessibilityNodeInfo node1 = getSettingUseSelfe() ?
                this.getTheLastUnPackNode(WECHAT_VIEW_OTHERS_CH, WECHAT_VIEW_SELF_CH) : this.getTheLastUnPackNode(WECHAT_VIEW_OTHERS_CH);
        if (node1 != null &&
                (currentActivityName.contains(WECHAT_LUCKMONEY_CHATTING_ACTIVITY)
                        || currentActivityName.contains(WECHAT_LUCKMONEY_GENERAL_ACTIVITY))) {
            l3("有红包");
            mReceiveNode = node1;
            mLuckyMoneyReceived = true;
            mCurrentRedStatus = S_RED_ONE;
            return;
        }
        /* 戳开红包，红包还没抢完，遍历节点匹配“拆红包” */
        AccessibilityNodeInfo node2 = findOpenButton(this.rootNodeInfo);
        if (node2 != null && "android.widget.Button".equals(node2.getClassName())) {
            l3("戳开--准备");
            mUnpackNode = node2;
            mCurrentRedStatus = S_RED_CLICK;
            return;
        } else if (null == node2) {
            l3("戳开失败node2 null");
        } else {
            l3("戳开失败 node2不是button is " + node2.getClassName());
        }
        /* 戳开红包，红包已被抢完，遍历节点匹配“红包详情”和“手慢了” */
        boolean hasNodes = this.hasOneOfThoseNodes(WECHAT_BETTER_LUCK_CH, WECHAT_DETAILS_CH, WECHAT_BETTER_LUCK_EN, WECHAT_DETAILS_EN, WECHAT_EXPIRES_CH);
        if (mMutex
                && event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                && hasNodes) {
            mCurrentRedStatus = S_RED_NONE;
            return;
        }
        l3("默认初始化状态");
        mCurrentRedStatus = S_INIT;
    }

    private AccessibilityNodeInfo findOpenButton(AccessibilityNodeInfo node) {
        if (node == null)
            return null;
        l3("" + node.getClassName() + ";" + node.getText());
//        l3("" + node.toString());
        //非layout元素
        if (node.getChildCount() == 0) {
//            l3("当前元素类名：" + node.getClassName());
            if ("android.widget.Button".equals(node.getClassName()))
                return node;
            else {
                return null;
            }
        }

        //layout元素，遍历找button
        AccessibilityNodeInfo button;
        for (int i = 0; i < node.getChildCount(); i++) {
            button = findOpenButton(node.getChild(i));
            if (button != null) {
                return button;
            }
        }
        return null;
    }

    private AccessibilityNodeInfo findOpenButtonPlus(AccessibilityNodeInfo node) {
        if (node == null)
            return null;

        //非layout元素
        if (node.getChildCount() == 0) {
            if ("android.widget.Button".equals(node.getClassName()))
                return node;
            else
                return null;
        }

        //layout元素，遍历找button
        AccessibilityNodeInfo button;
        for (int i = 0; i < node.getChildCount(); i++) {
            button = findOpenButton(node.getChild(i));
            if (button != null)
                return button;
        }
        return null;
    }

    private AccessibilityNodeInfo getTheLastUnPackNode(String... texts) {
        int bottom = 0;
        AccessibilityNodeInfo lastNode = null, tempNode;
        List<AccessibilityNodeInfo> nodes;

        for (String text : texts) {
            if (text == null) continue;

            nodes = this.rootNodeInfo.findAccessibilityNodeInfosByText(text);

            if (nodes != null && !nodes.isEmpty()) {
                tempNode = nodes.get(nodes.size() - 1);
                if (tempNode == null) return null;
                Rect bounds = new Rect();
                tempNode.getBoundsInScreen(bounds);
                if (bounds.bottom > bottom) {
                    bottom = bounds.bottom;
                    lastNode = tempNode;
                }
                if (null != lastNode) {
                    String id = getHongbaoHash(lastNode);
                    if (fetchedIdentifiers.contains(id)) {
                        l3("这个红包已经抢过了：" + id);
                        lastNode = null;
                        continue;
                    }
                }
            }
        }
        return lastNode;
    }

    private boolean hasOneOfThoseNodes(String... texts) {
        List<AccessibilityNodeInfo> nodes;
        for (String text : texts) {
            if (text == null) continue;

            nodes = this.rootNodeInfo.findAccessibilityNodeInfosByText(text);

            if (nodes != null && !nodes.isEmpty()) return true;
        }
        return false;
    }

    private List<AccessibilityNodeInfo> getTheVisibleNode(String... texts) {
        List<AccessibilityNodeInfo> nodes = null;
        for (String text : texts) {
            if (text == null) continue;
            List<AccessibilityNodeInfo> list = this.rootNodeInfo.findAccessibilityNodeInfosByText(text);
            if (null != list && !list.isEmpty()) {
                if (nodes == null)
                    nodes = this.rootNodeInfo.findAccessibilityNodeInfosByText(text);
                else
                    nodes.addAll(list);

            }
        }
        return nodes;
    }

    /**
     * 将节点对象的id和红包上的内容合并
     * 用于表示一个唯一的红包
     *
     * @param node 任意对象
     * @return 红包标识字符串
     */
    private String getHongbaoHash(AccessibilityNodeInfo node) {
        /* 获取红包上的文本 */
        String content;
        try {
            AccessibilityNodeInfo i = node.getParent().getChild(0);
            content = i.getText().toString();
        } catch (NullPointerException npr) {
            return null;
        }

        return content + "@" + getNodeId(node);
    }

    /**
     * 获取节点对象唯一的id，通过正则表达式匹配
     * AccessibilityNodeInfo@后的十六进制数字
     *
     * @param node AccessibilityNodeInfo对象
     * @return id字符串
     */
    private String getNodeId(AccessibilityNodeInfo node) {
        /* 用正则表达式匹配节点Object */
        Pattern objHashPattern = Pattern.compile("(?<=@)[0-9|a-z]+(?=;)");
        Matcher objHashMatcher = objHashPattern.matcher(node.toString());
        // AccessibilityNodeInfo必然有且只有一次匹配，因此不再作判断
        objHashMatcher.find();
        return objHashMatcher.group(0);
    }

    @Override
    public void onInterrupt() {
        //这个在系统想要中断AccessibilityService返给的响应时会调用。在整个生命周期里会被调用多次
    }

    private void l(String s) {
        Log.d(TAGS, s);
    }

    private void l3(String s) {
        Log.d(TAGS, "===" + s);
    }
}
