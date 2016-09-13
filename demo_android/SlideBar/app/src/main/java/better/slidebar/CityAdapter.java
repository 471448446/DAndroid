package better.slidebar;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by better on 16/9/10.
 */
public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int head = 0;
    final int city = head + 1;
    List<CityBean> list;

    public CityAdapter(List<CityBean> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (head == viewType) {
            return new HeadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide_head, parent, false));
        } else {
            return new CityHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide_city, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadHolder){
            HeadHolder headHolder= (HeadHolder) holder;
            headHolder.txt.setText(list.get(position).getHead());
        }else {
            CityHolder cityHolder= (CityHolder) holder;
            cityHolder.txt.setText(list.get(position).getCity());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TextUtils.isEmpty(list.get(position).getHead()) ? city : head;
    }

    public class HeadHolder extends RecyclerView.ViewHolder {
        TextView txt;
        public HeadHolder(View itemView) {
            super(itemView);
            txt= (TextView) itemView.findViewById(R.id.cityName);
        }
    }

    public class CityHolder extends RecyclerView.ViewHolder {
        TextView txt;
        public CityHolder(View itemView) {
            super(itemView);
            txt= (TextView) itemView.findViewById(R.id.cityName);
        }

    }
}
