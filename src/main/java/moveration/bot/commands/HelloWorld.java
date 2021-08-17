package moveration.bot.commands;

import lombok.extern.java.Log;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@Log
public class HelloWorld implements Command {

	@Override
	public boolean matches(GenericEvent event) {
		return
				event instanceof GuildMessageReceivedEvent &&
				((GuildMessageReceivedEvent) event).getMessage().getContentRaw().equalsIgnoreCase("hi");
	}

	@Override
	public void handle(GenericEvent event) {
		assert event instanceof GuildMessageReceivedEvent;
		final GuildMessageReceivedEvent e = ((GuildMessageReceivedEvent) event);
		final String name = e.getAuthor().getName();
		final String nick = e.getMessage().getMember().getNickname();
		e.getMessage().getChannel().sendMessage(String.format("Hello, %s", nick == null ? name : nick)).queue();
	}
}
