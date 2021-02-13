package application.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

public class User {
    private String email;
    private String userName;
    private Boolean isAdmin;
    private LocalDate signUpDate;
    private String lastPost;
    private String password;
    private Set<String> mailingLists;
    //private MailingList mailingList;

    public User(String email, String userName, Boolean isAdmin, LocalDate signUpDate, String password, Set<String> mailingLists, String lastPost /*MailingList mailingList*/) {
        this.email = email;
        this.userName = userName;
        this.isAdmin = isAdmin;
        this.signUpDate = signUpDate;
        this.password = password;
        this.mailingLists = mailingLists;
        this.lastPost = lastPost;
        //this.mailingList = mailingList;
    }

    public User(String email, String userName, Boolean isAdmin, LocalDate signUpDate, String password) {
        this.email = email;
        this.userName = userName;
        this.isAdmin = isAdmin;
        this.signUpDate = signUpDate;
        this.password = password;
    }
    /*public MailingList getGroup() {
        return mailingList;
    }*/

    public Boolean getAdmin() {
        return isAdmin;
    }

    public String getLastPost() {
        return lastPost;
    }

    public void setLastPost(String lastPost) {
        this.lastPost = lastPost;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDate getSignUpDate() {
        return signUpDate;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public void setSignUpDate(LocalDate signUpDate) {
        this.signUpDate = signUpDate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getMailingLists() {
        return mailingLists;
    }

    public void setMailingLists(Set<String> mailingLists) {
        this.mailingLists = mailingLists;
    }

    @Override
    public String toString() {
        return userName + " " + "Last posted: " + lastPost;
    }
}
