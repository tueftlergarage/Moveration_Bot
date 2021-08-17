package moveration.bot.commands;

import moveration.bot.Constants;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ChangePrefix implements Command {


    @Override
    public boolean matches(GenericEvent event) {
        //String[] message =((GuildMessageReceivedEvent) event).getMessage().getContentRaw().split("\\ +");
        return

                event instanceof GuildMessageReceivedEvent ;//&&
                //message[0].equalsIgnoreCase(Constants.botprefix+"changeprefix");
    }

    @Override
    public void handle(GenericEvent event) {
        final GuildMessageReceivedEvent e = ((GuildMessageReceivedEvent) event);
        e.getMessage().getChannel().sendMessage("Do you want to change Botprefix?").queue();
    }
}
