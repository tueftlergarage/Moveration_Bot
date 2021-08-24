package moveration.bot.commands;

import lombok.val;
import moveration.bot.data.DataManager;
import moveration.bot.data.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

public class DBCreator implements Command {

	@Override
	public boolean matches(GenericEvent event) {
		return event instanceof GuildJoinEvent;
	}

	@Override
	public void handle(GenericEvent event) {
		val e = ((GuildJoinEvent) event);
		DataManager.createNewGuild(e.getGuild().getIdLong());
	}
}
