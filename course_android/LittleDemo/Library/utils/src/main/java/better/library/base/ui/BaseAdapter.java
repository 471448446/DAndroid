package better.library.base.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    public Context context;
    private List<T> list = new ArrayList<>();

    public BaseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(getViewId(), parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initData(holder, position);
        return convertView;
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

    protected abstract void initData(ViewHolder holder, int position);

    protected abstract int getViewId();

    public class ViewHolder {
        public View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }
}
