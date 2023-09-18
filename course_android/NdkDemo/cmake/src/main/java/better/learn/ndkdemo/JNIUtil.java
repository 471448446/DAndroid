package better.learn.ndkdemo;

public class JNIUtil {
    static {
        System.loadLibrary("hellojni");
    }

    public native String sayHelloJNI();

    public native int sayHelloJInt();
}
