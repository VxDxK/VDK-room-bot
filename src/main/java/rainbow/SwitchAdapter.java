package rainbow;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.entities.RoleImpl;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class SwitchAdapter extends ListenerAdapter {
    HashSet<String> ProUserID = new HashSet<>();
    Switcher switcher;



    public SwitchAdapter(JSONObject botData) {
        JSONArray array = (JSONArray) botData.get("ProUserIDs");
        for (int i = 0; i < array.size(); i++) {
            String now = (String) array.get(i);
            if(!ProUserID.contains(now)){
                ProUserID.add(now);
            }
        }

        this.switcher = new Switcher();
        this.switcher.setDaemon(true);
        switcher.start();
    }


    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        String UserId = event.getAuthor().getId();

        if(ProUserID.contains(UserId) && event.getMessage().getContentDisplay().startsWith("/start-th") &&  switcher.isAlive()){
            this.switcher.unpause();
        }else if(ProUserID.contains(UserId) && event.getMessage().getContentDisplay().startsWith("/stop-th") && switcher.isAlive()){
            this.switcher.pause();
        }

    }
}
