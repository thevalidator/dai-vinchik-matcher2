package ru.thevalidator.daivinchikmatcher2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.thevalidator.daivinchikmatcher2.config.DaiVinchikDialogHandlerFactory;
import ru.thevalidator.daivinchikmatcher2.config.SpringJavaConfig;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.poll.DaiVinchikDialogHandler;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.poll.Statistic;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        LOG.info("DAI-VINCHIK-MATCHER 2 -> START");

        var context = new AnnotationConfigApplicationContext(SpringJavaConfig.class);
        DaiVinchikDialogHandlerFactory factory = context.getBean(DaiVinchikDialogHandlerFactory.class);

        List<Thread> threads = new ArrayList<>();
        int counter = 0;
        while (factory.hasObject()) {
            DaiVinchikDialogHandler service = factory.getObject();
            Thread thread = new Thread(service, "account - " + ++counter);
            threads.add(thread);
        }

        for (Thread thread: threads) {
            thread.start();
        }

        for (Thread thread: threads) {
            thread.join();
        }


//        TaskRunner runner = new TaskRunnerImpl();
//        runner.runTask(service);
//        runner.runTask(service2);
//
//        while (runner.isActive()) {
//            TimeUnit.SECONDS.sleep(5);
//        }


        LOG.info("DAI-VINCHIK-MATCHER 2 -> END");
        var stats = Statistic.getGlobalStatistic();
        System.out.println(">>>>>>>   STATS   <<<<<<<");
        for (Statistic s: stats) {
            System.out.printf("[%s] (L_%03d/D_%03d) \n",
                    s.getName(), s.getLikes(), s.getDislikes());
        }
        System.out.print("""
                +++++++++++++++++++++++++
                + FINISHED SUCCESSFULLY +
                +++++++++++++++++++++++++
                """);

    }

}
