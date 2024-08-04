package ru.yandex.javacource.kulpinov.schedule.http;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.Status;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicAdapter extends TypeAdapter<Epic> {
    @Override
    public void write(JsonWriter out, Epic epic) throws IOException {
        out.beginObject();
        out.name("name").value(epic.getName());
        out.name("description").value(epic.getDescription());
        out.name("id").value(epic.getId());
        out.name("status").value(epic.getStatus().toString());
        out.name("duration").value(epic.getDuration() != null ? epic.getDuration().toString() : null);
        out.name("startTime").value(epic.getStartTime() != null ? epic.getStartTime().toString() : null);
        out.name("endTime").value(epic.getEndTime() != null ? epic.getEndTime().toString() : null);
        out.name("subTasks").beginArray();
        for (Integer subTaskId : epic.getSubtasks()) {
            out.value(subTaskId);
        }
        out.endArray();
        out.endObject();
    }

    public Epic read(JsonReader in) throws IOException {
        in.beginObject();
        String name = "";
        String description = "";
        int id = 0;
        Status status = Status.NEW;
        Duration duration = null;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        ArrayList<Integer> subTasks = new ArrayList<>();

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "name":
                    name = in.nextString();
                    break;
                case "description":
                    description = in.nextString();
                    break;
                case "id":
                    id = in.nextInt();
                    break;
                case "status":
                    status = Status.valueOf(in.nextString());
                    break;
                case "duration":
                    String durationString = in.nextString();
                    duration = durationString != null ? Duration.parse(durationString) : null;
                    break;
                case "startTime":
                    String startTimeString = in.nextString();
                    startTime = startTimeString != null ? LocalDateTime.parse(startTimeString) : null;
                    break;
                case "endTime":
                    String endTimeString = in.nextString();
                    endTime = endTimeString != null ? LocalDateTime.parse(endTimeString) : null;
                    break;
                case "subTasks":
                    in.beginArray();
                    while (in.hasNext()) {
                        subTasks.add(in.nextInt());
                    }
                    in.endArray();
                    break;
            }
        }
        in.endObject();
        Epic epic = new Epic(name, description, status, id, duration, startTime, endTime);
        epic.setSubtaskIds(subTasks);
        return epic;
    }

}

