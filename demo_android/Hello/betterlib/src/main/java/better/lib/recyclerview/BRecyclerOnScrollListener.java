package better.lib.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Created by Better on 2016/3/13.
 * thk http://www.cnblogs.com/tianzhijiexian/p/4397552.html
 * thk http://blog.csdn.net/zhangphil/article/details/50375244提供了一种判断是否填充整个屏幕的方法，虽然并没有起到用处.
 * 如果item数量不足以填满整个屏幕会一直显示loadingView;
 */
public abstract class BRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }
    /**
     * layoutManager的类型（枚举）
     */
    protected LAYOUT_MANAGER_TYPE layoutManagerType;
    /**
     * 最后一个的位置，用于瀑布流
     */
    private int[] lastPositions;
    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;
    /**
     * 当前滑动的状态
     */
    private int currentScrollState = 0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
//        log("onScrolled");
        RecyclerView.LayoutManager manager=recyclerView.getLayoutManager();
        if(null==manager) throw new RuntimeException("添加了ScrollLisetner 但是没事设置LayoutManager");
        if (null==layoutManagerType){
            if(manager instanceof LinearLayoutManager){
                layoutManagerType=LAYOUT_MANAGER_TYPE.LINEAR;
            }else if(manager instanceof GridLayoutManager){
                layoutManagerType=LAYOUT_MANAGER_TYPE.GRID;
            }else if (manager instanceof StaggeredGridLayoutManager){
                layoutManagerType=LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
            }else{
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }
        switch (layoutManagerType){
            case LINEAR:
                lastVisibleItemPosition=((LinearLayoutManager)manager).findLastVisibleItemPosition();
                break;
            case GRID:
                lastVisibleItemPosition=((GridLayoutManager)manager).findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager
                        = (StaggeredGridLayoutManager) manager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        log("onScrollStateChanged");
        currentScrollState=newState;
        RecyclerView.LayoutManager manager=recyclerView.getLayoutManager();
        int totalCount=manager.getItemCount();
        int visibleCount=manager.getChildCount();

        if(visibleCount>0&&currentScrollState==RecyclerView.SCROLL_STATE_IDLE/*滚动停止*/&&lastVisibleItemPosition>=totalCount-1){
            onBottom();
        }
//        switch (layoutManagerType){
//            case LINEAR:
//                log("last="+lastVisibleItemPosition);
//                View lastView=manager.findViewByPosition(lastVisibleItemPosition);
//                BRecyclerView bRecyclerView= (BRecyclerView) recyclerView;
//                if(lastView.getBottom()<recyclerView.getBottom()){
//                    log("没有充满整个屏幕");
//                    if (null!=bRecyclerView) ((HeaderViewProxyRecyclerAdapter)bRecyclerView.getAdapter()).closeFooterView();
//                }else if (lastView.getBottom()==recyclerView.getBottom()){
//                    log("重合");
//                } else if (lastView.getBottom()==recyclerView.getBottom()){
//                    log("海美到底");
//                }
//                break;
//            case GRID:
//                break;
//            case STAGGERED_GRID:
//                break;
//        }
    }

    /**
     * 用于瀑布流
     * @param lastPositions
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
    public abstract void onBottom();
    private void log(String string){
        Log.v(BRecyclerOnScrollListener.class.getSimpleName(),string);
    }
}
