package command;

import java.util.Arrays;
import java.util.List;

public interface ICommand {
    void handle(CommandContext context);

    default String getName(){
        return getAliases().get(0);
    }

    String getHelp();

    List<String> getAliases();
}
