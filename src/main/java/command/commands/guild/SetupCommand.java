package command.commands.guild;

import command.Command;
import command.CommandContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static util.Config.getConfig;

public class SetupCommand implements Command<GuildMessageReceivedEvent> {
    @Override
    public void handle(CommandContext<GuildMessageReceivedEvent> context) {
        if(Objects.requireNonNull(context.getEvent().getMember()).hasPermission(Permission.MANAGE_CHANNEL)){
            Guild guild = context.getEvent().getGuild();
            Category category = context.getEvent().getMessage().getCategory();
            if(context.getArguments().size() == 0 || context.getArguments().get(0).equals("all")){
                createText(guild, category);
                createVoice(guild, category);
            }else{
                switch (context.getArguments().get(0)){
                    case "voice":
                        createVoice(guild, category);
                        break;
                    case "text":
                        createText(guild, category);
                        break;
                }
            }
        }
    }

    private void createText(Guild guild, Category category){
        guild.createVoiceChannel(getConfig().getTextRoomButtonName(), category).queue();
    }

    private void createVoice(Guild guild, Category category){
        guild.createVoiceChannel(getConfig().getVoiceRoomButtonName(), category).queue();
    }

    @Override
    public String getHelp() {
        return "Creating rooms in category. You can specify room types.";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("setup", "qs", "make");
    }
}
