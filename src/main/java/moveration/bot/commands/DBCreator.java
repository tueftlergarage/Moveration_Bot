package moveration.bot.commands;

import lombok.val;
import moveration.bot.data.DataManager;
import moveration.bot.data.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

public class DBCreator implements Command {						//listens for guildjoinevent and starts database creation after every guildjoinevent

	@Override
	public boolean matches(GenericEvent event) {
		return event instanceof GuildJoinEvent;
	}

	@Override
	public void handle(GenericEvent event) {
		val e = ((GuildJoinEvent) event);
		DataManager.addGuild(new Guild(e.getGuild().getIdLong()));
	}
}
