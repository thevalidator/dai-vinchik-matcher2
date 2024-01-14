package ru.thevalidator.daivinchikmatcher2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.thevalidator.daivinchikmatcher2.config.SpringJavaConfig;
import ru.thevalidator.daivinchikmatcher2.task.request.DaiVinchikDialogHandler;

import java.io.IOException;

public class MainSpring {

    public static final Logger LOG = LoggerFactory.getLogger(MainSpring.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        LOG.info("DAI-VINCHIK-MATCHER 2 -> START");

        var context = new AnnotationConfigApplicationContext(SpringJavaConfig.class);
        DaiVinchikDialogHandler service = context.getBean(DaiVinchikDialogHandler.class);
        Thread thread = new Thread(service);
        thread.start();
        thread.join();
        System.out.println();

        LOG.info("DAI-VINCHIK-MATCHER 2 -> END");
    }

}
