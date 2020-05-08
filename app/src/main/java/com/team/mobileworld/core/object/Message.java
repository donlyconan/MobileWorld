package com.team.mobileworld.core.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Message {
    @SerializedName("billid") @Expose
    private int id;
    @SerializedName("title") @Expose
    private String title;
    @SerializedName("content") @Expose
    private String content;
    @SerializedName("createdAt") @Expose
    private Date date;
    @SerializedName("status") @Expose
    private int status;

    public Message() {
    }

    public Message(String title, String content, Date date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public Message(String title, String content, Date date, int status) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Message{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Message(int status) {
        this.status = status;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
