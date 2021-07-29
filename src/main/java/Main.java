import music.MusicManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.PrivateChannel;
import rooms.RoomManager;
import util.AsciiColor;

import javax.security.auth.login.LoginException;

import java.time.LocalDateTime;

import static util.Config.getConfig;

public class Main {
    public static void main(String[] args) {
        try{
            JDABuilder builder = JDABuilder.createDefault(getConfig().getToken())
                    .addEventListeners(RoomManager.getInstance())
                    .addEventListeners(new MusicManager());

            JDA jda = builder.build().awaitReady();
            jda.getPresence().setActivity(Activity.playing(String.valueOf(jda.getGuilds().size())));

            System.out.println("Bot started at: " + LocalDateTime.now().toString().replace('T', ' '));
            PrivateChannel channel = jda.openPrivateChannelById(getConfig().getMasterID()).complete();
            channel.sendMessage("Restart").queue();
            channel.close().complete();
        } catch (LoginException | InterruptedException e) {
            System.out.println(AsciiColor.RED + "JDA build fail.");
            e.printStackTrace();
        }
    }
}
