package com.tminc.taskmanage;

public class TodoItem {
    private String user_name;
    private String todo_text;
    private Boolean isUrgent;
    private String status;

    public TodoItem(String user_name, String todo_text, Boolean isUrgent, String status) {
        this.user_name = user_name;
        this.todo_text = todo_text;
        this.isUrgent = isUrgent;
        this.status = status;
    }

    public String getUserName() {
        return user_name;
    }

    public String getTodoText() {
        return todo_text;
    }

    public Boolean getIsUrgent() {
        return isUrgent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
