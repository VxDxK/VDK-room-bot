package command.commands;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.lang3.ObjectUtils;
import rooms.RoomManager;
import java.util.Arrays;
import java.util.List;

public class DelCommand implements ICommand {
    @Override
    public void handle(CommandContext context) {
        BidiMap map = RoomManager.getInstance().getMemberTextID();
        Member author = context.getEvent().getMember();
        try {
            if(map.containsKey(author.getId()) ||
                    author.isOwner() ||
                    author.hasPermission(Permission.ADMINISTRATOR) ||
                    author.getId().equals("320925451918770177")){
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
