package command.commands.user;

import command.CommandContext;
import command.Command;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GetGuildsCommand implements Command<PrivateMessageReceivedEvent> {
    @Override
    public void handle(CommandContext<PrivateMessageReceivedEvent> context) {
        new Thread(() -> {
            MessageBuilder builder = new MessageBuilder();
            context.getEvent().getJDA().getGuilds().stream().forEachOrdered(x -> builder.append(x.getName() + " - " + x.retrieveInvites().complete().stream().max(Comparator.comparingInt(Invite::getUses)).get().getCode() + "\n"));
            System.out.println(builder.getStringBuilder());
            context.getEvent().getAuthor().openPrivateChannel().queue((channel) ->{
                channel.sendMessage(builder.build()).queue();
            });
        }).start();
    }

    @Override
    public String getHelp() {
        return "Return all servers, where chilling bot";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("gg");
    }
}
