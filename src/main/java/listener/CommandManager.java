package listener;

import command.Command;
import command.CommandContext;
import command.commands.admin.StopCommand;
import command.commands.guild.*;
import command.commands.user.GetGuildsCommand;
import command.commands.user.HashCommand;
import command.commands.user.HelpCommand;
import command.commands.user.MuteCommand;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.*;
import static util.Config.getConfig;


public class CommandManager {
    private final Map<String, Command<GuildMessageReceivedEvent>> guildCommands = new HashMap<>();
    private final Map<String, Command<PrivateMessageReceivedEvent>> userCommands = new HashMap<>();
    private final StringBuilder docBuilder = new StringBuilder();
    private final List<Command> commands = new ArrayList<>();
    private final List<Command<GuildMessageReceivedEvent>> listGuildCommands = new ArrayList<>();
    private final List<Command<PrivateMessageReceivedEvent>> listUserCommands = new ArrayList<>();


    public CommandManager(){
        addGuildCommand(new ClearCommand());
        addGuildCommand(new DelCommand());
        addGuildCommand(new GuildHashCommand());
        addGuildCommand(new GuildHelpCommand(this));
        addGuildCommand(new SetupCommand());

        addUserCommand(new HashCommand());
        addUserCommand(new HelpCommand(this));
        addUserCommand(new MuteCommand());
        addUserCommand(new GetGuildsCommand());
        addUserCommand(new StopCommand());
    }

    private void addGuildCommand(Command<GuildMessageReceivedEvent> command){
        commands.add(command);
        docBuilder.append(command.getHelp());
        for (String alias: command.getAliases()) {
            guildCommands.put(alias, command);
        }
    }

    private void addUserCommand(Command<PrivateMessageReceivedEvent> command){
        commands.add(command);
        docBuilder.append(command.getHelp());
        for (String alias: command.getAliases()) {
            userCommands.put(alias, command);
        }
    }

    public void handle(GuildMessageReceivedEvent event){
        handle(event, guildCommands, event.getMessage().getContentDisplay().split(" "));
    }

    public void handle(PrivateMessageReceivedEvent event){
        handle(event, userCommands, event.getMessage().getContentDisplay().split(" "));
    }

    private <E extends Event> void handle(E event, Map<String, Command<E>> commands, String[] messageContent){
        String commandName = messageContent[0].substring(getConfig().getPrefix().length());
        if(commandName.equals("")){
            return;
        }
        Command<E> command = commands.get(commandName);
        if(command != null){
            CommandContext<E> context = new CommandContext<>(event, Arrays.asList(messageContent).subList(1, messageContent.length));
            command.handle(context);
        }
    }

    public List<Command> getCommands(){
        return commands;
    }

    public String getDocs(){
        return docBuilder.toString();
    }
}
