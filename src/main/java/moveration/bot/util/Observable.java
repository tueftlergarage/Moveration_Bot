package moveration.bot.util;

public interface Observable<T> {

	void registerObserver(Observer<T> observer);

	void deregisterObserver(Observer<T> observer);

	void notifyObservers(T t);

}
