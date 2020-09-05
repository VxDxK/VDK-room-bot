import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.InputStream;
import java.util.Arrays;

public class Main {
    private static String token = "NzUxMTg0MjU3NDg5MTc0NjI4.X1FY2w.lqL9Q0gnUuJXUVjWKAuaEPkAQiE";
    public static void main(String[] args) throws Exception {
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(new NewRooms())
                .build();
    }
}
