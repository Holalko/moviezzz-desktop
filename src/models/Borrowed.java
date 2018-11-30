package models;

public class Borrowed {

    private Long id;
    private String dueDate;

    public Borrowed() {
    }

    public Borrowed(Long id, String dueDate) {
        this.id = id;
        this.dueDate = dueDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
