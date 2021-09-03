package listener;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static util.Config.getConfig;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot() || event.isWebhookMessage()){
            return;
        }
        String message = event.getMessage().getContentDisplay();
        String prefix = getConfig().getPrefix();

        if(message.startsWith(prefix)){
            CommandManager.getInstance().handle(event);
        }
    }
}
