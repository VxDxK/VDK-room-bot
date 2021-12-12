package command.commands.user;

import command.CommandContext;
import command.Command;
import listener.CommandManager;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;


public class HelpCommand implements Command<PrivateMessageReceivedEvent> {
    private final CommandManager manager;
    private String message;
    public HelpCommand(CommandManager manager){
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext<PrivateMessageReceivedEvent> context) {
        if(message == null){
            messageGenerator();
        }
        context.getEvent().getChannel().sendMessage(message).queue();
    }

    @Override
    public String getHelp() {
        return "Print info about bot commands";
    }

    private void messageGenerator(){
        StringBuilder builder = new StringBuilder();
        manager.getCommands().forEach(x -> builder.append(x.getName()).append(" -> ").append(x.getHelp()).append("\n"));
        message = builder.toString();
    }


    @Override
    public List<String> getAliases() {
        return Arrays.asList("help");
    }
}
