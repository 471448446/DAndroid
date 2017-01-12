package better.bbanner;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class OnBannerFragmentClickReceiver extends BroadcastReceiver {
	public static final String defaultAction="com.example.bbanner.OnPageClickReceiver";
	public static final String BANNER_POSITION="position";
	private String action;
	public IntentFilter getDefaultInterFilter(){
		action=defaultAction;
		IntentFilter filter= new IntentFilter();
		filter.addAction(action);
		return filter;
	}
	public IntentFilter getInterFilter(String paction){
		action=paction;
		IntentFilter filter= new IntentFilter();
		filter.addAction(action);
		return filter;
	}
	public int getP(Intent intent){
		if(intent!=null){
			return intent.getIntExtra(BANNER_POSITION, 0);
		}
		return 0;
	}
}
