package music;

import com.google.api.services.youtube.YouTube;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MusicManager extends ListenerAdapter {
    private final AudioPlayerManager playerManager;
    private final Map<String, GuildMusicManager> musicManagers = new HashMap<>(); //Guild id and GuildMusicManager

    private final String COMMAND_PREFIX = "/";


    public MusicManager() {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String msg = event.getMessage().getContentDisplay().trim();
        String[] array = msg.split(" ");
        if(array[0].toLowerCase().startsWith(COMMAND_PREFIX + "play") && array.length >= 2){
            if(array[1].startsWith("https://") || array[1].startsWith("http://")){
                loadAndPlay(event.getChannel(), array[1]);
            }else {
                System.out.println("not a link");

            }

        }else if(array[0].startsWith(COMMAND_PREFIX + "skip")){
            skip(event.getChannel());
        }
    }


    private void loadAndPlay(TextChannel channel, String trackURL){
        GuildMusicManager manager = getGuildMusicManager(channel.getGuild());
        playerManager.loadItemOrdered(manager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                channel.sendMessage("Added to queue").queue();
                play(channel.getGuild(), manager, audioTrack);
            }
            @Override
            public void noMatches() {
                System.out.println("'" + trackURL + "'");
                channel.sendMessage("No matches for: " + trackURL).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessage("FAIL :(").queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                AudioTrack firstTrack = audioPlaylist.getSelectedTrack();
                if(firstTrack == null){
                    firstTrack = audioPlaylist.getTracks().get(0);
                }
                play(channel.getGuild(), manager, firstTrack);
            }

        });
    }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track){
        connectToVoiceChannel(guild.getAudioManager()); //Функция коннекта

        musicManager.scheduler.queue(track);
    }

    private void skip(TextChannel channel){
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
        musicManager.scheduler.nextTrack();
        channel.sendMessage("Skipped").queue();
    }

    private void connectToVoiceChannel(AudioManager audioManager){
        if(!audioManager.isConnected() && !audioManager.isAttemptingToConnect()){
            for(VoiceChannel channel : audioManager.getGuild().getVoiceChannels()){
                if(channel.getName().trim().startsWith("[+] New Text Room") || channel.getName().trim().startsWith("[+] New Voice Room")){
                    continue;
                }
                audioManager.openAudioConnection(channel);
                break;
            }
        }
    }

    private synchronized GuildMusicManager getGuildMusicManager(Guild guild){
        String guildID = guild.getId();
        GuildMusicManager musicManager;
        if(!musicManagers.containsKey(guildID)){
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildID, musicManager);
        }else {
            musicManager = musicManagers.get(guildID);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getAudioPlayerSendHandler());
        return musicManager;
    }


}
