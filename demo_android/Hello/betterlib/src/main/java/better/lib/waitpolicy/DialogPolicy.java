package better.lib.waitpolicy;

import android.content.Context;

import better.lib.utils.BaseUtils;
import better.lib.waitpolicy.dialog.WaitDialog;

/**
 * Created by Better on 2016/3/14.
 * 默认提示失败信息，成功
 */
public class DialogPolicy extends WaitPolicy {
    public DialogPolicy(Context context){
        this.mContext=context;
        this.isToastSuccess=true;
        this.isToastError=true;
    }
    public DialogPolicy(Context context,boolean toastSuccess){
        this.mContext=context;
        this.isToastError=true;
        this.isToastSuccess=toastSuccess;
    }
    public DialogPolicy(Context context,boolean toastError,boolean toasSuccess){
        this.mContext=context;
        this.isToastError=toastError;
        this.isToastSuccess=toasSuccess;
    }
    @Override
    public void displayLoading(String message) {
        WaitDialog.getInstance(mContext).showWaitDialog(message);
    }

    @Override
    public void displayLoading() {
        WaitDialog.getInstance(mContext).showWaitDialog();
    }

    @Override
    public void displayRetry(String message) {
        disappear();
        if(isToastError&&null!=mContext) BaseUtils.toastShort(mContext,message);
    }

    @Override
    public void displayRetry() {
    }

    @Override
    public void disappear() {
        WaitDialog.getInstance(mContext).dismissWaitDialog();
    }
    @Override
    public void disappear(String msg) {
        disappear();
        if(isToastSuccess&&null!=mContext)BaseUtils.toastShort(mContext, msg);
    }
}
