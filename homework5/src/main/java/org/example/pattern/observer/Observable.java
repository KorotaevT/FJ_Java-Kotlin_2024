package org.example.pattern.observer;

public interface Observable<T> {

    void addObserver(Observer<T> observer);

    void removeObserver(Observer<T> observer);

    void notifyObservers(T model);

}