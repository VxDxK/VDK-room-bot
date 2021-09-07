package listener;

import command.CommandContext;
import command.ICommand;
import command.commands.ClearCommand;
import command.commands.DelCommand;
import command.commands.HashCommand;
import command.commands.HelpCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import rooms.RoomManager;

import java.util.ArrayList;
import java.util.Arrays;

import static util.Config.getConfig;

public class CommandManager {
    private static volatile CommandManager manager;
    private ArrayList<ICommand> commands = new ArrayList<>();

    public static CommandManager getInstance(){
        if(manager == null){
            synchronized (RoomManager.class){
                if(manager == null){
                    manager = new CommandManager();
                }
            }
        }
        return manager;
    }

    private CommandManager(){
        commands.add(new DelCommand());
        commands.add(new HashCommand());
        commands.add(new ClearCommand());
        commands.add(new HelpCommand());
    }

    public ArrayList<ICommand> getCommands() {
        return commands;
    }

    public ICommand searchCommand(String commandName){

        for(ICommand comm : commands){
            if(comm.getAliases().contains(commandName.toLowerCase())){
                return comm;
            }
        }
        return null;
    }

    public void handle(GuildMessageReceivedEvent event){
        String prefix = getConfig().getPrefix();
        String[] parsed = event.getMessage().getContentDisplay().split(" ");
        String commandName = parsed[0].substring(getConfig().getPrefix().length());
        if(commandName.equals("")){
            return;
        }
        ICommand command = searchCommand(commandName);
        if(command != null){
            CommandContext context = new CommandContext(event, Arrays.asList(parsed)
                    .subList(1, parsed.length));
            command.handle(context);
        }

    }

}
