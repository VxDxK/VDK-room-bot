package rooms;

import command.commands.DelCommand;
import listener.CommandManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static util.Config.getConfig;

public class RoomManager extends ListenerAdapter {
    private static volatile RoomManager instance;
    private final BidiMap<String, String> MemberTextID = new DualHashBidiMap<>();
    private final BidiMap<String, VoiceRoom> MemberVoiceID = new DualHashBidiMap<>();

    private RoomManager() { super(); }

    public static RoomManager getInstance(){
        if(instance == null){
            synchronized (RoomManager.class){
                if(instance == null){
                    instance = new RoomManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        if(event.getChannelJoined().getName().equalsIgnoreCase(getConfig().getVoiceRoomID()) ) {
            if (MemberVoiceID.containsKey(event.getMember().getUser().getId())) {
                event.getGuild().moveVoiceMember(event.getMember(), event.getGuild().getVoiceChannelById(MemberVoiceID.get(event.getMember().getUser().getId()).getVoiceChannelID())).queue();
            } else{
                MemberVoiceID.put(event.getMember().getUser().getId(), new VoiceRoom(event.getGuild().getId(), createVoiceChannel(event.getMember()).getId(), true));
            }
        }else if(event.getChannelJoined().getName().equalsIgnoreCase(getConfig().getTextRoomID())){
            if (!MemberTextID.containsKey(event.getMember().getUser().getId())){
                MemberTextID.put(event.getMember().getUser().getId(), createTextChannel(event.getMember()).getId());
            }
            event.getGuild().kickVoiceMember(event.getMember()).queue();
        }


    }
    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        String key = event.getMember().getUser().getId();
        if(MemberVoiceID.containsValue(new VoiceRoom(event.getGuild().getId(), event.getChannelLeft().getId())) && event.getChannelLeft().getMembers().size() == 0){
            event.getChannelLeft().delete().submit();
            MemberVoiceID.removeValue(new VoiceRoom(event.getGuild().getId(), event.getChannelLeft().getId()));
        }
        if(MemberVoiceID.containsKey(key)
                && MemberVoiceID.get(key).equals(new VoiceRoom(event.getGuild().getId(), event.getChannelLeft().getId()))){
            if (MemberVoiceID.get(key).isLocked() && Objects.requireNonNull(event.getGuild().getVoiceChannelById(MemberVoiceID.get(key).getVoiceChannelID())).getMembers().size() != 0){
                return;
            }
            event.getChannelLeft().delete().submit();
            MemberVoiceID.removeValue(new VoiceRoom(event.getGuild().getId(), event.getChannelLeft().getId()));
        }
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        if(event.getChannelJoined().getName().equalsIgnoreCase(getConfig().getTextRoomID())){
            if(!MemberTextID.containsKey(event.getMember().getUser().getId())){
                MemberTextID.put(event.getMember().getUser().getId(), createTextChannel(event.getMember()).getId());
            }
            event.getGuild().moveVoiceMember(event.getMember(), event.getChannelLeft()).queue();
        }else if(event.getChannelJoined().getName().equalsIgnoreCase(getConfig().getVoiceRoomID())){
            if(!MemberVoiceID.containsKey(event.getMember().getUser().getId())){
                MemberVoiceID.put(event.getMember().getUser().getId(), new VoiceRoom(event.getGuild().getId(), createVoiceChannel(event.getMember()).getId(), true));
            }else {
                event.getGuild().moveVoiceMember(event.getMember(), event.getGuild().getVoiceChannelById(MemberVoiceID.get(event.getMember().getUser().getId()).getVoiceChannelID())).queue();
            }
        }else {
            if(MemberVoiceID.containsKey(event.getMember().getUser().getId()) && MemberVoiceID.get(event.getMember().getUser().getId()).equals(new VoiceRoom(event.getGuild().getId(), event.getChannelLeft().getId()))){
                if(MemberVoiceID.get(event.getMember().getUser().getId()).isLocked() && event.getChannelLeft().getMembers().size() != 0){
                   return;
                }
                event.getChannelLeft().delete().queue();
                MemberVoiceID.removeValue(new VoiceRoom(event.getGuild().getId(), event.getChannelLeft().getId()));
            }else if(MemberVoiceID.containsValue(new VoiceRoom(event.getGuild().getId(), event.getChannelLeft().getId())) && event.getChannelLeft().getMembers().size() == 0){
                event.getChannelLeft().delete().queue();
                MemberVoiceID.removeValue(new VoiceRoom(event.getGuild().getId(), event.getChannelLeft().getId()));
            }
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()){
            return;
        }
        String MessageContent = event.getMessage().getContentDisplay();
        if(MessageContent.trim().toLowerCase().startsWith("/lock")
                && MemberVoiceID.containsKey(Objects.requireNonNull(event.getMember()).getId())
                && MemberVoiceID.get(event.getMember().getUser().getId()).getGuildID().equals(event.getGuild().getId())){
            PrivateChannel channel = event.getJDA().openPrivateChannelById(event.getMember().getUser().getId()).complete();
            channel.sendMessage("Locked").queue();
            MemberVoiceID.get(event.getMember().getUser().getId()).Lock();
        }else if(MessageContent.trim().toLowerCase().startsWith("/unlock")
                && MemberVoiceID.containsKey(Objects.requireNonNull(event.getMember()).getId())
                && MemberVoiceID.get(event.getMember().getUser().getId()).getGuildID().equals(event.getGuild().getId())){
            PrivateChannel PrivateChannel = event.getJDA().openPrivateChannelById(event.getMember().getUser().getId()).complete();
            PrivateChannel.sendMessage("Unlocked").queue();
            MemberVoiceID.get(event.getMember().getUser().getId()).Unlock();
            VoiceChannel channel = event.getGuild().getVoiceChannelById(MemberVoiceID.get(event.getMember().getUser().getId()).getVoiceChannelID());
            if(Objects.requireNonNull(event.getMember().getVoiceState()).getChannel() == null
                    || !Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getId().equals(MemberVoiceID.get(event.getMember().getUser().getId()).getVoiceChannelID())){
                channel.delete().queue();
                MemberVoiceID.removeValue(new VoiceRoom(event.getGuild().getId(), channel.getId()));
            }

        }
    }

    @Override
    public void onTextChannelDelete(@NotNull TextChannelDeleteEvent event) {
        if(MemberTextID.containsValue(event.getChannel().getId())){
            MemberTextID.removeValue(event.getChannel().getId());
        }
    }

    private VoiceChannel createVoiceChannel(Member member){
         VoiceChannel ch = member.getGuild()
        .createVoiceChannel("Room: " + member.getUser().getName())
        .setParent( Objects.requireNonNull(Objects.requireNonNull(member.getVoiceState()).getChannel()).getParent())
        .complete();
         member.getGuild().moveVoiceMember(member, ch).queue();
         PrivateChannel channel = member.getJDA().openPrivateChannelById(member.getUser().getId()).complete();
         channel.sendMessage("Hey, you created voice channel\nYou can unlock it, by typing \"***/unlock***\" in chat of your server.\nAnd lock using\"***/lock***\".").queue();
         return ch;
    }

    private TextChannel createTextChannel(Member member){
        TextChannel ch = member.getGuild().createTextChannel("room-" + member.getUser().getName())
                .setParent(Objects.requireNonNull(Objects.requireNonNull(member.getVoiceState()).getChannel()).getParent())
                .complete();
        ch.sendMessage("Hi "+ member.getAsMention() + "\nYou can delete this channel using \"***" +
                getConfig().getPrefix() +
                "del" +
                "***\" in chat. Admins also can delete your chat room.").queue();
        return ch;
    }


    public BidiMap<String, String> getMemberTextID() {
        return MemberTextID;
    }

    public BidiMap<String, VoiceRoom> getMemberVoiceID() {
        return MemberVoiceID;
    }

}
