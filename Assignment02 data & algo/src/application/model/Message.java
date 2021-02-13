package application.model;

import java.time.LocalDate;

public class Message {
    private User user;
    private String messageTitle;
    private LocalDate dateTime;
    private String priority;
    private String mailingList;
    private String messageBody;

    public Message(User user, String messageTitle, LocalDate dateTime, String priority, String mailingList, String messageBody) {
        this.user = user;
        this.messageTitle = messageTitle;
        this.dateTime = dateTime;
        this.priority = priority;
        this.mailingList = mailingList;
        this.messageBody = messageBody;
    }

    public User getUser() {
        return user;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public String getPriority() {
        return priority;
    }

    public String getMailingList() {
        return mailingList;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setMailingList(String mailingList) {
        this.mailingList = mailingList;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    @Override
    public String toString() {
        return "Message{" +
                "user=" + user.getUserName() +
                ", messageTitle='" + messageTitle + '\'' +
                ", dateTime=" + dateTime +
                ", priority='" + priority + '\'' +
                ", MailingList='" + mailingList + '\'' +
                ", messageBody='" + messageBody + '\'' +
                '}';
    }
}
