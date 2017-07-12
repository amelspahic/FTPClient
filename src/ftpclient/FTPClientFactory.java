package ftpclient;

import common.FTPClientType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by amelsp on 7/12/2017.
 */
public class FTPClientFactory {
    public static FTPClient getFTPClient(FTPClientType ftpClientType){
        switch (ftpClientType){
            case AMEL_CLIENT:
                return new FTPClient();
            default:
                throw new NotImplementedException();
        }
    }
}
