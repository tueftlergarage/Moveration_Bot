package moveration.bot.commands;

import moveration.bot.util.Assert;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MoveTestCommand implements Command {

    @Override
    public boolean matches(GenericEvent event) {
        return
                event instanceof GuildMessageReceivedEvent &&
                        ((GuildMessageReceivedEvent) event).getMessage().getContentRaw().equalsIgnoreCase("move");
    }

    @Override
    public void handle(GenericEvent event) {
        assert event instanceof GuildMessageReceivedEvent;
        final GuildMessageReceivedEvent e = ((GuildMessageReceivedEvent) event);
        final Guild guild = e.getGuild();
        final Member member = e.getMember();
        final VoiceChannel target = guild.getVoiceChannelById(876914273476046918L);
        Assert.allNotNull(guild, member, target);
        guild.moveVoiceMember(member, target).queue();
    }
}
