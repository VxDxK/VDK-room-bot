package musicmanager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
    public final AudioPlayer player;
    public final TrackScheduler scheduler;

    public GuildMusicManager(AudioPlayerManager manager) {
        this.player = manager.createPlayer();
        scheduler = new TrackScheduler(this.player);
        player.addListener(scheduler);
    }

    public AudioPlayerSendHandler getAudioPlayerSendHandler(){
        return new AudioPlayerSendHandler(player);
    }


}

