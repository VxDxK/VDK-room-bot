import com.google.api.services.youtube.YouTube;
import music.MusicManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.internal.JDAImpl;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rainbow.SwitchAdapter;
import rooms.RoomManager;

import javax.security.auth.login.LoginException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0 ){
            return;
        }
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream stream = classLoader.getResourceAsStream("BotData.json");
            JSONObject BotData;

            JDABuilder builder = JDABuilder.createDefault(args[0])
                    .addEventListeners(new RoomManager())
                    .addEventListeners(new MusicManager());

            if(stream != null){
                JSONParser parser = new JSONParser();
                BotData = (JSONObject) parser.parse(IOUtils.toString(stream, StandardCharsets.UTF_8));
                builder.addEventListeners(new SwitchAdapter(BotData));
            }else{
                System.out.println("BotData.json failed to find");
            }



            JDA jda = builder.build().awaitReady();

            

            System.out.println("Bot started at: " + LocalDateTime.now().toString().replace('T', ' '));
            PrivateChannel channel = jda.openPrivateChannelById("320925451918770177").complete();
            channel.sendMessage("Restart").queue();

        }catch (LoginException | InterruptedException | IOException e){
            System.out.print("JDA build fail");
            e.printStackTrace();
        }catch (NullPointerException e){
            System.out.println("NPE here");
            e.printStackTrace();
        }catch (ParseException e){
            System.out.println("JSON parsing failed");
            e.printStackTrace();
        }

    }
}
