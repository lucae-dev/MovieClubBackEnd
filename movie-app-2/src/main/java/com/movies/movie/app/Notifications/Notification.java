package com.movies.movie.app.Notifications;

import com.movies.movie.app.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User recipient;

    private String triggerUserName;
    private String triggerUserImage;
    private String action; // e.g., "followed you"
    private String attachedImageUrl;

    private LocalDateTime timestamp;

    public Notification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getTriggerUserName() {
        return triggerUserName;
    }

    public void setTriggerUserName(String triggerUserName) {
        this.triggerUserName = triggerUserName;
    }

    public String getTriggerUserImage() {
        return triggerUserImage;
    }

    public void setTriggerUserImage(String triggerUserImage) {
        this.triggerUserImage = triggerUserImage;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAttachedImageUrl() {
        return attachedImageUrl;
    }

    public void setAttachedImageUrl(String attachedImageUrl) {
        this.attachedImageUrl = attachedImageUrl;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

