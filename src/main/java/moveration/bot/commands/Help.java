package moveration.bot.commands;

import moveration.bot.Constants;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Help implements Command {

    //public String prefix = "M!";
    @Override
    public boolean matches(GenericEvent event) {
        return
                event instanceof GuildMessageReceivedEvent && ((GuildMessageReceivedEvent) event).getMessage().getContentRaw().equalsIgnoreCase(Constants.botprefix+"help");
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
