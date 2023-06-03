package better.library.base.ui;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by better on 2023/6/3 19:31.
 */
public abstract class SimpleAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final List<T> list = new ArrayList<>();

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<T> data) {
        if (null == data) return;
        this.list.addAll(data);
        notifyDataSetChanged();
    }

    public void reSetData(List<T> data) {
        if (null == data) return;
        this.list.clear();
        addData(data);
    }
    public T getItem(int position){
        return list.get(position);
    }

}
