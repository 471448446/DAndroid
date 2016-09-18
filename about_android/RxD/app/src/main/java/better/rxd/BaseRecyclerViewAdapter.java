package better.rxd;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Des  RecyclerView Adapter 基类 子类需要重写 onBindViewHolder onCreateViewHolder<br>
 * thk http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0804/3259.html
 * 动画 http://www.tqcto.com/article/mobile/138414.html
 * Created by Better on 2016/4/15 10:26.
 */
public abstract class BaseRecyclerViewAdapter<E, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    protected List<E> mList = new ArrayList<>();
    protected Activity mContext;
    /**
     * 一般list
     * @param context
     */
    public BaseRecyclerViewAdapter(Activity context) {
        this.mContext = context;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onViewDetachedFromWindow(T holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }
    /**
     * 下拉数据，最新数据，会重置列表
     *
     * @param list
     */
    public void addDownData(List<E> list) {
        if (null == list || list.isEmpty()) return;
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 上拉数据，加载更多
     *
     * @param list
     */
    public void addPullData(List<E> list) {
        if (null == list || list.isEmpty()) return;
        this.mList.addAll(list);
        notifyDataSetChanged();
    }
    public void reSetList(List<E> list){
        this.mList=list;
        notifyDataSetChanged();
    }

    public List<E> getList(){
        return this.mList;
    }
    protected E getItem(int position) {
        return mList.get(position);
    }
}
