import listener.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.PrivateChannel;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import rooms.RoomManager;
import util.AsciiColor;

import javax.security.auth.login.LoginException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import static util.Config.getConfig;

public class Main {
    private final JDA jda;
    private final Timer statusSwitcher;

    private Main() throws LoginException, InterruptedException{
        JDABuilder builder = JDABuilder.createDefault(getConfig().getToken())
                .addEventListeners(RoomManager.getInstance())
                .addEventListeners(new CommandListener());
        jda = builder.build().awaitReady();
        statusSwitcher = new Timer("Status switcher", true);
    }

    public static void main(String[] args) {
        try{
            Main main = new Main();
            main.sendMessageToOwner();
            main.printStartupInfo();
            main.startSwitcher();

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

    private void printStartupInfo() throws IOException, XmlPullParserException {
        String version = "";
        if(!System.getProperty("sun.java.command").contains(".jar")){
            Model model = new MavenXpp3Reader().read(new FileReader("pom.xml"));
            version = model.getVersion();
        }else{
            Properties p = new Properties();
            InputStream stream = Main.class.getResourceAsStream("/META-INF/maven/org.vdk/NewRoomBot/pom.properties");
            p.load(stream);
            version = p.getProperty("version");
        }
        String versionMessage = version.concat(version.contains("SNAPSHOT") ? ". Be careful, version is unstable" : "");
        System.out.println(AsciiColor.GREEN_BOLD_BRIGHT + versionMessage + AsciiColor.RESET);
        System.out.println(AsciiColor.BLUE_BOLD + "Bot started at: " + LocalDateTime.now().toString().replace('T', ' ') + AsciiColor.RESET);
    }
    private void sendMessageToOwner(){
        PrivateChannel channel = jda.openPrivateChannelById(getConfig().getMasterID()).complete();
        channel.sendMessage("Restart").queue();
    }

    private void startSwitcher(){
        statusSwitcher.scheduleAtFixedRate(new TimerTask(){
            private boolean position = false;
            private final String name = "@".concat(jda.retrieveUserById("320925451918770177").complete().getName());
            @Override
            public void run() {
                jda.getPresence().setActivity(position ? Activity.playing(name) :
                        Activity.watching(String.valueOf(jda.getGuilds().size()).concat(" servers")));
                position = !position;
            }
        },0 , 10*1000);
//        statusSwitcher.scheduleAtFixedRate(new StatusSwitcher(jda),0,  10*1000);
    }

}
