package ftpclient;

import common.Constants;
import common.Converter;
import common.Progress;
import helpers.SocketParameter;
import helpers.Statistics;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Created by amelsp on 7/10/2017.
 */
public class FTPClient implements BaseFTPClient {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private int threadNumber;
    private Statistics statistics;

    public FTPClient(){
        this.threadNumber = 0;
    }

    public FTPClient(int threadNumber){
        this.threadNumber = threadNumber;
    }

    /**
     * Creates connection to FTP server with given parameters and performs login with given username and password
     * @param server
     * @param username
     * @param password
     * @throws Exception
     */
    @Override
    public synchronized void connect(String server, String username, String password) throws Exception {
        initialize(server);
        login(username, password);
    }

    private void initialize(String server) throws Exception {
        try {
            this.socket = new Socket(server, 21);
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.statistics = new Statistics();

            String response = readServerResponse();
            validateServerResponse(response, Constants.RESPONSE_SERVICE_READY);
        } catch (Exception ex){
            throw ex;
        }
    }

    private String readServerResponse() throws Exception {
        return bufferedReader.readLine();
    }

    /**
     * Sends command to Client server
     * @param command
     * @throws Exception
     */
    private void sendCommand(String command) throws Exception {
        try {
            //From RFC 959 - "It should be noted that the server is to take no action until the end of line code is received."
            bufferedWriter.write(String.format("%s\r\n", command));
            bufferedWriter.flush();
        } catch (Exception ex){
            throw ex;
        }
    }

    /**
     * Sends command for ASCII transfer mode to FTP server
     * @return
     * @throws Exception
     */
    @Override
    public synchronized boolean setAsciiMode() throws Exception {
        sendCommand(Constants.COMMAND_ASCII);
        String response = readServerResponse();
        return validateServerResponse(response, Constants.RESPONSE_COMMAND_OK);
    }

    /**
     * Sends command for BINARY transfer mode to FTP server
     * @return
     * @throws Exception
     */
    @Override
    public synchronized boolean setBinaryMode() throws Exception {
        sendCommand(Constants.COMMAND_BINARY);
        String response = readServerResponse();
        return validateServerResponse(response, Constants.RESPONSE_COMMAND_OK);
    }

    /**
     * Sends file to FTP server default directory
     * @param filePath
     * @return
     * @throws Exception
     */
    @Override
    public synchronized boolean sendFile(String filePath) throws Exception {
        File file = new File(filePath);
        String filename = file.getName();

        BufferedInputStream input = new BufferedInputStream(new FileInputStream(filePath));

        sendCommand(Constants.COMMAND_PASSIVE);

        String response = readServerResponse();
        validateServerResponse(response, Constants.RESPONSE_PASSIVE_MODE_OK);

        SocketParameter socketParameter = getSocket(response);
        sendCommand(String.format("%s %s", Constants.COMMAND_SEND, filename));

        Socket dataSocket = new Socket(socketParameter.getIpAddress(), socketParameter.getPortNumber());

        response = readServerResponse();
        if (!response.startsWith(Constants.RESPONSE_DATA_CONNECTION_OK)
                && !response.startsWith(Constants.RESPONSE_FILE_STATUS_OK)) {
            throw new Exception(String.format("Error sending file: %s", response));
        }

        BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int i;
        long written = 0;
        long len = file.length();

        Progress progress = new Progress(this.threadNumber, len, filename);

        while ((i = input.read(buffer)) != -1) {
            output.write(buffer, 0, i);
            written += i;
            progress.update(written);
        }

        this.statistics = progress.complete();
        output.flush();
        output.close();
        input.close();

        response = readServerResponse();
        return response.startsWith(Constants.RESPONSE_FILE_TRANSFER_SUCCESS);
    }

    /**
     * Gets formatted statistics for filename, total size, average transfer speed and total time for upload
     * @return
     */
    @Override
    public synchronized String getStatistics(){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n").append(this.statistics.getFilename()).append(" - Total size: ").append(this.statistics.getTransferSize()).append(" KB/s")
                .append(" - ").append("Total time: ")
                .append((this.statistics.getCompletionTime() - this.statistics.getStartTime())/1000000000)
                .append("seconds").append(" - ").append("Average transfer speed: ")
                .append(Converter.calculateBytesPerSecond(this.statistics.getStartTime(), this.statistics.getCompletionTime(), this.statistics.getTransferSize()))
                .append(" KB/s");

        return stringBuilder.toString();
    }

    private synchronized SocketParameter getSocket(String response) throws Exception {
        SocketParameter socketParameter = new SocketParameter();

        socketParameter.setIpAddress(null);
        socketParameter.setPortNumber(-1);

        int startingIndex = response.indexOf('(');
        int closingIndex = response.indexOf(')', startingIndex + 1);
        if (closingIndex > 0) {
            String dataLink = response.substring(startingIndex + 1, closingIndex);
            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
            try {
                socketParameter.setIpAddress(String.format("%s.%s.%s.%s", tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken()));
                socketParameter.setPortNumber(Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken()));
            } catch (Exception e) {
                throw new Exception("Bad information: " + response);
            }
        }

        return socketParameter;
    }

    /**
     * Sends command to FTP server to disconnect
     * @throws Exception
     */
    @Override
    public synchronized void disconnect() throws Exception {
        try {
            sendCommand(Constants.COMMAND_QUIT);
        } finally {
            socket = null;
        }
    }

    /**
     * Login to FTP server with given username and password. Use connect() command to connect and login if specific login is not necessary
     * @param username
     * @param password
     * @throws Exception
     */
    @Override
    public synchronized void login(String username, String password) throws Exception {
        sendCommand(String.format("%s %s", Constants.COMMAND_USERNAME, username));
        String response = readServerResponse();
        validateServerResponse(response, Constants.RESPONSE_USER_OK_NEEDS_PASSWORD);
        sendCommand(String.format("%s %s", Constants.COMMAND_PASSWORD, password));
        response = readServerResponse();
        validateServerResponse(response, Constants.RESPONSE_USER_LOGGED_IN);
    }

    private boolean validateServerResponse(String serverResponse, String command) throws Exception {
        if (!serverResponse.startsWith(command)) {
            throw new Exception(String.format("Unknown response: %s", serverResponse));
        }

        return true;
    }
}
