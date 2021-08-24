package moveration.bot.util;

@FunctionalInterface
public interface Observer<T> {

	void onNotification(T before, T after);

}
