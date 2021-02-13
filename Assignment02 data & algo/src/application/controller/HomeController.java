package application.controller;
import application.model.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class HomeController implements Initializable {
    protected application.model.UserModel users;
    protected application.model.MailingListModel mailingLists;
    protected MessageModel messages;

    List<String> list = new Stack<String>();
    ObservableList<String> mailingListNames = FXCollections.observableList(list);

    @FXML
    private ChoiceBox<String> choiceMailingList;
    @FXML
    private TextField txtUserName;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtRepeatPassword;
    @FXML
    private TextField loginEmail;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private TextArea feedback;

    /**
     *
     *
     * KEEP THIS STRINGBUILDER IN MIND FOR WHEN DISPLAYING ALL OF THE USERS THAT USE THE MAILING LIST IN ADMIN
     * Its pretty G as you can see it works well with that for loop
     *
     *
     */
    public void handleDisplayButton(ActionEvent e) throws  Exception{
        StringBuilder message = new StringBuilder();
        for(int i = 0; i < mailingLists.numberOfLists(); i++) {
            message.append("Mailing List Name: " + mailingLists.getMailingList(i).getName() + "\n" + "Description: " + mailingLists.getMailingList(i).getDescription() + "\n");
        }
        feedback.setText(message.toString());
    }

    public void handleRegisterButton(ActionEvent e) throws Exception {
        int i = 0;
        Boolean isAdmin = false;
        String userName = txtUserName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        LocalDate date = LocalDate.now();
        String lastPost = "No posts yet";
        int errorCount = 0;
        StringBuilder message = new StringBuilder();

        for(int g = 0; g< users.numberOfUsers(); g++){
            if(txtEmail.getText().equals(users.getUser(g).getEmail())){
                message.append("That email is already in use!\n");
                errorCount++;
            }
        }
        for(int h = 0; h< users.numberOfUsers(); h++){
            if(txtUserName.getText().equals(users.getUser(h).getUserName())){
                message.append("That user name is already in use!\n");
                errorCount++;
            }
        }

        if(txtUserName.getText().length() == 0){
            message.append("User Name must be at least 1 character \n");
            errorCount++;
        }
        if(!txtEmail.getText().contains("@") && !txtEmail.getText().contains(".")){
            message.append("Email must have a valid format (must include '@' & '.') \n");
            errorCount++;
        }
        if(!txtPassword.getText().equals(txtRepeatPassword.getText())){
            message.append("Password and Repeat Password Do Not Match");
            errorCount++;
        }
        if (txtPassword.getText().length() < 7 ){
            message.append("Password mus be longer that 7 characters \n");
            errorCount++;
        }
        if(errorCount == 0){
            if (!isAdmin) {
                if (mailingListChecker(choiceMailingList.getValue())) {
                    String mName = Main.getMailingList().getName();
                    String description = Main.getMailingList().getDescription();
                    MailingList ml = Main.getMailingList();
                    String mailLists = ml.getName();
                    Set<String> mls = new TreeSet<>();
                    mls.add(mailLists);
                    Main.setMailLists(mls);
                    ArrayList<String> userNames;
                    userNames = Main.getMailingList().getUsers();
                    User u1 = new User(email, userName, isAdmin, date, password, mls, lastPost);
                    users.addUser(u1);
                    userNames.add(u1.getUserName());

                    while (i < mailingLists.numberOfLists()) {
                        if(Main.getMailingList().getName().contains(mailingLists.getMailingList(i).getName())) {
                            mailingLists.updateMailingList(i, mName, description, userNames);
                        }
                        i++;
                    }
                    mailingLists.saveMailingLists();
                    mailingLists.loadMailingLists();
                    users.saveUsers();
                    users.loadUsers();
                    feedback.setText(txtUserName.getText() + " Has Successfully Registered");
                }
            }
        }
        else{
            feedback.setText(message.toString());
        }
    }

    public void handleLoginButton(ActionEvent e) throws Exception{
        if(loginUser(loginEmail.getText(), loginPassword.getText())){
                Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("../view/UserPage.fxml"));
                primaryStage.setTitle("User Page");
                primaryStage.setScene(new Scene(root, 1400, 800));
                primaryStage.show();

        }
        else if(loginAdmin(loginEmail.getText(), loginPassword.getText())){
            Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../view/AdminPage.fxml"));
            primaryStage.setTitle("Admin Page");
            primaryStage.setScene(new Scene(root, 750, 800));
            primaryStage.show();
        }
        else {
            feedback.setText("Invalid Email or Password!");
        }
    }

    private boolean loginUser(String email, String password) {
        Stack<User> users = null;
        XStream xstream = new XStream(new DomDriver());
        try {
            ObjectInputStream is = xstream.createObjectInputStream(new FileReader("users.xml"));
            users = (Stack<User>) is.readObject();
            is.close();
        }
        catch(FileNotFoundException e) {
            users =  new Stack<User>();
            feedback.setText("File not located");
            return false;

        }
        catch (Exception e) {
            feedback.setText("Error accessing File");
            return false;
        }

        for (User user: users) {
            if(user.getEmail().equals(email) && user.getPassword().equals(password)) {
                if(!user.getAdmin()) {
                    Main.setUser(user);
                    return true;
                }
            }
        }
        return false;
    }
    private boolean loginAdmin(String email, String password) {
        Stack<User> users = null;
        XStream xstream = new XStream(new DomDriver());
        try {
            ObjectInputStream is = xstream.createObjectInputStream(new FileReader("users.xml"));
            users = (Stack<User>) is.readObject();
            is.close();
        }
        catch(FileNotFoundException e) {
            users =  new Stack<User>();
            feedback.setText("File not located");
            return false;

        }
        catch (Exception e) {
            feedback.setText("Error accessing File");
            return false;
        }

        for (User user: users) {
            if(user.getEmail().equals(email) && user.getPassword().equals(password)) {
                if(user.getAdmin()) {
                    Main.setUser(user);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean mailingListChecker(String mailingListName) {
        Stack<MailingList> mailingLists = null;
        XStream xstream = new XStream(new DomDriver());
        try {
            ObjectInputStream is = xstream.createObjectInputStream(new FileReader("mailingLists.xml"));
            mailingLists = (Stack<MailingList>) is.readObject();
            is.close();
        }
        catch(FileNotFoundException e) {
            mailingLists =  new Stack<MailingList>();
            feedback.setText("File not located");
            return false;

        }
        catch (Exception e) {
            feedback.setText("Error accessing File");
            return false;
        }

        for (MailingList mailingList: mailingLists) {
            if(mailingList.getName().equals(mailingListName)) {
                Main.setMailingList(mailingList);
                return true;
            }
        }
        return false;
    }




    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources){
        messages = new MessageModel();
        users = new UserModel();
        mailingLists = new MailingListModel();
        try {
            users.loadUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mailingLists.loadMailingLists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            messages.loadMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0; i < mailingLists.numberOfLists(); i++){
            mailingListNames.add(mailingLists.getMailingList(i).getName());
        }
        choiceMailingList.setItems(mailingListNames);
        choiceMailingList.setValue(mailingLists.getMailingList(0).getName());


    }
}
