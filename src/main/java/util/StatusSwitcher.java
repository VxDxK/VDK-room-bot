package util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import java.util.TimerTask;

@Deprecated
public class StatusSwitcher extends TimerTask {
    private JDA jda;
    private boolean position = false;
    public StatusSwitcher(JDA jda) {
        super();
        this.jda = jda;
    }

    @Override
    public void run() {
        jda.getPresence().setActivity(position ? Activity.playing("@fAntom") :
                Activity.watching(String.valueOf(jda.getGuilds().size()).concat(" servers")));
        position = !position;
    }
}
