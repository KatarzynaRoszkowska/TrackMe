package pl.roszkowska.track.module;

import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.common.RxEventDispatcher;

public class EventDispatcherModule {
    private static EventDispatcherModule sInstance;

    private final EventDispatcher mEventDispatcher;

    private EventDispatcherModule(EventDispatcher eventDispatcher) {
        mEventDispatcher = eventDispatcher;
    }

    public static void setup() {
        if (sInstance != null) {
            throw new IllegalStateException("Setup wolamy tylko raz!");
        }
        sInstance = new EventDispatcherModule(new RxEventDispatcher());
    }

    public static EventDispatcherModule getModule() {
        return sInstance;
    }

    public EventDispatcher getDispatcher() {
        return sInstance.mEventDispatcher;
    }
}
