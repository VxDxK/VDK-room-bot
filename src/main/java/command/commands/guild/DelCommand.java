package command.commands.guild;

import command.CommandContext;
import command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.collections4.BidiMap;
import rooms.RoomManager;
import util.Config;

import java.util.Arrays;
import java.util.List;

public class DelCommand implements Command<GuildMessageReceivedEvent> {
    @Override
    public void handle(CommandContext<GuildMessageReceivedEvent> context) {
        BidiMap map = RoomManager.getInstance().getMemberTextID();
        Member author = context.getEvent().getMember();
        try {
            if(map.containsKey(author.getId()) ||
                    author.isOwner() ||
                    author.hasPermission(Permission.ADMINISTRATOR) ||
                    author.getId().equals(Config.getConfig().getMasterID())){
                if(map.containsValue(context.getEvent().getChannel().getId())){
                    context.getEvent().getChannel().delete().queue();
                    map.removeValue(context.getEvent().getChannel().getId());
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }


    @Override
    public String getHelp() {
        return "Delete user channel";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("del", "delete", "delit");
    }
}
