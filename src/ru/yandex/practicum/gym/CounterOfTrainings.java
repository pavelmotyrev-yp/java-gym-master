package ru.yandex.practicum.gym;

public class CounterOfTrainings {
    private Coach coach;
    private int trainingAmount;

    public CounterOfTrainings(Coach coach, int trainingAmount) {
        this.coach = coach;
        this.trainingAmount = trainingAmount;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getTrainingAmount() {
        return trainingAmount;
    }
}
