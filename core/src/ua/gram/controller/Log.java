package ua.gram.controller;

import com.badlogic.gdx.Gdx;

import java.util.Arrays;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Log {

    /**
     * Normal log
     */
    public synchronized static void info(String msg) {
        Gdx.app.log("INFO",
//                new SimpleDateFormat("HH:mm:ss").format(new Date()) + ": " +
                msg);
    }

    public synchronized static void warn(String msg) {
        Gdx.app.log("WARN", msg);
    }

    /**
     * Log critical errors. Should not happen
     */
    public synchronized static void crit(String msg) {
        Gdx.app.error("CRIT", msg);
    }

    /**
     * Log exceptions
     */
    public synchronized static void exc(String msg, Exception e) {
        msg += "\nCLASS:\t" + e.getClass().getSimpleName();
        msg += "\nMSG:\t" + e.getMessage();
        msg += "\nTRACE:\t" + Arrays.toString(e.getStackTrace()) + "\r\n";
        Gdx.app.error("EXC",
//                new SimpleDateFormat("HH:mm:ss").format(new Date()) + ": " +
                msg);
    }
}