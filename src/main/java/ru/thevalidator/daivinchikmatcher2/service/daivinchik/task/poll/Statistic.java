package ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.poll;

class Statistic {

    private int likes;
    private int dislikes;

    public int getLikes() {
        return likes;
    }

    public void increaseLikes() {
        likes++;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void increaseDislikes() {
        dislikes++;
    }

}
