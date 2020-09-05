import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class NewRooms extends ListenerAdapter {
    HashMap<Member, VoiceChannel> MemberAndVoice = new HashMap();
    HashMap<Member, TextChannel> MemberAndText = new HashMap();
    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

        if(event.getChannelJoined().getName().equalsIgnoreCase("[+] New Voice Room")){
           MemberAndVoice.put(event.getMember(), createVoiceChannel(event.getMember()));
        }else if(event.getChannelJoined().getName().equalsIgnoreCase("[+] New Text Room")){
            if(!MemberAndText.containsKey(event.getMember())){
                MemberAndText.put(event.getMember(), createTextChannel(event.getMember()));
            }
            event.getGuild().kickVoiceMember(event.getMember()).queue();
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        if(MemberAndVoice.containsKey(event.getMember()) && MemberAndVoice.get(event.getMember()).equals(event.getChannelLeft())){
            event.getChannelLeft().delete().queue();
            MemberAndVoice.remove(event.getMember(), event.getChannelLeft());
        }
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {

        if(event.getChannelJoined().getName().equalsIgnoreCase("[+] New Text Room")){
            if(!MemberAndText.containsKey(event.getMember())){
                MemberAndText.put(event.getMember(), createTextChannel(event.getMember()));
            }
            event.getGuild().moveVoiceMember(event.getMember(), event.getChannelLeft()).queue();

        }else if(event.getChannelJoined().getName().equalsIgnoreCase("[+] New Voice Room")){
            if(!MemberAndVoice.containsKey(event.getMember())){
                MemberAndVoice.put(event.getMember(), createVoiceChannel(event.getMember()));
                return;
            }

            event.getGuild().moveVoiceMember(event.getMember(), event.getChannelLeft()).queue();

        }else {
            if(MemberAndVoice.containsKey(event.getMember()) && MemberAndVoice.get(event.getMember()).equals(event.getChannelLeft())){
                event.getChannelLeft().delete().queue();
                MemberAndVoice.remove(event.getMember(), event.getChannelLeft());
            }
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.getMessage().getContentDisplay().equals("/del")){
            return;
        }
        if(MemberAndText.containsKey(event.getMember()) || event.getMember().isOwner() || event.getMember().hasPermission(Permission.ADMINISTRATOR)){
            if(MemberAndText.get(event.getMember()).equals(event.getTextChannel())){
                event.getTextChannel().delete().queue();
                MemberAndText.remove(event.getMember(), event.getTextChannel());
            }
        }
    }

    private VoiceChannel createVoiceChannel(Member member){
         VoiceChannel ch = member.getGuild()
        .createVoiceChannel("Room: " + member.getUser().getName())
        .setParent( member.getVoiceState().getChannel().getParent())
        .setUserlimit(10)
        .complete();
         member.getGuild().moveVoiceMember(member, ch).queue();
         return ch;
    }

    private TextChannel createTextChannel(Member member){
        TextChannel ch = member.getGuild().createTextChannel("room-" + member.getUser().getName())
                .setParent(member.getVoiceState().getChannel().getParent())
                .complete();

        ch.sendMessage("Hi "+ member.getAsMention() + "\nYou can delete this channel using \"***/del***\" in chat. Admins also can delete your chat room.").queue();
        return ch;
    }

}
