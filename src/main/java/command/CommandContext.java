package command;

import command.commands.DelCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class CommandContext {
    private final GuildMessageReceivedEvent event;
    private final List<String> arguments;

    public CommandContext(GuildMessageReceivedEvent event, List<String> arguments) {
        this.event = event;
        this.arguments = arguments;
    }

    public GuildMessageReceivedEvent getEvent() {
        return event;
    }
    public User getAuthor(){
        return event.getAuthor();
    }
    public List<String> getArguments() {
        return arguments;
    }
}
