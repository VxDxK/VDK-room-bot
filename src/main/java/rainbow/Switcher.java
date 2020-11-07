package rainbow;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class Switcher extends Thread {
    boolean stop_flag = true;

    BidiMap<String, String> GuildRoleID = new DualHashBidiMap<>();

    public void pause(){
        stop_flag = true;
    }
    public void unpause(){
        stop_flag = false;
    }

    @Override
    public void run() {
        while (!stop_flag){
            System.out.println("size: " + GuildRoleID.size());
            //Тут еще по коллекции иду и что-то с элементами делаю
        }
    }
}
