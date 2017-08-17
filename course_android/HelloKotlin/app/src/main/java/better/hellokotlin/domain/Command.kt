package better.hellokotlin.domain

/**
 * Created by better on 2017/7/29 14:21.
 */
public interface Command<T> {
    fun execute(): T
}