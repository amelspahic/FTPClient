package model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by amelsp on 7/8/2017.
 */
public class Parameter {
    String username;
    String password;
    String server;
    String[] files;

    public Parameter(){
        setDefaultProperties();
    }

    private void setDefaultProperties() {
        Properties prop = new Properties();

        try {
            InputStream inputStream = Parameter.class.getClassLoader().getResourceAsStream("./config.properties");
            prop.load(inputStream);
            inputStream.close();

            this.username = prop.getProperty("username");
            this.password = prop.getProperty("password");
            this.server = prop.getProperty("server");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setServer(String server){
        this.server = server;
    }

    public void setFiles(String[] files){
        this.files = files;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getServer(){
        return this.server;
    }

    public String[] getFiles(){
        return this.files;
    }
}
