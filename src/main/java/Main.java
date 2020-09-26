import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0 ){
            return;
        }
        try {
            JDA jda = JDABuilder.createDefault(args[0])
                    .addEventListeners(new NewRooms())
                    .build();
        }catch (LoginException e){
            System.out.print("JDA build fail");
            e.printStackTrace();
        }

    }
}
