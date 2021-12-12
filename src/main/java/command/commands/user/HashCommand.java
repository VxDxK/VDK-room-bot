package command.commands.user;

import command.CommandContext;
import command.Command;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import rooms.RoomManager;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HashCommand implements Command<PrivateMessageReceivedEvent> {
    @Override
    public void handle(CommandContext<PrivateMessageReceivedEvent> context) {
        StringBuilder answer = new StringBuilder();
        String  argument;
        if(context.getArguments().size() >= 1){
            argument = context.getArguments().get(0).toLowerCase(Locale.ROOT);
        }else{
            argument = "";
        }
        switch (argument) {
            case "vc":
                answer.append(getVoiceInfo());
                break;
            case "tc":
                answer.append(getTextInfo());
                break;
            default:
                answer.append(getTextInfo()).append("\n").append(getVoiceInfo());
        }
        context.getEvent().getAuthor().openPrivateChannel().complete().sendMessage(answer.toString()).queue();
    }

    private String getTextInfo(){
        return "Voice hash size: " + RoomManager.getInstance().getMemberVoiceID().size();
    }

    private String getVoiceInfo(){
        return "Voice hash size: " + RoomManager.getInstance().getMemberVoiceID().size();
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
