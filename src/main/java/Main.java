import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0 ){
            return;
        }
        try {
            JDA jda = JDABuilder.createDefault(args[0])
                    .addEventListeners(new RoomManager())
                    .build().awaitReady();

            System.out.println("Bot started at: " + LocalDateTime.now().toString().replace('T', ' '));
            PrivateChannel channel = jda.openPrivateChannelById("320925451918770177").complete();
            channel.sendMessage("Restart").queue();

        }catch (LoginException | InterruptedException e){
            System.out.print("JDA build fail");
            e.printStackTrace();
        }

    }
}
