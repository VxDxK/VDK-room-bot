package command.commands;

import command.CommandContext;
import command.ICommand;
import listener.CommandManager;
import util.Config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static util.Config.getConfig;

public class HelpCommand implements ICommand {
    @Override
    public void handle(CommandContext context) {
        if(context.getArguments().size() == 0){
            context.getEvent().getChannel().sendMessage(messageGenerator()).queue();
            return;
        }
        ICommand cmd = CommandManager.getInstance().searchCommand(context.getArguments().get(0).toLowerCase(Locale.ROOT));
        if(cmd == null){
            context.getEvent().getChannel().sendMessage("Command not found").queue();
        }else{
            context.getEvent().getChannel()
                    .sendMessage(getConfig().getPrefix() + cmd.getName() + " aliases: " + Arrays.toString(cmd.getAliases().toArray()))
                    .queue();
        }
    }

    private String messageGenerator(){
        StringBuilder builder = new StringBuilder();
        CommandManager.getInstance().getCommands().forEach(x -> builder.append(getConfig().getPrefix() + x.getName()+ " - "+ x.getHelp() + '\n'));
        return builder.toString();
    }

    @Override
    public String getHelp() {
        return "Print info about bot commands";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("help");
    }
}
