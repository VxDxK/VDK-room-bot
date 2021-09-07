import listener.CommandListener;
import music.MusicManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import rooms.RoomManager;
import util.AsciiColor;
import util.StatusSwitcher;

import javax.security.auth.login.LoginException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Timer;

import static util.Config.getConfig;

public class Main {
    private static JDA jda;
    public static void main(String[] args) {
        try{
            JDABuilder builder = JDABuilder.createDefault(getConfig().getToken())
                    .addEventListeners(RoomManager.getInstance())
                    .addEventListeners(new MusicManager())
                    .addEventListeners(new CommandListener());
            jda = builder.build().awaitReady();
            printStartupInfo();
            sendMessageToOwner();
            runStatusTimer();
        } catch (LoginException | InterruptedException e) {
            System.out.println(AsciiColor.RED + "JDA build fail." + AsciiColor.RESET);
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            System.out.println("pom.xml parse failed");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printStartupInfo() throws IOException, XmlPullParserException {
        //Model model = new MavenXpp3Reader().read(new FileReader("pom.xml"));
        //String versionMessage = model.getVersion().concat(model.getVersion().contains("SNAPSHOT") ? ". Be careful, version is unstable" : "");
        //System.out.println(AsciiColor.GREEN_BOLD_BRIGHT + versionMessage + AsciiColor.RESET);
        System.out.println(AsciiColor.BLUE_BOLD + "Bot started at: " + LocalDateTime.now().toString().replace('T', ' '));
    }
    private static void sendMessageToOwner(){
        PrivateChannel channel = jda.openPrivateChannelById(getConfig().getMasterID()).complete();
        channel.sendMessage("Restart").queue();
        channel.close().complete();
    }
    private static void runStatusTimer(){
        Timer statusSwitcher = new Timer();
        statusSwitcher.scheduleAtFixedRate(new StatusSwitcher(jda),0,  5*1000);
    }
}
