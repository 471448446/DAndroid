package better.dbottomsheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

/**
 * Created by better on 16/8/31.
 */
public class BBottomSheetDialog extends BottomSheetDialog {
    View bottomSheetLay;
    BottomSheetBehavior<View> behavior;
    public BBottomSheetDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.diaog_collect_zhihu);
        bottomSheetLay =  findViewById(R.id.behavior);
        behavior = BottomSheetBehavior.from(bottomSheetLay);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
