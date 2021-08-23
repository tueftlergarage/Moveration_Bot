package moveration.bot.commands;

import lombok.val;
import moveration.bot.data.DataManager;
import moveration.bot.util.Assert;
import moveration.bot.util.Emotes;
import moveration.bot.util.StringUtil;
import net.dv8tion.jda.api.entities.AbstractChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.*;
import java.util.regex.Pattern;

public class MoveTestCommand implements Command {

	private static final int DELAY = 180000;
	private final List<Long> sentMessages = new ArrayList<>();
	private final Timer timer = new Timer();
	private final String regex = "sumove\\s(@.{2,32}#[0-9]{4})(\\s.{1,100})?(\\s.{1,100})?";		//user input pattern

	@Override
	public boolean matches(GenericEvent event) {
		if (event instanceof GuildMessageReceivedEvent e &&
		    StringUtil.stripPrefix(e.getMessage().getContentRaw(), DataManager.getGuild(e.getGuild().getIdLong())).matches(regex))
			return true;
		return event instanceof GuildMessageReactionAddEvent r &&
		       sentMessages.contains(r.getReaction().getMessageIdLong());
	}

	@Override
	public void handle(GenericEvent event) {
		if (event instanceof GuildMessageReceivedEvent e) {
			val guild = e.getGuild();
			val matcher = Pattern.compile(regex).matcher(e.getMessage().getContentRaw());
			matcher.find();
			val tag = matcher.group(1);
			val channel = matcher.group(2);
			val category = matcher.group(3);
			Assert.allNotEmpty(tag, channel, category);
			if (!validateInput(e, tag, channel, category)) //validating user input
				return;
			if (guild.getVoiceChannelsByName(channel, false).size() == 1) {			//if channelname is unique, move user
				guild.moveVoiceMember(guild.getMemberByTag(tag), guild.getVoiceChannelsByName(channel, false).get(0)).queue();
				e.getMessage().getChannel().sendMessage("Moved User to the Channel").queue();
			} else {
				e.getMessage().getChannel().sendMessage("There is more than one channel with the same name").queue(msg ->			//channelname not unique, send message and add reactions
				{
					sentMessages.add(msg.getIdLong());
					msg.addReaction(Emotes.EMOTE_ACCEPT).queue();// TODO maybe change this to message with list of channels and special emotes for each channel, and wait for user reaction input plus one cancel emote, no accept emote
					msg.addReaction(Emotes.EMOTE_CANCEL).queue();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							e.getChannel().deleteMessagesByIds(Collections.singleton(msg.getId())).queue();
							sentMessages.remove(msg.getIdLong());
						}
					}, DELAY);
				});
			}
		} else if (event instanceof GuildMessageReactionAddEvent r) {
			//TODO
		}
	}

	@SuppressWarnings("ConstantConditions")
	private boolean validateInput(GuildMessageReceivedEvent e, String tag, String channel, String category) {		//validates the user input
		val guild = e.getGuild();
		if (guild.getMembers().stream().map(m -> m.getUser().getAsTag())								//validates if the given user exists on the guild
				.noneMatch(username -> username.equals(tag))) {
			return false;
		}
		if (!channel.isEmpty() && guild.getVoiceChannels().stream().map(AbstractChannel::getName)		//validates if user input includes any voicechannel and the given voicechannel exists on the guild
				.noneMatch(channelName -> channelName.equals(channel))) {
			return false;
		}
		if (!category.isEmpty() && guild.getCategories().stream().map(AbstractChannel::getName)			//validates if user input includes any category the given category exists
				.noneMatch(categoryName -> categoryName.equals(category))) {
			return false;
		}
		return guild.getMemberByTag(tag).getVoiceState().inVoiceChannel();
	}
}
