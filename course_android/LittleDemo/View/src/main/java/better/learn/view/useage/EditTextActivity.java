package better.learn.view.useage;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import better.learn.view.R;
import better.library.base.ui.BaseActivity;
import better.library.utils.Utils;

public class EditTextActivity extends BaseActivity {
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text_activity);
        mEditText = (EditText) findViewById(R.id.editText_search);
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Utils.log("--->" + event.getAction() + Utils.getEditTextString(mEditText));
                }
                return false;
            }
        });
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Utils.log("EditorAction->" + actionId + Utils.getEditTextString(mEditText));
                }
                return false;
            }
        });
    }
}
