package command.commands.admin;

import command.Command;
import command.CommandContext;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

import static util.Config.*;

public class StopCommand implements Command<PrivateMessageReceivedEvent> {
    @Override
    public void handle(CommandContext<PrivateMessageReceivedEvent> context) {
        if(context.getEvent().getAuthor().getId().equals(getConfig().getMasterID())){
            context.getEvent().getJDA().shutdownNow();
        }
    }

    @Override
    public String getHelp() {
        return "Disconnect bot(only bot owner can use)";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("stop", "halt", "hlt");
    }
}
