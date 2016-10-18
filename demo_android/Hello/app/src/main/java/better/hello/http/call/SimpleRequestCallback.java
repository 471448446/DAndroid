package better.hello.http.call;

import better.lib.waitpolicy.WaitPolicy;

/**
 * Created by better on 2016/10/17.
 * 子类子关心成功与否。
 */

public abstract class SimpleRequestCallback implements RequestCallback {
    @Override
    public void onStart(WaitPolicy waitPolicy) {

    }

    @Override
    public void onComplete(WaitPolicy waitPolicy) {

    }
}
