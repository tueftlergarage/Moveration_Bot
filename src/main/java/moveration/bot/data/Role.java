package moveration.bot.data;

import lombok.Data;
import moveration.bot.util.Observable;
import moveration.bot.util.Observer;

import java.util.HashSet;
import java.util.Set;

/**
 * Class to store information about a specific role
 */
@Data
public class Role implements Observable<Role> {

	private final String name;
	private final int power;

	private final Set<Observer<Role>> observers = new HashSet<>();

	/**
	 * Register a new observer
	 *
	 * @param observer The observer to register
	 * @author Jonas Mohr
	 */
	@Override
	public void registerObserver(Observer<Role> observer) {
		observers.add(observer);
	}

	@Override
	public void deregisterObserver(Observer<Role> observer) {
		observers.remove(observer);
	}


	@Override
	public void notifyObservers(Role role) {
		observers.forEach(o -> o.onNotification(role));
	}
}