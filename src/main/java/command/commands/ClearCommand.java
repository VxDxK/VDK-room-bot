package command.commands;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import util.Config;

import java.util.Arrays;
import java.util.List;

import static util.Config.getConfig;

public class ClearCommand implements ICommand {
    @Override
    public void handle(CommandContext context) {
        if(!context.getEvent().getMember().hasPermission(Permission.MESSAGE_MANAGE) &&
                !context.getEvent().getAuthor().getId().equals(getConfig().getMasterID())){
            return;
        }
        int amount = 11;
        if(context.getArguments().size() != 0){
            try {
                amount = Integer.parseInt(context.getArguments().get(0)) + 1;
            }catch (NumberFormatException ignored){

            }
        }
        TextChannel channel = context.getEvent().getChannel();
        List<Message> messages = channel.getHistory().retrievePast(amount).complete();
        messages.stream().forEach(msg -> msg.delete().queue());
    }

    @Override
    public String getHelp() {
        return "Type\"***" + getConfig().getPrefix() + "clear N***\" to delete N last messages";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("clear", "cl");
    }
}
