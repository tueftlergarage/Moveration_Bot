package moveration.bot;

import lombok.NonNull;
import lombok.extern.java.Log;
import moveration.bot.commands.Command;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.LinkedList;

@Log
public class EventManager implements EventListener {

    private final LinkedList<Command> commands = new LinkedList<>();

    public void registerCommand(Command c) {
        commands.add(c);
    }

    @Override
    public void onEvent(@NonNull GenericEvent event) {
        commands
                .stream()
                .filter(command -> command.matches(event))
                .peek(command -> log.info(command.toString()))
                .findFirst()
                .ifPresent(command -> command.handle(event));
    }
}
