package better.hello.common.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * onCreateView  设置no title  需要自定义大小</br>重写onCreateView()
 * @author Better
 */
public class BaseDialogFragmentView extends DialogFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isNoTitle()){//自定义布局
			setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
		}
	}
	@Override
	public void onStart() {
		super.onStart();
		int[] wh=getWH();
		if(null!=wh&&isNoTitle()){
			getDialog().getWindow().setLayout(wh[0],wh[1]);
			getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); 
		}
	}
	protected int[] getWH() {
		return null;
	}
	protected boolean isNoTitle() {
		return false;
	}
	@Override
	public void dismiss() {
		if(null==getActivity()){
			return;
		}
		super.dismiss();
	}
	/**
	 * 显示
	 */
	public void showDialog(FragmentManager manager){
		// DialogFragment.show() will take care of adding the fragment
	    // in a transaction.  We also want to remove any currently showing
	    // dialog, so make our own transaction and take care of that here.
//	    FragmentTransaction ft = manager.beginTransaction();
//	    Fragment prev = manager.findFragmentByTag("dialog");
//	    if (prev != null) {
//	        ft.remove(prev);
//	    }
//	    ft.addToBackStack(null);
//
//	    this.show(ft, "dialog");
		FragmentTransaction ft = manager.beginTransaction();
//		Fragment prev = manager.findFragmentByTag("dialog");
//		if (prev != null) {
//			ft.remove(prev);
//		}
//		ft.addToBackStack(null);
//
//		this.show(ft, "dialog");
		ft.add(BaseDialogFragmentView.this,"dialog");
		ft.commitAllowingStateLoss();
	}
}
