package moveration.bot.commands;

import moveration.bot.util.StringUtil;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Help implements Command {

	//public String prefix = "M!";
	@Override
	public boolean matches(GenericEvent event) {
		if (!(event instanceof GuildMessageReceivedEvent e)) return false;
		return StringUtil.stripPrefix(((GuildMessageReceivedEvent) event).getMessage().getContentRaw()).matches("help [0-9]+");
		//val msg = e.getMessage().getContentRaw();
		//if (!msg.contains(" ")) return false;
		//val parts = e.getMessage().getContentRaw().split("\\ +");
		//if (parts.length != 2) return false;
		//if (!IntUtil.isInteger(parts[1])) return false;
		//return parts[1].equalsIgnoreCase(Constants.botprefix + "help");
	}

	@Override
	public void handle(GenericEvent event) {
		assert event instanceof GuildMessageReceivedEvent;
		final GuildMessageReceivedEvent e = ((GuildMessageReceivedEvent) event);
		final String name = e.getAuthor().getName();
		final String nick = e.getMessage().getMember().getNickname();
		e.getMessage().getChannel().sendMessage(String.format("Do you need help? I can help you, %s", nick == null ? name : nick)).queue();
	}
}
