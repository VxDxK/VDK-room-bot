package listener;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static util.Config.getConfig;

public class CommandListener extends ListenerAdapter {
    CommandManager manager = new CommandManager();
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot() || event.isWebhookMessage()){
            return;
        }
        String message = event.getMessage().getContentDisplay();
        String prefix = getConfig().getPrefix();

        if(message.startsWith(prefix)){
           manager.handle(event);
        }
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if(event.getAuthor().isBot() || event.getMessage().isWebhookMessage()){
            return;
        }
        String message = event.getMessage().getContentDisplay();
        String prefix = getConfig().getPrefix();
        if(message.startsWith(prefix)){
            manager.handle(event);
        }
    }
}
