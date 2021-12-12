package command;

import net.dv8tion.jda.api.events.Event;

import java.util.List;

public interface Command<E extends Event> {
    void handle(CommandContext<E> context);
    default String getName(){
        return getAliases().get(0);
    }
    String getHelp();
    List<String> getAliases();

}
