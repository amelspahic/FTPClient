package common;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConverterTest {
    @Test
    public void calculateBytesPerSecond() throws Exception {
        //long startTimeMS, long completionTimeMS, long filesizeInBytes
        long startTimeNanoSec = 1000000000;
        long completionTimeNanoSec = 2000000000;
        long filesizeBytes = 5000;

        long expected = Converter.calculateBytesPerSecond(startTimeNanoSec, completionTimeNanoSec, filesizeBytes);

        assertEquals(4, expected);
    }

}