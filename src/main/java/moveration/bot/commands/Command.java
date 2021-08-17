package moveration.bot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;

public interface Command {

    boolean matches(GenericEvent event);
    void handle(GenericEvent event);

}
