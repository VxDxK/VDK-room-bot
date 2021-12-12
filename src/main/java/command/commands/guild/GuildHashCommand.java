package command.commands.guild;

import command.Command;
import command.CommandContext;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class GuildHashCommand implements Command<GuildMessageReceivedEvent> {
    @Override
    public void handle(CommandContext<GuildMessageReceivedEvent> context) {

    }

    @Override
    public String getHelp() {
        return "Return sizes of maps for this guild.";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("hash", "hs");
    }
}
