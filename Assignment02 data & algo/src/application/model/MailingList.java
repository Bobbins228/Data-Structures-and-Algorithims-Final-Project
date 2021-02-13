package application.model;

import java.util.ArrayList;

public class MailingList {
    private String name;
    private ArrayList<String> userNames;
    private String description;

    public MailingList(String name,ArrayList<String> userNames, String description) {
        this.name = name;
        this.userNames = userNames;
        this.description =description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getUsers() {
        return userNames;
    }

    public void setUsers(ArrayList<String> userNames) {
        this.userNames = userNames;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
