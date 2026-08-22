package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> mondayResult = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondayResult.size());

        //Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> tuesdayResult = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(0, tuesdayResult.size());
    }

    @Test
    void shouldAddTwoTrainingSessionInSameTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession firstTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        Group groupAdult = new Group("Акробатика для ADULT", Age.ADULT, 60);
        Coach coachAdult = new Coach("Иванов", "Николай", "Сергеевич");
        TrainingSession secondTrainingSession = new TrainingSession(groupAdult, coachAdult,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(firstTrainingSession);
        timetable.addNewTrainingSession(secondTrainingSession);

        Map<TimeOfDay, List<TrainingSession>> mondayResult = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondayResult.size());

        List<TrainingSession> trainingSessions = mondayResult.get(new TimeOfDay(13, 0));
        Assertions.assertEquals(2, trainingSessions.size());
    }

    @Test
    void shouldNotAddTwoTrainingSessionInSameTimeAndSameTraining() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession firstTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        Group groupAdult = new Group("Акробатика для ADULT", Age.ADULT, 60);
        Coach coachAdult = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession secondTrainingSession = new TrainingSession(groupAdult, coachAdult,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(firstTrainingSession);
        timetable.addNewTrainingSession(secondTrainingSession);

        Map<TimeOfDay, List<TrainingSession>> mondayResult = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondayResult.size());

        List<TrainingSession> trainingSessions = mondayResult.get(new TimeOfDay(13, 0));
        Assertions.assertEquals(1, trainingSessions.size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> mondayResult = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondayResult.size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        Map<TimeOfDay, List<TrainingSession>> thursdayResult = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdayResult.size());
        boolean isFirstEntry = true;
        for (TimeOfDay timeOfDay : thursdayResult.keySet()) {
            if (isFirstEntry) {
                isFirstEntry = false;
                Assertions.assertEquals(13, timeOfDay.getHours());
            } else {
                Assertions.assertEquals(20, timeOfDay.getHours());
            }
        }

        // Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> tuesdayResult = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertEquals(0, tuesdayResult.size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> thirteenTrainingSessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        Assertions.assertEquals(1, thirteenTrainingSessions.size());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> fourteenTrainingSessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));
        Assertions.assertEquals(0, fourteenTrainingSessions.size());
    }

    @Test
    void shouldReturn3TrainingsAndOneCoach() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Акробатика для ADULT", Age.ADULT, 60);
        Group group3 = new Group("Акробатика", Age.ADULT, 60);

        TrainingSession firstTrainingSession = new TrainingSession(group1, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession secondTrainingSession = new TrainingSession(group2, coach,
                DayOfWeek.MONDAY, new TimeOfDay(11, 0));
        TrainingSession trainingSession = new TrainingSession(group3, coach,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0));


        timetable.addNewTrainingSession(firstTrainingSession);
        timetable.addNewTrainingSession(secondTrainingSession);
        timetable.addNewTrainingSession(trainingSession);

        List<CounterOfTrainings> counterOfTrainings = timetable.getCountByCoaches();

        Assertions.assertEquals(1, counterOfTrainings.size());
        Assertions.assertEquals(3, counterOfTrainings.getFirst().getTrainingAmount());
    }

    @Test
    void getCountByCoachesShouldReturn3TrainingsAndOneCoach() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Акробатика для ADULT", Age.ADULT, 60);
        Group group3 = new Group("Акробатика", Age.ADULT, 60);

        TrainingSession firstTrainingSession = new TrainingSession(group1, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession secondTrainingSession = new TrainingSession(group2, coach,
                DayOfWeek.MONDAY, new TimeOfDay(11, 0));
        TrainingSession trainingSession = new TrainingSession(group3, coach,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0));


        timetable.addNewTrainingSession(firstTrainingSession);
        timetable.addNewTrainingSession(secondTrainingSession);
        timetable.addNewTrainingSession(trainingSession);

        List<CounterOfTrainings> counterOfTrainings = timetable.getCountByCoaches();

        Assertions.assertEquals(1, counterOfTrainings.size());
        Assertions.assertEquals(3, counterOfTrainings.getFirst().getTrainingAmount());
    }

    @Test
    void getCountByCoachesShouldReturn3CoachDecreaseByTrainsAmount() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);


        Coach coach2 = new Coach("Иванов", "Иван", "Иванович");
        Group group2 = new Group("Акробатика для ADULT", Age.ADULT, 60);
        Group group3 = new Group("Акробатика", Age.ADULT, 60);


        Coach coach3 = new Coach("Петров", "Петр", "Петрович");
        Group group4 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group5 = new Group("Акробатика для ADULT", Age.ADULT, 60);
        Group group6 = new Group("Акробатика для детей", Age.CHILD, 60);

        TrainingSession trainingSession1 = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        TrainingSession trainingSession2 = new TrainingSession(group2, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(11, 0));
        TrainingSession trainingSession3 = new TrainingSession(group3, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0));

        TrainingSession trainingSession4 = new TrainingSession(group4, coach3,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession trainingSession5 = new TrainingSession(group5, coach3,
                DayOfWeek.MONDAY, new TimeOfDay(11, 0));
        TrainingSession trainingSession6 = new TrainingSession(group6, coach3,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0));


        timetable.addNewTrainingSession(trainingSession1);
        timetable.addNewTrainingSession(trainingSession2);
        timetable.addNewTrainingSession(trainingSession3);
        timetable.addNewTrainingSession(trainingSession4);
        timetable.addNewTrainingSession(trainingSession5);
        timetable.addNewTrainingSession(trainingSession6);

        List<CounterOfTrainings> counterOfTrainings = timetable.getCountByCoaches();

        Assertions.assertEquals(3, counterOfTrainings.size());
        Assertions.assertEquals(3, counterOfTrainings.getFirst().getTrainingAmount());
        Assertions.assertEquals(1, counterOfTrainings.getLast().getTrainingAmount());
    }

    @Test
    void getCountByCoachesShouldReturn0CoachAnd0Trainings() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> counterOfTrainings = timetable.getCountByCoaches();

        Assertions.assertEquals(0, counterOfTrainings.size());
    }

}
