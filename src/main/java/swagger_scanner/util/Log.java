package swagger_scanner.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {

    public static Logger log() {
        var stackTrace = Thread.currentThread().getStackTrace();
        var callingClassName = stackTrace[2].getClassName();
        try {
            return LogManager.getLogger(Class.forName(callingClassName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
