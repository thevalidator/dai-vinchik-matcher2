package ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.impl;

import ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.Task;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.TaskRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRunnerImpl implements TaskRunner {

    private static final int MAX_THREADS = 10;
    public final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

    @Override
    public void runTask(Task task) {
        executor.execute(task);
    }

    @Override
    public boolean isActive() {
        //@TODO: implement ability to check if all threads did their job
        return executor.isTerminated();
    }

}
