package ru.yandex.javacource.kulpinov.schedule.manager;

public class Managers {

    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){ return new InMemoryHistoryManager();
    }
}
