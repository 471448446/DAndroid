package better.hello.ui.base;

import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import better.hello.R;
import better.hello.util.Utils;


/**
 * Created by Better on 2016/4/9.
 */
public abstract class BaseDetailActivity extends BaseActivity {

    protected boolean isCollected;
    protected View mContainer;
    protected Menu mMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (0 == childrenInflateMenuID()) {
            getMenuInflater().inflate(R.menu.menu_share, menu);
        } else {
            getMenuInflater().inflate(childrenInflateMenuID(), menu);
        }
        mMenu = menu;
        if (null != mMenu) updateCollectionMenu();
        onMenuCreated();
        return true;
    }

    protected void onMenuCreated() {
    }

    protected boolean childrenInflateMenu(Menu menu) {
        return false;
    }

    protected int childrenInflateMenuID() {
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_collect) {
            if (isCollected) {
                removeFromCollection();
                isCollected = false;
                updateCollectionMenu();
                if (null != mContainer)
                    Snackbar.make(mContainer, R.string.notify_remove_from_collection, Snackbar.LENGTH_SHORT).show();
            } else {
                addToCollection();
                isCollected = true;
                updateCollectionMenu();
                if (null != mContainer)
                    Snackbar.make(mContainer, R.string.notify_add_to_collection, Snackbar.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.menu_share) {
            Utils.shareTxt(mContext, getShareTitle());
        }
        onMenuSelected(item);
        return true;
    }

    protected abstract String getShareTitle();

    /**
     * 子类 新增的Item
     *
     * @param item 选中的menu
     */
    public void onMenuSelected(MenuItem item) {

    }

    protected void updateCollectionMenu() {
        MenuItem item = mMenu.findItem(R.id.menu_collect);
        if (null == item) return;
        if (isCollected) {
            item.setIcon(R.drawable.ic_star_collect_24dp);
        } else {
            item.setIcon(R.drawable.ic_star_normal_24dp);
        }
    }

    protected abstract void addToCollection();

    protected abstract void removeFromCollection();
}
