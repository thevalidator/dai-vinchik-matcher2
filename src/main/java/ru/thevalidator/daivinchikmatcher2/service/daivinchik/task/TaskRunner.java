package ru.thevalidator.daivinchikmatcher2.service.daivinchik.task;

public interface TaskRunner {

    void runTask (Task task);

    boolean isActive();

}
