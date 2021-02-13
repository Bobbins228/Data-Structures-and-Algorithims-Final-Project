package application.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Set;

public class Main extends Application {
    public static MailingList mailingList;
    public static User user;
    public static Set<String> mailLists;
    public static ArrayList<String> userNames;

    public static ArrayList<String> getUserNames() {
        return userNames;
    }

    public static void setUserNames(ArrayList<String> userNames) {
        Main.userNames = userNames;
    }

    public static Set<String> getMailLists() {
        return mailLists;
    }

    public static void setMailLists(Set<String> mailLists) {
        Main.mailLists = mailLists;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Main.user = user;
    }


    public static MailingList getMailingList() {
        return mailingList;
    }

    public static void setMailingList(MailingList mailingList) {
        Main.mailingList = mailingList;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/Home.fxml"));
        primaryStage.setTitle("Home");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
