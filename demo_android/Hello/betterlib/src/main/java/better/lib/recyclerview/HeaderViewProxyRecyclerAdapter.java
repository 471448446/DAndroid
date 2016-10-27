package better.lib.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import better.lib.waitpolicy.emptyproxy.EmptyViewProxy;

/**
 * Created by Android Studio
 * User: Ailurus(ailurus@foxmail.com)
 * Date: 2015-10-26
 * Time: 18:23
 */
public class HeaderViewProxyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADERS_START = Integer.MIN_VALUE;
    private static final int FOOTERS_START = Integer.MIN_VALUE + 10;
    private static final int ITEMS_START = Integer.MIN_VALUE + 20;
    private static final int ADAPTER_MAX_TYPES = 100;

    private RecyclerView.Adapter mWrappedAdapter;
    private Map<Class, Integer> mItemTypesOffset;

    private EmptyViewProxy mHeadViewProxy,mFooterViewProxy;
    private boolean isShowFooterView=true,isShowHeaderView=true;//是否显示footerView,默认支持

    public HeaderViewProxyRecyclerAdapter(RecyclerView.Adapter adapter) {
        mItemTypesOffset = new HashMap<>();
        setWrappedAdapter(adapter);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (mWrappedAdapter != null && mWrappedAdapter.getItemCount() > 0) {
            notifyItemRangeRemoved(getHeaderCount(), mWrappedAdapter.getItemCount());
        }
        setWrappedAdapter(adapter);
        notifyItemRangeInserted(getHeaderCount(), mWrappedAdapter.getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        int hCount = getHeaderCount();
        if (position < hCount) return HEADERS_START + position;
        else {
            int itemCount = mWrappedAdapter.getItemCount();
            if (position < hCount + itemCount) {
                return getAdapterTypeOffset() + mWrappedAdapter.getItemViewType(position - hCount);
            } else return FOOTERS_START + position - hCount - itemCount;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType < HEADERS_START + getHeaderCount())
//            return new StaticViewHolder(mHeaderViews.get(viewType - HEADERS_START));
            return new StaticViewHolder(mHeadViewProxy.getProxyView());
        else if (viewType < FOOTERS_START + getFooterCount())
//            return new StaticViewHolder(mFooterViews.get(viewType - FOOTERS_START));
            return new StaticViewHolder(mFooterViewProxy.getProxyView());
        else {
            return mWrappedAdapter.onCreateViewHolder(viewGroup, viewType - getAdapterTypeOffset());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int hCount = getHeaderCount();
        if (position >= hCount && position < hCount + mWrappedAdapter.getItemCount())
            mWrappedAdapter.onBindViewHolder(viewHolder, position - hCount);
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getFooterCount() + getWrappedItemCount();
    }

    public int getWrappedItemCount() {
        return mWrappedAdapter.getItemCount();
    }

    public int getHeaderCount() {
        if(null!=mHeadViewProxy&&isShowHeaderView) return 1;
        return 0;
    }
    public int getFooterCount() {
        if(null!=mFooterViewProxy&&isShowFooterView)return  1;
        return 0;
    }
    public void addHeadViewProxy(EmptyViewProxy headViewProxy){
        this.mHeadViewProxy=headViewProxy;
        notifyDataSetChanged();
    }
    public void addFooterViewProxy(EmptyViewProxy footerViewProxy){
        this.mFooterViewProxy=footerViewProxy;
        notifyDataSetChanged();
    }

    public EmptyViewProxy getHeadViewProxy() {
        return mHeadViewProxy;
    }

    public EmptyViewProxy getFooterViewProxy() {
        return mFooterViewProxy;
    }

    public HeaderViewProxyRecyclerAdapter setIsShowFooterView(boolean isShowFooterView) {
        this.isShowFooterView = isShowFooterView;
        return this;
    }

    public HeaderViewProxyRecyclerAdapter setIsShowHeaderView(boolean isShowHeaderView) {
        this.isShowHeaderView = isShowHeaderView;
        return this;
    }

    public void showHeaderView(){
        isShowFooterView=true;
        notifyDataSetChanged();
    }
    public void closeHeaderView(){
        isShowFooterView=false;
        notifyDataSetChanged();
    }
    public void showFooterView(){
        isShowFooterView=true;
        notifyDataSetChanged();
    }
    public void closeFooterView(){
        isShowFooterView=false;
        notifyDataSetChanged();
    }

    private void setWrappedAdapter(RecyclerView.Adapter adapter) {
        if (mWrappedAdapter != null) mWrappedAdapter.unregisterAdapterDataObserver(mDataObserver);
        mWrappedAdapter = adapter;
        Class adapterClass = mWrappedAdapter.getClass();
        if (!mItemTypesOffset.containsKey(adapterClass)) putAdapterTypeOffset(adapterClass);
        mWrappedAdapter.registerAdapterDataObserver(mDataObserver);
    }

    public RecyclerView.Adapter getWrappedAdapter() {
        return mWrappedAdapter;
    }


    private void putAdapterTypeOffset(Class adapterClass) {
        mItemTypesOffset.put(adapterClass, ITEMS_START + mItemTypesOffset.size() * ADAPTER_MAX_TYPES);
    }


    private int getAdapterTypeOffset() {
        return mItemTypesOffset.get(mWrappedAdapter.getClass());
    }


    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart + getHeaderCount(), itemCount);
        }


        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart + getHeaderCount(), itemCount);
        }


        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart + getHeaderCount(), itemCount);
        }


        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            int hCount = getHeaderCount();
            // TODO: No notifyItemRangeMoved method?
            notifyItemRangeChanged(fromPosition + hCount, toPosition + hCount + itemCount);
        }
    };

    private static class StaticViewHolder extends RecyclerView.ViewHolder {
        public StaticViewHolder(View itemView) {
            super(itemView);
        }
    }
}
