package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();

        Comparator<TimeOfDay> comparator = (o1, o2) -> {
            int resultHoursCompare = o1.getHours() - o2.getHours();
            if (resultHoursCompare != 0) {
                return resultHoursCompare;
            } else {
                return o1.getMinutes() - o2.getMinutes();
            }
        };

        for (DayOfWeek value : DayOfWeek.values()) {
            timetable.put(value, new TreeMap<>(comparator));
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayTimeTable = timetable.get(trainingSession.getDayOfWeek());
        List<TrainingSession> dayAndTimeTimeTable = dayTimeTable.getOrDefault(trainingSession.getTimeOfDay(),
                new ArrayList<>());

        boolean hasCoachTraining = false;

        for (TrainingSession session : dayAndTimeTimeTable) {
            if (session.getCoach().equals(trainingSession.getCoach())) {
                hasCoachTraining = true;
            }
        }

        if (!hasCoachTraining) {
            dayAndTimeTimeTable.add(trainingSession);
        }

        dayTimeTable.put(trainingSession.getTimeOfDay(), dayAndTimeTimeTable);
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.get(dayOfWeek).getOrDefault(timeOfDay, new ArrayList<>());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachTrainings = countCoachTrainings();
        return sortCoachTrainings(coachTrainings);
    }

    private Map<Coach, Integer> countCoachTrainings() {
        Map<Coach, Integer> coachTrainings = new HashMap<>();
        for (TreeMap<TimeOfDay, List<TrainingSession>> trainingsInDay : timetable.values()) {
            for (List<TrainingSession> trainingsInHour : trainingsInDay.values()) {
                for (TrainingSession trainingSession : trainingsInHour) {
                    int trainingAmount = coachTrainings.getOrDefault(trainingSession.getCoach(), 0);
                    trainingAmount++;
                    coachTrainings.put(trainingSession.getCoach(), trainingAmount);
                }
            }
        }
        return coachTrainings;
    }

    private List<CounterOfTrainings> sortCoachTrainings(Map<Coach, Integer> coachTrainings) {
        List<CounterOfTrainings> list = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachTrainings.entrySet()) {
            list.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
        Comparator<CounterOfTrainings> comparator =
                (o1, o2) -> o2.getTrainingAmount() - o1.getTrainingAmount();

        list.sort(comparator);

        return list;
    }
}
