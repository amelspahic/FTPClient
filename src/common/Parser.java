package common;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import model.Parameter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by amelsp on 7/8/2017.
 */
public class Parser {
    static String filesDelimiter = ";";

    /**
     * Parse parameters from command line arguments. Default values are taken from config.properties file
     * @param args
     * @return
     */
    public static Parameter parseParameters(String[] args) {
        HashMap<String, String> parsedParameters = new HashMap<>();
        List<String> argsList = new ArrayList<>();
        List<String> doubleOptsList = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i].charAt(0)) {
                case '-':
                    if (args[i].length() < 2) {
                        throw new IllegalArgumentException("Not a valid argument: " + args[i]);
                    }
                    if (args[i].charAt(1) == '-') {
                        if (args[i].length() < 3) {
                            throw new IllegalArgumentException("Not a valid argument: " + args[i]);
                        }
                        doubleOptsList.add(args[i].substring(2, args[i].length()));
                    } else {
                        if (args.length - 1 == i) {
                            throw new IllegalArgumentException("Expected arg after: " + args[i]);
                        }
                        parsedParameters.put(args[i], args[i + 1]);
                        i++;
                    }
                    break;
                default:
                    argsList.add(args[i]);
                    break;
            }
        }

        Parameter p = mapArgumentsToParam(parsedParameters);
        return p;
    }

    private static Parameter mapArgumentsToParam(HashMap<String, String> parsedParameters) {
        Parameter p = new Parameter();
        if (parsedParameters.get(Constants.PARAMETER_USERNAME) != null){
            p.setUsername(parsedParameters.get(Constants.PARAMETER_USERNAME));
        }

        if (parsedParameters.get(Constants.PARAMETER_PASSWORD) != null){
            p.setPassword(parsedParameters.get(Constants.PARAMETER_PASSWORD));
        }

        if (parsedParameters.get(Constants.PARAMETER_SERVER) != null){
            p.setServer(parsedParameters.get(Constants.PARAMETER_SERVER));
        }

        if (parsedParameters.get(Constants.PARAMETER_FILES) != null){
            String[] files = parsedParameters.get(Constants.PARAMETER_FILES).split(Parser.filesDelimiter);
            validateMaxFilesNumber(files);
            p.setFiles(files);
        }
        else{
            throw new IllegalArgumentException("Invalid argument " + Constants.PARAMETER_FILES);
        }

        return p;
    }

    private static void validateMaxFilesNumber(String[] files){
        int maxNumOfFiles = 0;
        try {
            Properties prop = new Properties();
            InputStream inputStream = Parameter.class.getClassLoader().getResourceAsStream("./config.properties");
            prop.load(inputStream);
            inputStream.close();

            maxNumOfFiles = Integer.parseInt(prop.getProperty("maxNumParallelUpload"));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        if (files.length > maxNumOfFiles){
            throw new IllegalArgumentException(String.format("Maximum number of files is: %s", maxNumOfFiles));
        }
    }
}