package better.hello.http.wait;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import better.hello.R;
import better.hello.util.Utils;
import better.lib.waitpolicy.WaitPolicy;

/**
 * Created by better on 2017/1/12.
 */

public class ProgressWait extends WaitPolicy {
    private View root;

    public ProgressWait(Context context) {
        root = LayoutInflater.from(context).inflate(R.layout.wait_progress, null, false);
    }

    public View getView() {
        return root;
    }

    @Override
    public void displayLoading(String message) {
        Utils.setVisible(root);
    }

    @Override
    public void displayLoading() {
        displayLoading("");
    }

    @Override
    public void displayRetry(String message) {
        Utils.toastShort(root.getContext(), message);
    }

    @Override
    public void displayRetry() {
        displayRetry("");
    }

    @Override
    public void disappear(String msg) {
        Utils.setGone(root);
    }

    @Override
    public void disappear() {
        disappear("");
    }

    @Override
    public void onNext(Object bean) {

    }
}
