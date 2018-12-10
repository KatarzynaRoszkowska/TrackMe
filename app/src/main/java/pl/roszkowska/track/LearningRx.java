package pl.roszkowska.track;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class LearningRx extends AppCompatActivity {

    private final String TAG = "RXLearning";
    private CompositeDisposable mDisposable = new CompositeDisposable();

    private Subject<Integer> mSubjectIds = PublishSubject.create();
    private Subject<Integer> mBehaviourSubjectIds = BehaviorSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_rx);

        mDisposable.add(rxJavaIterationWithThread());
//        rxPrzykladLaczenie2RoznychStreamow();
//        rxPrzykladZminayWatkow();
//        rxPrzykladWatkow2();

//        koncepcjaSubjects();
//        koncepcjaBehaviourSubjects();
        flatMapExample();

    }

    private void flatMapExample() {
        mDisposable.add(Observable
                .just(1, 2, 4, 6, 8, 9)
                .flatMap(integer -> Observable.just(2 * integer))
                .subscribe(i -> Log.d(TAG, "Wynik: " + i))
        );
    }

    private void koncepcjaSubjects() {
        mSubjectIds.onNext(1);
        mSubjectIds.onNext(132);
        mSubjectIds.onNext(111234);
        mDisposable.add(mSubjectIds.subscribe(i -> Log.d(TAG, "Wynik1: " + i)));
        mSubjectIds.onNext(12);
        mDisposable.add(mSubjectIds.subscribe(i -> Log.d(TAG, "Wynik2: " + i)));
        mSubjectIds.onNext(123);
    }

    private void koncepcjaBehaviourSubjects() {
        mBehaviourSubjectIds.onNext(1);
        mBehaviourSubjectIds.onNext(132);
        mBehaviourSubjectIds.onNext(1234);
        mDisposable.add(mBehaviourSubjectIds.subscribe(i -> Log.d(TAG, "Wynik1: " + i)));
        mBehaviourSubjectIds.onNext(12);
        mDisposable.add(mBehaviourSubjectIds.subscribe(i -> Log.d(TAG, "Wynik2: " + i)));
        mBehaviourSubjectIds.onNext(123);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private void przykladMethodReference() {
        test(new Hey() {
            @Override
            public void test(int x, int y, String hey) {

            }
        });

        test(this::hey);
    }

    interface Hey {
        void test(int x, int y, String hey);
    }

    private void test(Hey hey) {
        hey.test(12, 12, "sss");
    }

    private void hey(int x, int y, String hey) {
        System.out.println("Hey" + x + "" + y + hey);
    }

    private Disposable rxPrzykladWatkow2() {
        return Observable
                .just(1, 2, 3, 4, 5)
                .map(integer -> {
                    Log.d(TAG, "Thread1: " + Thread.currentThread().getName());
                    return integer * 2;
                })
                .subscribeOn(Schedulers.io())
                .mergeWith(Observable.just(6, 7, 8, 9, 10, 11))
                .map(integer -> {
                    Log.d(TAG, "Thread2: " + Thread.currentThread().getName());
                    return integer * 2;
                })
                .observeOn(Schedulers.io())
                .subscribe(integer -> Log.d(TAG, "Wynik: " + integer + " T: " + Thread.currentThread().getName()));
        // subskrypcja zawsze dziala na watku na ktorym jestesmy obecnie.
        // to samo sie tyczy domyslnych Schedulers, jesli jestemy na main Thread to
        // observeOn i SubscribeOn sa Thread Main
    }

    private Disposable rxPrzykladZminayWatkow() {
        return Observable
                .just(1, 2, 45, 2, 345, 234, 234, 6)
                .map(integer -> {
                    Log.d(TAG, "Thread: " + Thread.currentThread().getName());
                    return integer * 234;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // observer on to jest wynik koncowy tam gdzie subskrybujemy
                .subscribe(integer -> Log.d(TAG, "Wynik: " + integer + " T: " + Thread.currentThread().getName()));
    }

    private Disposable rxPrzykladLaczenie2Streamow() {
        return Observable.concat(
                Observable
                        .just(1, 6, 8),
                Observable
                        .just(2, 9, 0)
        ).subscribe(integer -> Log.d(TAG, "Mamy cos: " + integer));
    }

    private Disposable rxPrzykladLaczenie2RoznychStreamow() {
        return Observable.zip(
                Observable.just(1, 6, 8),
                Observable.just("a", "b", "c"),
                (i, s) -> "" + i + ":" + s
        ).subscribe(integer -> Log.d(TAG, "Mamy cos: " + integer));
    }

    private Disposable rxPrzykladJakKonwertowacIntDoString() {
        return Observable
                .just(1, 2, 3)
//                .map(new Function<Integer, String>() { // anonymous class which has 1 method
//                    @Override
//                    public String apply(Integer integer) throws Exception {
//                        return String.valueOf(integer);
//                    }
//                })
//                .map(integer -> String.valueOf(integer)) // lambda
                .map(String::valueOf) // method reference
                .subscribe(
                        val -> Log.d("RXLearning", val)
                );
    }


    private Disposable rxJavaIterationWithThread() {
        return Observable
                .just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        i -> System.out.println("Liczba: " + i + " Thread: " + Thread.currentThread().getName()),
                        throwable -> System.out.println("Error: " + throwable.toString()),
                        () -> System.out.println("Finished"));
    }

    private Disposable rxJavaIteration() {
        return Observable
                .just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .subscribe(
                        i -> System.out.println("Liczba: " + i + " Thread: " + Thread.currentThread().getName()),
                        throwable -> System.out.println("Error: " + throwable.toString()),
                        () -> System.out.println("Finished"));
    }

    private void standardJavaIteration() {
        for (int i = 1; i < 10; i++) {
            try {
                System.out.println("Liczba: " + i + " Thread: " + Thread.currentThread().getName());
            } catch (Throwable throwable) {
                System.out.println("Error: " + throwable.toString());
            }
        }
        System.out.println("Finished");
    }
}
