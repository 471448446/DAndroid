package com.better.learn.jetpack.datastore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DataStoreJavaImpl {
    @SuppressLint("StaticFieldLeak")
    private static DataStoreJavaImpl instance;

    Context context;
    RxDataStore<Preferences> dataStore;

    Preferences.Key<Integer> EXAMPLE_COUNTER = PreferencesKeys.intKey("example_counter");

    private DataStoreJavaImpl(Context context) {
        this.context = context;
        dataStore = new RxPreferenceDataStoreBuilder(context, /*name=*/ "settings_java").build();
    }

    public static DataStoreJavaImpl getInstance(Context context) {
        if (null == instance) {
            synchronized (DataStoreJavaImpl.class) {
                if (null == instance) {
                    instance = new DataStoreJavaImpl(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void setTest() {
        // 将kotlin中flow转换为Rx中的Flowable：https://github.com/Kotlin/kotlinx.coroutines/blob/master/reactive/kotlinx-coroutines-rx2/src/RxConvert.kt
        // 秒
        Flowable<Integer> exampleCounterFlow =
                dataStore.data().map(prefs -> prefs.get(EXAMPLE_COUNTER));
        exampleCounterFlow
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i("Better", "example counter set:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("Better", "example counter set:" + t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getTest() {
        Single<Preferences> updateResult = dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            Integer currentInt = prefsIn.get(EXAMPLE_COUNTER);
            mutablePreferences.set(EXAMPLE_COUNTER, currentInt != null ? currentInt + 1 : 1);
            return Single.just(mutablePreferences);
        });
        // The update is completed once updateResult is completed.
        updateResult
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Preferences>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Preferences preferences) {
                        Log.i("Better", "example counter set:" + preferences);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i("Better", "example counter set:" + e);
                    }
                });
    }
}
