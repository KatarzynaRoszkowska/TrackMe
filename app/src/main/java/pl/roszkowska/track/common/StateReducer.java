package pl.roszkowska.track.common;

public interface StateReducer<E, S> {
    S reduce(S state, E event);
}
