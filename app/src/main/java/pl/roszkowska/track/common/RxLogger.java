package pl.roszkowska.track.common;

import android.util.Log;

import io.reactivex.Observable;

public class RxLogger {
    public static <T> Observable<T> debug(String tag, Observable<T> observable) {
        String frame = getStackFrame(2);
        String fullTag = getFrameTag(observable, frame) + " " + tag;
        return observable
                .doOnNext(n -> Log.i(tag, fullTag + " [N] " + n))
                .doOnError(e -> Log.i(tag, fullTag + " [E] " + e))
                .doOnComplete(() -> Log.i(tag, fullTag + " [C] "))
                .doOnSubscribe(disposable -> Log.i(tag, fullTag + " [S] "))
                .doOnDispose(() -> Log.i(tag, fullTag + " [D] "));
    }

    private static String getFrameTag(Object observable, String frame) {
        return observable.hashCode() + ":" + frame;
    }

    private static String getStackFrame(int pos) {
        StackTraceElement frame = new Exception().fillInStackTrace().getStackTrace()[pos + 1];
        String className = frame.getClassName();
        return className.substring(className.lastIndexOf('.') + 1) + "." + frame.getMethodName() + ":" + frame.getLineNumber();
    }
}
