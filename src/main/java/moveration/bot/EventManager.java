package moveration.bot;

import lombok.NonNull;
import lombok.extern.java.Log;
import moveration.bot.commands.Command;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.LinkedList;

@Log
public class EventManager implements EventListener {

	private final LinkedList<Command> events = new LinkedList<>();

	public void registerCommand(Command c) {
		events.add(c);
	}

	@Override
	public void onEvent(@NonNull GenericEvent event) {
		events
				.stream()
				.filter(command -> command.matches(event))
				.findFirst()
				.ifPresentOrElse(command -> command.handle(event),
						() -> log.warning(String.format("Discarding event %s", event))
				);
	}
}
