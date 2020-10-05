import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0 ){
            return;
        }
        try {
            JDA jda = JDABuilder.createDefault(args[0])
                    .addEventListeners(new NewRooms())
                    .build();
            System.out.println("Bot started at: " + LocalDateTime.now().toString());

        }catch (LoginException e){
            System.out.print("JDA build fail");
            e.printStackTrace();
        }

    }
}
