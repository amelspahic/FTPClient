import common.ClientType;
import common.Parser;
import client.Client;
import client.ClientFactory;
import model.Parameter;

public class FTPClient {

    public static void main(String[] args) throws Exception {
        runFTPClient(args);
    }

    private static void runFTPClient(String[] args) throws Exception {
        Parameter connectionParameter = Parser.parseParameters(args);
        Client client = ClientFactory.getClient(ClientType.PARALLEL);
        client.init(connectionParameter);
    }
}
