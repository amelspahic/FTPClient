package common;

/**
 * Created by amelsp on 7/12/2017.
 */
public class Converter {
    /**
     * Will calculate kilobytes per second from given parameters
     * @param startTimeMS
     * @param completionTimeMS
     * @param filesizeInBytes
     * @return
     */
    public synchronized static long calculateBytesPerSecond(long startTimeMS, long completionTimeMS, long filesizeInBytes){
        double elapsedTimeInSeconds = (completionTimeMS - startTimeMS)/1000000000;
        double filesizeKB = filesizeInBytes/1024;

        if (elapsedTimeInSeconds < 1){
            elapsedTimeInSeconds = 1;
        }

        double bytesPerSecond = filesizeKB/elapsedTimeInSeconds;

        return (long)bytesPerSecond;
    }
}
