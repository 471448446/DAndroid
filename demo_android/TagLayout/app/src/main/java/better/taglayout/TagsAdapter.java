package better.taglayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by better on 16/8/25.
 */
public class TagsAdapter extends BaseAdapter {
    private List<String> mListe;
    private Context mContext;

    public TagsAdapter(List<String> mListe, Context mContext) {
        this.mListe = mListe;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mListe.size();
    }

    @Override
    public Object getItem(int i) {
        return mListe.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_item_unsubreason, viewGroup, false);
        final TextView textView = (TextView) view.findViewById(R.id.dialog_unsub_reason_item_tv);
        textView.setText(mListe.get(i));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setSelected(true);
            }
        });
        return view;
    }
    public void setList(List<String> list){
        this.mListe=list;
        notifyDataSetChanged();
    }
}
