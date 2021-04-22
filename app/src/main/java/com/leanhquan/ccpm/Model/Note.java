package com.leanhquan.ccpm.Model;

public class Note {

    private String  name,
                    category,
                    priority,
                    status,
                    plandate,
                    createddate;

    public Note() {
    }



    public Note(String name, String category, String priority, String status, String plandate, String createddate) {
        this.name = name;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.plandate = plandate;
        this.createddate = createddate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlandate() {
        return plandate;
    }

    public void setPlandate(String plandate) {
        this.plandate = plandate;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }
}
