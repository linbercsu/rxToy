package com.mx.rxjavatoy.UI;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mx.rxjavatoy.Disposable;
import com.mx.rxjavatoy.Function;
import com.mx.rxjavatoy.Observable;
import com.mx.rxjavatoy.Observer;
import com.mx.rxjavatoy.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        just();
        map();
    }

    private void just() {
        Observable<String> observable = Observable.just("hello");
        observable.subscribe(getObserver());
    }

    private void map() {
        Observable<String> observable = Observable.just(1)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return integer.toString();
                    }
                });

        observable.subscribe(getObserver());
    }

    @NonNull
    private Observer<String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e("test", "test: onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.e("test", "test: onNext " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("test", "test: onError");
            }

            @Override
            public void onComplete() {
                Log.e("test", "test: onComplete");
            }
        };
    }
}
