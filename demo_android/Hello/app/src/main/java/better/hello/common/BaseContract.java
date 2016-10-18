package better.hello.common;

/**
 * Des 定义P的数据类型,项目中所有的mvp都按照这种契约类来管理，可以方便看出有哪些功能。
 * Create By better on 2016/10/14 13:59.
 */
public interface BaseContract<T> {

    public interface BasePresenter<T> {

    }

    public interface BaseView {

    }
}
