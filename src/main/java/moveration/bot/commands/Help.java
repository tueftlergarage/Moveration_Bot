package moveration.bot.commands;

import lombok.val;
import moveration.bot.data.DataManager;
import moveration.bot.util.Assert;
import moveration.bot.util.Constants;
import moveration.bot.util.StringUtil;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.regex.Pattern;

public class Help implements Command {		//listens for guildmessagerecievedevent and checks if message matches with help command

	@Override
	public boolean matches(GenericEvent event) {
		if (!(event instanceof GuildMessageReceivedEvent e)) return false;
		return StringUtil.stripPrefix(e.getMessage().getContentRaw(), DataManager.getGuild(e.getGuild().getIdLong())).matches("help ?(.*)");
	}

	@Override
	public void handle(GenericEvent event) {
		assert event instanceof GuildMessageReceivedEvent;
		final GuildMessageReceivedEvent e = ((GuildMessageReceivedEvent) event);
		val pattern = Pattern.compile("help ?(.*)");
		val matcher = pattern.matcher(e.getMessage().getContentRaw());
		matcher.find();
		val command = matcher.group(1);
		Assert.allNotEmpty(command);
		if (command.isEmpty()) {
			final String name = e.getAuthor().getName();
			final String nick = e.getMessage().getMember().getNickname();
			e.getMessage().getChannel().sendMessage(String.format("Do you need help? I can help you, %s. Use %s + help + [command] and I show you what it does.", nick == null ? name : nick, Constants.BOT_PREFIX)).queue();//shows the user how help command works
		} else {
			if (command.equals("changeprefix")) {
				e.getMessage().getChannel().sendMessage(String.format("This command changes the prefix from %s to [newprefix]", Constants.BOT_PREFIX)).queue(); //shows how to use changeprefix command
			} else if (command.equals("help")) {
				e.getMessage().getChannel().sendMessage(String.format("This command shows you how to interact with Moverationbot")).queue();//shows how to interact with the bot
			}//TODO add new commands to this class
		}
	}
}