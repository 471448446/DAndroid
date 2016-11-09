package better.hello.common;

/**
 * Created by better on 2016/11/9.
 */

public interface LikeRx<T> {
    void onStart();
    void onNext(T o);
    void onComplete();
    void onError(String erroe);
}
