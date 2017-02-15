package better.http;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by better on 2017/2/7.
 */

public abstract class CustomCallBack implements Callback {
    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }
}
