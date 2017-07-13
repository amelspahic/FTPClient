package client;

import common.FTPClientType;
import ftpclient.BaseFTPClient;
import ftpclient.FTPClientFactory;
import model.Parameter;

import java.util.*;
import java.util.concurrent.*;

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
                    BaseFTPClient ftpCli = FTPClientFactory.getFTPClientWithThreads(FTPClientType.AMEL_CLIENT, threadNum);
                    ftpCli.connect(connectionParameter.getServer(), connectionParameter.getUsername(), connectionParameter.getPassword());
                    ftpCli.setBinaryMode();
                    ftpCli.sendFile(file);
                    ftpCli.disconnect();
                    return ftpCli.getStatistics();
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
