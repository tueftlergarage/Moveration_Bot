package moveration.bot.commands;

import lombok.val;
import moveration.bot.Constants;
import moveration.bot.util.Assert;
import moveration.bot.util.StringUtil;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.regex.Pattern;

public class ChangePrefix implements Command {

	@Override
	public boolean matches(GenericEvent event) {
		if (!(event instanceof GuildMessageReceivedEvent)) return false;
		return StringUtil.stripPrefix(((GuildMessageReceivedEvent) event).getMessage().getContentRaw()).matches("changeprefix .+");
	}

	@Override
	public void handle(GenericEvent event) {
		final GuildMessageReceivedEvent e = ((GuildMessageReceivedEvent) event);
		val pattern = Pattern.compile("changeprefix (.+)");
		val matcher = pattern.matcher(e.getMessage().getContentRaw());
		val newPrefix = matcher.group(1);
		Assert.allNotNull(newPrefix);
		e.getMessage().getChannel().sendMessage(String.format("Changing prefix from %s to %s", Constants.botprefix, newPrefix)).queue();
		Constants.botprefix = newPrefix;
	}
}
