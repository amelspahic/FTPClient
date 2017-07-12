package client;

import model.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amelsp on 7/12/2017.
 */
public class ClientSync implements Client {
    Parameter connectionParameter;

    public ClientSync(){
        connectionParameter = new Parameter();
    }

    @Override
    public void init(Parameter parameter) throws Exception {
        this.connectionParameter = parameter;

        ftpclient.FTPClient FTPClient = new ftpclient.FTPClient();
        FTPClient.connect(connectionParameter.getServer(), connectionParameter.getUsername(), connectionParameter.getPassword());
        FTPClient.setBinaryMode();
        List<String> stats = new ArrayList<>();

        for (String file: this.connectionParameter.getFiles()) {
            FTPClient.sendFile(file);
            stats.add(FTPClient.getStatistics());
        }

        FTPClient.disconnect();

        for (String s: stats) {
            System.out.println(s);
        }
    }
}
