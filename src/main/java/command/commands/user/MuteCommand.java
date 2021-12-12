package command.commands.user;

import command.Command;
import command.CommandContext;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class MuteCommand implements Command<PrivateMessageReceivedEvent> {

    @Override
    public void handle(CommandContext<PrivateMessageReceivedEvent> context) {

    }

    @Override
    public String getHelp() {
        return "Mute notifications from bot";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("mute", "mt");
    }
}
