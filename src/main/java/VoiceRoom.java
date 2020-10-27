import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Objects;

public class VoiceRoom {
    private String GuildID;
    private String VoiceChannelID;

    private boolean locked = false;

    public VoiceRoom(String GuildID, String VoiceChannelID) {
        this.GuildID = GuildID;
        this.VoiceChannelID = VoiceChannelID;
    }

    public VoiceRoom(Guild guild, VoiceChannel channel){
        this(guild.getId(), channel.getId());
    }

    public VoiceRoom(VoiceChannel channel) {
        this(channel.getGuild(), channel);
    }

    public String getGuildID() {
        return GuildID;
    }

    public String getVoiceChannelID() {
        return VoiceChannelID;
    }

    public boolean isLocked() {
        return locked;
    }
    public void Lock(){
        locked = true;
    }
    public void Unlock(){
        locked = false;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoiceRoom voiceRoom = (VoiceRoom) o;
        return GuildID.equals(voiceRoom.GuildID) &&
                VoiceChannelID.equals(voiceRoom.VoiceChannelID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(GuildID, VoiceChannelID);
    }
}
