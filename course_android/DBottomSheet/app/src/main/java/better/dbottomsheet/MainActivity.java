package better.dbottomsheet;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * http://www.materialdoc.com/bottom-sheets/
 * https://medium.com/@nullthemall/new-bottomsheet-caab21aff19b#.aeoqymslh
 * pp:layout_behavior="@string/bottom_sheet_behavior" 这个属性是作为Bottom Sheet必须的属性
 * app:behavior_hideable="false" 这个属性是当我们拖拽下拉的时候，bottom sheet是否能全部隐藏
 * app:behavior_peekHeight="0dp" 这个属性是当Bottom Sheets关闭的时候，底部下表我们能看到的高度
 * 另外这里必须用CoordinatorLayout才可以生效
 * <p>
 */
public class MainActivity extends AppCompatActivity {
    View bottomSheetLay;
    BottomSheetBehavior<View> behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomSheetLay = findViewById(R.id.sheetLay);
        behavior = BottomSheetBehavior.from(bottomSheetLay);

//        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        findViewById(R.id.txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        findViewById(R.id.fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BBottomSheetFragment fragment = new BBottomSheetFragment();
                getSupportFragmentManager().beginTransaction().add(fragment, "d").commitAllowingStateLoss();
            }
        });
        findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BBottomSheetDialog(MainActivity.this).show();
            }
        });
        findViewById(R.id.collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().add(new CollectDialog(), "d").commitAllowingStateLoss();
            }
        });
    }
}
