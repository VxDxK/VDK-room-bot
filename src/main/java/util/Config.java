package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String CONFIG_PROPERTIES = "config.properties";

    private static volatile Config instance;
    private String token;
    private String masterID;
    private String textRoomButtonName;
    private String voiceRoomButtonName;
    private String prefix;
    private Config(){
        try(FileInputStream fileInputStream = new FileInputStream(CONFIG_PROPERTIES)){
            readFile(fileInputStream);
        }catch (FileNotFoundException e){
            System.out.println("Config file loading failed");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile(FileInputStream file) throws IOException {
        Properties property = new Properties();
        property.load(file);
        token = property.getProperty("token");
        masterID = property.getProperty("masterID");
        textRoomButtonName = property.getProperty("textRoomButtonName");
        voiceRoomButtonName = property.getProperty("voiceRoomButtonName");
        prefix = property.getProperty("prefix");
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

    public String getTextRoomButtonName() {
        return textRoomButtonName;
    }

    public String getVoiceRoomButtonName() {
        return voiceRoomButtonName;
    }

    public String getPrefix() {
        return prefix;
    }
}
