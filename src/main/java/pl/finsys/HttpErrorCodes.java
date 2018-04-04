package pl.finsys;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class HttpErrorCodes {
    private final static Logger logger = Logger.getLogger(HttpErrorCodes.class);
    private final static String ERROR_CODES = "errorCodes.txt";
    private static Map<String, String> errorCodes;

    //loads the error codes with their descriptions from file
    static {
        Path path = FileSystems.getDefault().getPath(ERROR_CODES);
        try {
            errorCodes = Files.lines(path)
                    .filter(s -> s.matches("^\\w+:\\w+"))
                    .collect(Collectors.toMap(k -> k.split(":")[0], v -> v.split(":")[1]));
        } catch (IOException e) {
            logger.error("Cannot read error codes from file " + ERROR_CODES);
        }
    }

    public static String getErrorCodeDescription(String errorCode) {
        return errorCodes.get(errorCode);
    }
}
