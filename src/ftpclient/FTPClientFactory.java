package ftpclient;

import common.FTPClientType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class FTPClientFactory {
    public static FTPClient getFTPClient(FTPClientType ftpClientType){
        switch (ftpClientType){
            case AMEL_CLIENT:
                return new FTPClient();
            default:
                throw new NotImplementedException();
        }
    }

    public static FTPClient getFTPClientWithThreads(FTPClientType ftpClientType, int threads){
        switch (ftpClientType){
            case AMEL_CLIENT:
                return new FTPClient(threads);
            default:
                throw new NotImplementedException();
        }
    }
}
