package client;

import model.Parameter;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by amelsp on 7/12/2017.
 */
public class ClientParallel implements Client {
    Parameter connectionParameter;

    public ClientParallel(){
        this.connectionParameter = new Parameter();
    }

    public void init(Parameter parameter) throws Exception {
        this.connectionParameter = parameter;

        ExecutorService pool = Executors.newFixedThreadPool(connectionParameter.getFiles().length);
        int i = 0;
        final List<Callable<String>> task = new ArrayList<Callable<String>>();

        for (String file : connectionParameter.getFiles()){
            final int threadNum = i;
            task.add(new Callable<String>(){

                @Override
                public String call() throws Exception {
                    ftpclient.FTPClient FTPClient = new ftpclient.FTPClient(threadNum);
                    FTPClient.connect(connectionParameter.getServer(), connectionParameter.getUsername(), connectionParameter.getPassword());
                    FTPClient.setBinaryMode();
                    FTPClient.sendFile(file);
                    FTPClient.disconnect();
                    return FTPClient.getStatistics();
                }
            });

            i++;
        }

        List<Future<String>> results = pool.invokeAll(task);
        for (Future<String> f : results) {
            String user = f.get();
            System.out.println(user);
        }

        pool.shutdown();

        System.out.println("\nOperations completed successfully");
    }
}
