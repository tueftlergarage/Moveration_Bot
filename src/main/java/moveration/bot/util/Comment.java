package moveration.bot.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Comment {

	public <T> T debug(ReturningRunnable<T> runnable, String msg) {
		System.out.println(msg);
		return runnable.run();
	}

	public <T> T debugf(ReturningRunnable<T> runnable, String msg, Object... objects) {
		System.out.printf(msg, objects);
		return runnable.run();
	}

	public <T> T warn(ReturningRunnable<T> runnable, String msg) {
		System.out.println(msg);
		return runnable.run();
	}

	public <T> T warnf(ReturningRunnable<T> runnable, String msg, Object... objects) {
		System.out.printf(msg, objects);
		return runnable.run();
	}
}
