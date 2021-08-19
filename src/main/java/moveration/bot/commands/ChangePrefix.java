package moveration.bot.commands;

import lombok.val;
import moveration.bot.data.DataManager;
import moveration.bot.util.Assert;
import moveration.bot.util.Constants;
import moveration.bot.util.StringUtil;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.regex.Pattern;

public class ChangePrefix implements Command {

	@Override
	public boolean matches(GenericEvent event) {
		if (!(event instanceof GuildMessageReceivedEvent e)) return false;
		return StringUtil.stripPrefix(e.getMessage().getContentRaw(), DataManager.getGuild(e.getGuild().getIdLong())).matches("changeprefix .+");
	}

	@Override
	public void handle(GenericEvent event) {
		final GuildMessageReceivedEvent e = ((GuildMessageReceivedEvent) event);
		val pattern = Pattern.compile("changeprefix (.+)");
		val matcher = pattern.matcher(e.getMessage().getContentRaw());
		matcher.find();
		val newPrefix = matcher.group(1);
		Assert.allNotNull(newPrefix);
		e.getMessage().getChannel().sendMessage(String.format("Changing prefix from %s to %s", Constants.BOT_PREFIX, newPrefix)).queue();
		Constants.BOT_PREFIX = newPrefix;
	}
}