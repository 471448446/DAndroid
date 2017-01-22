package better.hello.ui.news.channle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import better.hello.R;
import better.hello.common.BaseSchedulerTransformer;
import better.hello.common.RVDragMoveHelper;
import better.hello.data.bean.NewsChannelBean;
import better.hello.http.wait.ProgressWait;
import better.hello.ui.base.BaseActivity;
import better.hello.util.C;
import better.lib.waitpolicy.WaitPolicy;
import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class ChannelActivity extends BaseActivity implements ChannelContract.view {
    @BindView(R.id.channel_topPanel)
    RecyclerView rvTop;
    @BindView(R.id.channel_bottomPanel)
    RecyclerView rvBottom;
    @BindView(R.id.activity_channel)
    LinearLayout mRelativeLayout;
    @BindView(R.id.toolBar)
    Toolbar mToolbar;
    private ProgressWait mProgressWait;
    private ChannelPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
    }

    @Override
    protected void initData() {
        super.initData();
        setBackToolBars();
        mPresenter = new ChannelPresenter(this);
        setPresenterProxy(mPresenter);
        GridLayoutManager managerTop = new GridLayoutManager(mContext, 4);
        GridLayoutManager managerBottom = new GridLayoutManager(mContext, 4);
        rvBottom.setLayoutManager(managerBottom);
        rvTop.setLayoutManager(managerTop);
        RVDragMoveHelper.get().attachToRecyclerView(rvTop);
        mPresenter.showTopChannel();
        mPresenter.showBottomChannel();
    }

    private void setBackToolBars() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTopItem(0);
            }
        });
        mToolbar.setNavigationIcon(R.drawable.icon_navgation_back_white);
        getSupportActionBar().setTitle("编辑栏目");
    }

    @Override
    public void deliverBackData(Object... objects) {
        super.deliverBackData();
        Intent intent = new Intent();
        if (null != objects) {
            intent.putExtra(C.EXTRA_FIRST, (int) objects[0]);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showTopChannel(List<NewsChannelBean> list) {
        rvTop.setAdapter(new ChannelAdapter(list, mContext, true));
    }

    @Override
    public void showBottomChannel(List<NewsChannelBean> list) {
        rvBottom.setAdapter(new ChannelAdapter(list, mContext, false));
    }

    @Override
    public void onClickBottomItem(NewsChannelBean bean, int p) {
        bean.setSelect(NewsChannelBean.SELECTED);
        ((ChannelAdapter) rvTop.getAdapter()).addDataTail(bean);
        ((ChannelAdapter) rvBottom.getAdapter()).remove(p);
    }

    @Override
    public void onDeleteTopItem(NewsChannelBean bean, int p) {
        bean.setSelect(NewsChannelBean.UN_SELECT);
        ((ChannelAdapter) rvBottom.getAdapter()).addDataTail(bean);
        ((ChannelAdapter) rvTop.getAdapter()).remove(p);
    }

    @Override
    public void onClickTopItem(final int p) {
        if (!((ChannelAdapter) rvTop.getAdapter()).isChanged && !((ChannelAdapter) rvTop.getAdapter()).isChanged) {
            deliverBackData(p);
            return;
        }
        getWait().displayLoading();
        Observable.just("").doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
                mPresenter.preSave();
                mPresenter.save(((ChannelAdapter) rvTop.getAdapter()).getList());
                mPresenter.save(((ChannelAdapter) rvBottom.getAdapter()).getList());
            }
        }).compose(new BaseSchedulerTransformer<String>()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                deliverBackData(p);
            }

            @Override
            public void onError(Throwable e) {
                toast(e.getMessage());
                deliverBackData(p);
            }

            @Override
            public void onNext(String s) {
            }
        });
    }

    @Override
    public WaitPolicy getWait() {
        synchronized (ChannelActivity.class) {
            if (null == mProgressWait) {
                mProgressWait = new ProgressWait(mContext);
                mRelativeLayout.addView(mProgressWait.getView(), 1,
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }
        }
        return mProgressWait;
    }
}
