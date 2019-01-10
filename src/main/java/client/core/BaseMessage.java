package client.core;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.util.Date;

public abstract class BaseMessage {
    private String from;
    private String subject;
    private String message;
    private File[] attachment;
    private Date date;

    BaseMessage(@Nullable String subject, @Nullable String message) {
        this.subject = subject;
        this.message = message;
    }

    BaseMessage(@Nullable String message) {
        this.message = message;
    }

    BaseMessage(@NotNull String from, @Nullable String subject, @Nullable String message, @Nullable File[] attachment) {
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.attachment = attachment;
    }

    public Date getDate() {
        return date;
    }
    void setDate(Date date) {
        this.date = date;
    }

    String getFrom() {
        return from;
    }
    void setFrom(String from) {
        this.from = from;
    }

    String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    File[] getAttachment() {
        return attachment;
    }
    void setAttachment(File[] attachment) {
        this.attachment = attachment;
    }
}
