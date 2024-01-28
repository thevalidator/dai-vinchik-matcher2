package ru.thevalidator.daivinchikmatcher2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.thevalidator.daivinchikmatcher2.config.DaiVinchikDialogHandlerFactory;
import ru.thevalidator.daivinchikmatcher2.config.SpringJavaConfig;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.TaskRunner;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.impl.TaskRunnerImpl;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.request.DaiVinchikDialogHandler;

import java.util.concurrent.TimeUnit;

public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        LOG.info("DAI-VINCHIK-MATCHER 2 -> START");

        var context = new AnnotationConfigApplicationContext(SpringJavaConfig.class);
        DaiVinchikDialogHandlerFactory factory = context.getBean(DaiVinchikDialogHandlerFactory.class);

        DaiVinchikDialogHandler service = factory.getObject();
        DaiVinchikDialogHandler service2 = factory.getObject();

        Thread thread = new Thread(service);
        Thread thread2 = new Thread(service2);
        thread.start();
        TimeUnit.SECONDS.sleep(6);
        thread2.start();
        thread.join();
        thread2.join();

//        TaskRunner runner = new TaskRunnerImpl();
//        runner.runTask(service);
//        runner.runTask(service2);
//
//        while (runner.isActive()) {
//            TimeUnit.SECONDS.sleep(5);
//        }

        System.out.println("""
                ++++++++++++++++++++++
                + FINISHED FOR TODAY +
                ++++++++++++++++++++++
                """);

        LOG.info("DAI-VINCHIK-MATCHER 2 -> END");
    }

}
