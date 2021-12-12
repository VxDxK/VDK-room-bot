package command;
import net.dv8tion.jda.api.events.Event;

import java.util.List;

public class CommandContext<E extends Event> {
    private final E event;
    private final List<String> arguments;

    public CommandContext(E event, List<String> arguments) {
        this.event = event;
        this.arguments = arguments;
    }

    public E getEvent() {
        return event;
    }
    public List<String> getArguments() {
        return arguments;
    }
}
