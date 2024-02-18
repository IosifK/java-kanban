import java.util.Objects;

class Task {
    private String name;
    private String description;
    private int id;
    private Status status;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public Status getStatus() {

        return status;
    }

    public void setStatus(Status status) {

        this.status = status;
    }

    public void setId(int id) {

        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }

    @Override
    public String toString() {
        return  "Task{" +
                "taskName='" + name + '\'' +
                ", taskDescription='" + description + '\'' +
                ", taskStatus=" + status +
                ", taskID=" + id +
                '}';
    }


}
