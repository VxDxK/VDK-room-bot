import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
public class NewRooms extends ListenerAdapter {
    private final BidiMap<String, String> MemberTextID = new DualHashBidiMap<>();
    private final BidiMap<String, String> MemberVoiceID = new DualHashBidiMap<>();

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

        if(event.getChannelJoined().getName().equalsIgnoreCase("[+] New Voice Room")){
           MemberVoiceID.put(event.getMember().getUser().getId(), createVoiceChannel(event.getMember()).getId());
        }else if(event.getChannelJoined().getName().equalsIgnoreCase("[+] New Text Room")){
            if (!MemberTextID.containsKey(event.getMember().getUser().getId())){
                MemberTextID.put(event.getMember().getUser().getId(), createTextChannel(event.getMember()).getId());
            }
            event.getGuild().kickVoiceMember(event.getMember()).queue();
        }


    }
    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        if(MemberVoiceID.containsKey(event.getMember().getUser().getId()) && MemberVoiceID.get(event.getMember().getUser().getId()).equals(event.getChannelLeft().getId())){
            event.getChannelLeft().delete().submit();
            MemberVoiceID.remove(event.getMember().getUser().getId());
        }


    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {

        if(event.getChannelJoined().getName().equalsIgnoreCase("[+] New Text Room")){
            if(!MemberTextID.containsKey(event.getMember().getUser().getId())){
                MemberTextID.put(event.getMember().getUser().getId(), createTextChannel(event.getMember()).getId());
            }
            event.getGuild().moveVoiceMember(event.getMember(), event.getChannelLeft()).queue();

        }else if(event.getChannelJoined().getName().equalsIgnoreCase("[+] New Voice Room")){
            if(!MemberVoiceID.containsKey(event.getMember().getUser().getId())){
                MemberVoiceID.put(event.getMember().getUser().getId(), createVoiceChannel(event.getMember()).getId());
                return;
            }

            event.getGuild().moveVoiceMember(event.getMember(), event.getChannelLeft()).queue();

        }else {
            if(MemberVoiceID.containsKey(event.getMember().getUser().getId()) && MemberVoiceID.get(event.getMember().getUser().getId()).equals(event.getChannelLeft().getId())){
                event.getChannelLeft().delete().queue();
                MemberVoiceID.remove(event.getMember().getUser().getId(), event.getChannelLeft().getId());
            }
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()){
            return;
        }

        if(event.getMessage().getContentDisplay().equals("/del")){
            try {
                if(MemberTextID.containsKey(Objects.requireNonNull(event.getMember()).getUser().getId()) ||
                        event.getMember().isOwner() ||
                        event.getMember().hasPermission(Permission.ADMINISTRATOR)){
                    String Value = event.getChannel().getId();
                    if(MemberTextID.containsValue(Value)){
                        event.getTextChannel().delete().queue();
                        MemberTextID.removeValue(event.getChannel().getId());
                    }
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }else if(event.getMessage().getContentDisplay().trim().toLowerCase().startsWith("/hash size")){
            event.getChannel().sendMessage("Voice hash size: " + MemberVoiceID.size() +
                    "\n"
                    +                            "Text hash size: " + MemberTextID.size()).queue();
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
         return ch;
    }

    private TextChannel createTextChannel(Member member){
        TextChannel ch = member.getGuild().createTextChannel("room-" + member.getUser().getName())
                .setParent(Objects.requireNonNull(Objects.requireNonNull(member.getVoiceState()).getChannel()).getParent())
                .complete();

        ch.sendMessage("Hi "+ member.getAsMention() + "\nYou can delete this channel using \"***/del***\" in chat. Admins also can delete your chat room.").queue();
        return ch;
    }



}
