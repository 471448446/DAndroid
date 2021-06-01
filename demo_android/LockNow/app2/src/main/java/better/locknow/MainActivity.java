package better.locknow;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * https://stackoverflow.com/questions/14282553/need-root-to-execute-shell-command-input-keyevent-through-an-app-at-runtim
 * https://stackoverflow.com/questions/50453289/executing-commands-inside-my-app
 */
public class MainActivity extends Activity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(16f);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        setContentView(textView, layoutParams);
        new Thread(new Runnable() {
            @Override
            public void run() {
                process();
            }
        }).start();
    }

    private void process() {
        Process process = null;
//        InputStream inputStream = null;
        try {
            //adb shell input keyevent 26
            process = Runtime.getRuntime().exec("input keyevent 26");
//            process = Runtime.getRuntime().exec(new String[]{"su", "-c", "input keyevent 26"});

//            inputStream = process.getErrorStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            final StringBuilder builder = new StringBuilder();
//            while ((line = bufferedReader.readLine()) != null) {
//                builder.append(line);
//            }
//            if (!TextUtils.isEmpty(builder)) {
//                textView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        textView.setText(builder.toString());
//                    }
//                });
//            }
            process.waitFor();
            int code = process.exitValue();
            if (0 == code) {
                finish();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            if (!isFinishing()) {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(e.getMessage());
                    }
                });
            }
        } finally {
            if (null != process) {
                process.destroy();
            }
//            if (null != inputStream) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}