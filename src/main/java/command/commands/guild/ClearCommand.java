package command.commands.guild;

import command.CommandContext;
import command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

import static util.Config.getConfig;

public class ClearCommand implements Command<GuildMessageReceivedEvent> {
    @Override
    public void handle(CommandContext<GuildMessageReceivedEvent> context) {
        if(!Objects.requireNonNull(context.getEvent().getMember()).hasPermission(Permission.MESSAGE_MANAGE) &&
                !context.getEvent().getAuthor().getId().equals(getConfig().getMasterID())){
            return;
        }
        int amount = 10;
        if(context.getArguments().size() != 0){
            try {
                amount = Integer.parseInt(context.getArguments().get(0));
                if(amount > 100 || amount <= 0){
                    amount = 100;
                }
            }catch (NumberFormatException ignored){
                //TODO:
            }
        }
        context.getEvent().getMessage().delete().queue();
        TextChannel channel = context.getEvent().getChannel();
        List<Message> messages = channel.getHistory().retrievePast(amount).complete();
        Timer s = new Timer();
        s.scheduleAtFixedRate(new TimerTask() {
            int iterator = 0;
            @Override
            public void run() {
                if (iterator >= messages.size()){
                    s.cancel();
                }else{
                    messages.get(iterator).delete().queue();
                    iterator++;
                }
            }
        }, 0, 1000);
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
