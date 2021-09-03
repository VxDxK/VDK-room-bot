package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static volatile Config instance;
    private String token;
    private String masterID;
    private String textRoomID;
    private String voiceRoomID;
    private String prefix;
    //./config.properties
    private Config(){
        try(FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties")){
            Properties property = new Properties();
            property.load(fileInputStream);
            token = property.getProperty("token");
            masterID = property.getProperty("masterID");
            textRoomID = property.getProperty("textRoomID");
            voiceRoomID = property.getProperty("voiceRoomID");
            prefix = property.getProperty("prefix");
        }catch (FileNotFoundException e){
            System.out.println("Config file loading failed");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Config getConfig(){
        if(instance == null){
            synchronized (Config.class){
                if(instance == null){
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public String getMasterID() {
        return masterID;
    }

    public String getTextRoomID() {
        return textRoomID;
    }

    public String getVoiceRoomID() {
        return voiceRoomID;
    }

    public String getPrefix() {
        return prefix;
    }
}
