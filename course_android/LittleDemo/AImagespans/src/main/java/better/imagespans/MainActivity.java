package better.imagespans;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String left = "文理学院";
    String img = "图";
    String right = "春熙路";
    String leftLong = "文理学院院到当前为当前无无群无群多多群无多群无群";
    String rightLong = "春熙路院到当前为当前无无群无群多多群无多群无群";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        normal();
        vertical();
        top();
    }

    private void top() {
        TextView normal = (TextView) findViewById(R.id.span_top);
        TextView normal2 = (TextView) findViewById(R.id.span_top2);

        SpannableStringBuilder stringBuilder = getStrLong();
        stringBuilder.setSpan(new ImageSpanPlus(this, R.drawable.goback, ImageSpanPlus.ALIGN_TOP_ASCENT), leftLong.length(), stringBuilder.length() - rightLong.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder stringBuilder2 = getStr();
        stringBuilder2.setSpan(new ImageSpanPlus(this, R.drawable.go, ImageSpanPlus.ALIGN_TOP_ASCENT), left.length(), stringBuilder2.length() - right.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        normal.setText(stringBuilder);
        normal2.setText(stringBuilder2);
    }

    private void vertical() {
        TextView normal = (TextView) findViewById(R.id.span_vertical);
        TextView normal2 = (TextView) findViewById(R.id.span_vertical2);
        TextView normal3 = (TextView) findViewById(R.id.span_vertical3);

        SpannableStringBuilder stringBuilder = getStrLong();
        stringBuilder.setSpan(new ImageSpanPlus(this, R.drawable.goback, ImageSpanPlus.ALIGN_CENTER), leftLong.length(), stringBuilder.length() - rightLong.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder stringBuilder2 = getStr();
        stringBuilder2.setSpan(new ImageSpanPlus(this, R.drawable.go, ImageSpanPlus.ALIGN_CENTER), left.length(), stringBuilder2.length() - right.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder stringBuilder3 = getStrLong();
        stringBuilder3.setSpan(new ImageSpanPlus(this, R.mipmap.ic_launcher, ImageSpanPlus.ALIGN_CENTER), leftLong.length(), stringBuilder3.length() - rightLong.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        normal.setText(stringBuilder);
        normal2.setText(stringBuilder2);
        normal3.setText(stringBuilder3);
    }

    private void normal() {
        TextView normal = (TextView) findViewById(R.id.span_normal);
        TextView normal2 = (TextView) findViewById(R.id.span_normal2);

        SpannableStringBuilder stringBuilder = getStr();
        stringBuilder.setSpan(new ImageSpan(this, R.drawable.goback, ImageSpan.ALIGN_BOTTOM), left.length(), stringBuilder.length() - right.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder stringBuilder2 = getStr();
        stringBuilder2.setSpan(new ImageSpan(this, R.drawable.go, ImageSpan.ALIGN_BOTTOM), left.length(), stringBuilder.length() - right.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        normal.setText(stringBuilder);
        normal2.setText(stringBuilder2);
    }

    private SpannableStringBuilder getStr() {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(left);
        stringBuilder.append(img);
        stringBuilder.append(right);
        return stringBuilder;
    }

    private SpannableStringBuilder getStrLong() {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(leftLong);
        stringBuilder.append(img);
        stringBuilder.append(rightLong);
        return stringBuilder;
    }
}
