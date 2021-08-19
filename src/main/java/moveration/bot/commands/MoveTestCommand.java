package moveration.bot.commands;

import lombok.val;
import moveration.bot.Constants;
import moveration.bot.util.Assert;
import moveration.bot.util.StringUtil;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.*;
import java.util.regex.Pattern;

public class MoveTestCommand implements Command {
    private final List<Long> sentMessages = new ArrayList();
    private final Timer timer = new Timer();
    private final int delay = 180000;

    @Override
    public boolean matches(GenericEvent event) {
        if (event instanceof GuildMessageReceivedEvent e && StringUtil.stripPrefix(((GuildMessageReceivedEvent) event)
                .getMessage().getContentRaw()).matches("sumove\\s@.{2,32}#[0-9]{4}(\\s.{1,100})?(\\s.{1,100})?"))
            return true;


        return event instanceof GuildMessageReactionAddEvent r && sentMessages.contains(r.getReaction().getMessageIdLong());

    }

    @Override
    public void handle(GenericEvent event) {
        assert event instanceof GuildMessageReceivedEvent;
        final GuildMessageReceivedEvent e = ((GuildMessageReceivedEvent) event);
        val pattern = Pattern.compile("sumove\\s(@.{2,32}#[0-9]{4})(\\s.{1,100})?(\\s.{1,100})?");
        val matcher = pattern.matcher(e.getMessage().getContentRaw());
        matcher.find();
        val user = matcher.group(1);
        val channel = matcher.group(2);
        val category = matcher.group(3);
        Assert.allNotNull(user, channel, category);
        final Guild guild = e.getGuild();
        final Member member = e.getMember();
        Assert.allNotNull(guild, member);


        if (guild.getMembers().stream().map(member1 -> member1.getUser().getName() + "#" + member1.getUser().getDiscriminator())
                .noneMatch(username -> username.equals(user))) {
            return;
        }
        if (!channel.isEmpty() && guild.getVoiceChannels().stream().map(AbstractChannel::getName)
                .noneMatch(channelname -> channelname.equals(channel))) {
            return;
        }
        if (!category.isEmpty() && guild.getCategories().stream().map(AbstractChannel::getName)
                .noneMatch(categoryname -> categoryname.equals(category))) {
            return;
        }
        if (!guild.getMemberByTag(user).getVoiceState().inVoiceChannel()) {
            return;
        }
        if (guild.getVoiceChannelsByName(channel, false).size() == 1) {
            guild.moveVoiceMember(guild.getMemberByTag(user), guild.getVoiceChannelsByName(channel, false).get(0)).queue();
            e.getMessage().getChannel().sendMessage("Moved User to the Channel").queue();
        } else {
            e.getMessage().getChannel().sendMessage("There is more than one channel with the same name").queue(msg ->
            {
                sentMessages.add(msg.getIdLong());

                msg.addReaction(Constants.acceptemote).queue();
                msg.addReaction(Constants.cancelemote).queue();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        e.getChannel().deleteMessagesByIds(Collections.singleton(msg.getId())).queue();
                    }
                },delay);
            });
            //timer, automatisch löschen
            //eventlistener reaction


        }


    }
}
