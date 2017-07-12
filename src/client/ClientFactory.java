package client;

import common.ClientType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by amelsp on 7/12/2017.
 */
public class ClientFactory {

    /**
     * Retrieve Client implementation
     * @param clientType
     * @return
     */
    public static Client getClient(ClientType clientType){
        switch (clientType){
            case PARALLEL:
                return new ClientParallel();
            case SYNC:
                return new ClientSync();
            default:
                throw new NotImplementedException();
        }
    }
}
