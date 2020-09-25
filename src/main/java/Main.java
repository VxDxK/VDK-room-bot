import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {
    //private static String token = "NzUxMTg0MjU3NDg5MTc0NjI4.X1FY2w.lqL9Q0gnUuJXUVjWKAuaEPkAQiE";
    public static void main(String[] args) throws Exception {
        if(args.length == 0 ){
            return;
        }
        JDA jda = JDABuilder.createDefault(args[0])
                .addEventListeners(new NewRooms())
                .build();

    }
}
