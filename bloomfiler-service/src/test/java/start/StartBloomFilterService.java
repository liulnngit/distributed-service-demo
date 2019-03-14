package start;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nbtv.commons.context.AppContext;

public class StartBloomFilterService {

    private static volatile boolean running = true;
    protected static Log log = LogFactory.getLog(StartBloomFilterService.class);

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    AppContext.stop();

                    System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) + " Main server stopped!");
                } catch (Throwable t) {
                    System.out.println("Main stop error:" + t);
                }

                synchronized (StartBloomFilterService.class) {
                    running = false;
                    StartBloomFilterService.class.notify();
                }
            }
        });

        AppContext.start();

        log.info(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) + " Main server started!");

        synchronized (StartBloomFilterService.class) {
            while (running) {
                try {
                    StartBloomFilterService.class.wait();
                } catch (Throwable e) {
                }
            }
        }
    }

}
