package ru.yandex.javacource.kulpinov.schedule.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    public void Managers(){
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(taskManager, "Не возвращает экземпляр");
        assertNotNull(historyManager, "Не возвращает экземпляр");

    }

}