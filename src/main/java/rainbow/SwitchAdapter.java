package rainbow;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.HashSet;

public class SwitchAdapter extends ListenerAdapter{
    HashSet<String> ProUserID = new HashSet<>();
    Switcher switcher;



    public SwitchAdapter(JSONObject botData) {
        JSONArray array = (JSONArray) botData.get("ProUserIDs");
        for (Object o : array) {
            String now = (String) o;
            if (!ProUserID.contains(now)) {
                ProUserID.add(now);
            }
        }

        this.switcher = new Switcher();
        this.switcher.setDaemon(true);
        switcher.start();
    }


    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String MessageSTR = event.getMessage().getContentDisplay();
        String UserId = event.getAuthor().getId();

        //RoleImpl s = new RoleImpl(event.getJDA().getRoleById("774678337964343296").getIdLong(), event.getGuild());

        if(ProUserID.contains(UserId) && MessageSTR.startsWith("/start-th") &&  switcher.isAlive()){
            this.switcher.unpause();
        }else if(ProUserID.contains(UserId) && MessageSTR.startsWith("/stop-th") && switcher.isAlive()){
            this.switcher.pause();
        }else if(ProUserID.contains(UserId) && MessageSTR.startsWith("/red")){
            event.getJDA().getRoleById("774678337964343296").getManager().setColor(Color.RED).queue();
        }else if(ProUserID.contains(UserId) && MessageSTR.startsWith("/ye")){
            event.getJDA().getRoleById("774678337964343296").getManager().setColor(Color.YELLOW).queue();
        }else if( ProUserID.contains(UserId) && MessageSTR.startsWith("/get")){
            event.getChannel().sendMessage(" String"+ this.switcher.getGuildRoleID().size()).queue();
        }else if(ProUserID.contains(UserId) && MessageSTR.startsWith("/add")){
            this.switcher.add(event.getJDA().getRoleById("774678337964343296").getManager());
        }

    }

}
