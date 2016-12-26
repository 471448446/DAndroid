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
public abstract class BaseDetailActivity extends BaseActivity{

    protected boolean isCollected;
    protected View mContainer;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!childrenInflateMenu(menu)){
            getMenuInflater().inflate(R.menu.menu_share, menu);
        }
        updateCollectionMenu(menu.findItem(R.id.menu_collect));
        return super.onCreateOptionsMenu(menu);
    }
    protected boolean childrenInflateMenu(Menu menu){
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_collect){
            if(isCollected){
                removeFromCollection();
                isCollected = false;
                updateCollectionMenu(item);
                if (null!=mContainer) Snackbar.make(mContainer, R.string.notify_remove_from_collection, Snackbar.LENGTH_SHORT).show();
            }else {
                addToCollection();
                isCollected = true;
                updateCollectionMenu(item);
              if (null!=mContainer) Snackbar.make(mContainer, R.string.notify_add_to_collection, Snackbar.LENGTH_SHORT).show();
            }
        }else if (item.getItemId() == R.id.menu_share){
            Utils.shareTxt(mContext,getShareTitle());
        }
        onMenuSelected(item);
        return true;
    }

    protected abstract String getShareTitle();

    /**
     * 子类 新增的Item
     * @param item 选中的menu
     */
    public void onMenuSelected(MenuItem item){

    }

    protected void updateCollectionMenu(MenuItem item){
        if (null==item) return;
        if(isCollected){
            item.setIcon(R.drawable.ic_star_collect_24dp);
        }else {
            item.setIcon(R.drawable.ic_star_normal_24dp);
        }
    }

    protected abstract void addToCollection();

    protected abstract void removeFromCollection();
}
