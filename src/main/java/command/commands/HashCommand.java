package command.commands;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import rooms.RoomManager;

import java.nio.channels.Channel;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HashCommand implements ICommand {
    @Override
    public void handle(CommandContext context) {
        if(context.getArguments().size() >= 1){
            String argument = context.getArguments().get(0).toLowerCase(Locale.ROOT);
            TextChannel channel = context.getEvent().getChannel();
            String answer = "";
            switch (argument){
                case "all":
                    answer = "Voice hash size: " + RoomManager.getInstance().getMemberVoiceID().size() +
                    "\nText hash size: " + RoomManager.getInstance().getMemberTextID().size();
                break;
                case "vc":
                    answer = "Voice hash size: " + RoomManager.getInstance().getMemberVoiceID().size();
                    break;
                case "tc":
                    answer = "Text hash size: " + RoomManager.getInstance().getMemberTextID().size();
                    break;
                default:
                    return;
            }
            channel.sendMessage(answer).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Returns sizes of room maps";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("hash", "hs");
    }
}
