package rainbow;

import net.dv8tion.jda.api.managers.RoleManager;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.awt.*;

public class Switcher extends Thread {
    private boolean stop_flag = true;

    private static final Color[] colors = {Color.RED, Color.YELLOW, Color.BLUE, Color.PINK};

    private final BidiMap<String, RoleManager> GuildRoleID = new DualHashBidiMap<>();

    public void add(RoleManager manager){
        this.GuildRoleID.put(manager.getGuild().getId(), manager);

    }

    public BidiMap<String, RoleManager> getGuildRoleID() {
        return GuildRoleID;
    }

    public void pause(){
        stop_flag = true;
    }
    public void unpause(){
        stop_flag = false;
    }

    @Override
    public void run() {
        while (!stop_flag){
            System.out.println("size: " + GuildRoleID.size());
            //Тут еще по коллекции иду и что-то с элементами делаю
        }
    }
}
