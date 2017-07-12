package common;

/**
 * Created by amelsp on 7/8/2017.
 */

public class Constants {
    //region Command line parameters
    public static final String PARAMETER_USERNAME = "-u";
    public static final String PARAMETER_PASSWORD = "-p";
    public static final String PARAMETER_SERVER = "-server";
    public static final String PARAMETER_FILES = "-files";
    //endregion

    //region Client Server commands - RFC 959 (not all spec commands are implemented)
    public static final String COMMAND_ASCII = "TYPE A";
    public static final String COMMAND_BINARY = "TYPE I";
    public static final String COMMAND_PASSIVE = "PASV";
    public static final String COMMAND_USERNAME = "USER";
    public static final String COMMAND_PASSWORD = "PASS";
    public static final String COMMAND_SEND = "STOR";
    public static final String COMMAND_QUIT = "QUIT";
    //endregion

    //region Client Server response codes - RFC 959 (not all spec response codes are implemented)
    public static final String RESPONSE_DATA_CONNECTION_OK = "125";
    public static final String RESPONSE_FILE_STATUS_OK = "150";
    public static final String RESPONSE_COMMAND_OK = "200";
    public static final String RESPONSE_SERVICE_READY = "220";
    public static final String RESPONSE_FILE_TRANSFER_SUCCESS = "226";
    public static final String RESPONSE_PASSIVE_MODE_OK = "227";
    public static final String RESPONSE_USER_LOGGED_IN = "230";
    public static final String RESPONSE_USER_OK_NEEDS_PASSWORD = "331";
    //endregion
}
