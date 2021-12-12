package command.commands.guild;

import command.Command;
import command.CommandContext;
import listener.CommandManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class GuildHelpCommand implements Command<GuildMessageReceivedEvent> {
    private final CommandManager manager;
    private String message;
    public GuildHelpCommand(CommandManager manager){
        this.manager = manager;
    }
    @Override
    public void handle(CommandContext<GuildMessageReceivedEvent> context) {
        if(message == null){
            messageGenerator();
        }
        context.getEvent().getChannel().sendMessage(message).queue();
    }

    private void messageGenerator(){
        StringBuilder builder = new StringBuilder();
        manager.getCommands().forEach(x -> builder.append(x.getName()).append(" -> ").append(x.getHelp()).append("\n"));
        message = builder.toString();
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("help");
    }
}
