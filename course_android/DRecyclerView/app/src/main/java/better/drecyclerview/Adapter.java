package better.drecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by better on 2017/1/18.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> implements RVDragMoveHelper.DragDataListener {
    private List<String> ls;

    public Adapter(List<String> ls) {
        this.ls = ls;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txt, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.txt.setText("" + position);

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override
    public List<?> getData() {
        return ls;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView txt;

        public Holder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.txt);
        }
    }
}
