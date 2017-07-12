package ftpclient;

/**
 * Created by amelsp on 7/10/2017.
 */
public interface BaseFTPClient {
    /**
     * Creating new connection
     * @param server
     * @param username
     * @param password
     * @throws Exception
     */
    void connect(String server, String username, String password) throws Exception;

    /**
     * Upload file
     * @param file
     * @return
     * @throws Exception
     */
    boolean sendFile(String file) throws Exception;

    /**
     * Disconnect from Client server
     * @throws Exception
     */
    void disconnect() throws Exception;

    /**
     * Set ASCII transfer mode
     * @return
     * @throws Exception
     */
    boolean setAsciiMode() throws Exception;

    /**
     * Set BINARY transfer mode
     * @return
     * @throws Exception
     */
    boolean setBinaryMode() throws Exception;

    /**
     * Login with username and password
     * @param username
     * @param password
     * @throws Exception
     */
    void login(String username, String password) throws Exception;
}
