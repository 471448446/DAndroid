package better.library.base.ui;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import better.library.utils.Utils;

public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter<SimpleViewHolder> {

    private List<T> list = new ArrayList<>();

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

    public T getItem(int p) {
        return list.get(p);
    }

    protected abstract int getItemView();

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemView(), parent, false));
    }

    @Override
    public int getItemCount() {
        Utils.log(")))"+list.size());
        return list.size();
    }

}
