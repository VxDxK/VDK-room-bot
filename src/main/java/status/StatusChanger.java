package status;

import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class StatusChanger extends ListenerAdapter {
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {

    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {

    }

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {

    }
}
