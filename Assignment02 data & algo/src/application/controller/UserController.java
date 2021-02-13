package application.controller;

import application.model.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class UserController implements Initializable {
    protected MessageModel messages;
    protected MailingListModel mailingLists;
    protected UserModel users;
    //USER PAGE
    @FXML
    private TableView myTable;
    @FXML
    private TableColumn<Message, User> txtSender;
    @FXML
    private TableColumn<Message, String> txtTitle;
    @FXML
    private TableColumn<Message, LocalDate> txtDate;
    @FXML
    private TableColumn<Message, String> txtPriority;
    @FXML
    private TableColumn<Message, String> txtMailList;
    @FXML
    private TableColumn<Message, String> txtMessageBody;
    @FXML
    private TextField filterField;
    @FXML
    private ChoiceBox<String> choiceMailList = new ChoiceBox<String>(FXCollections.observableArrayList());
    @FXML
    private TextArea feedback;

    //MESSAGE PAGE
    @FXML
    private TextField txtMessageTitle;
    @FXML
    private TextField txtBody;
    @FXML
    private ChoiceBox<String> choicePriority = new ChoiceBox<String>(FXCollections.observableArrayList("Low", "Medium", "High"));
    ObservableList<String> priorities = FXCollections.observableArrayList("Low", "Medium", "High");
    public void sendAll(ActionEvent e) throws Exception{
        Set<String> mNames = new HashSet<>();
        String title = txtMessageTitle.getText();
        LocalDate date = LocalDate.now();
        String priority = choicePriority.getValue();
        String body = txtBody.getText();
        int errorCount = 0;
        StringBuilder m = new StringBuilder();
        if(txtMessageTitle.getText().length() == 0){
            m.append("Please enter a title longer than 0 characters\n");
            errorCount++;
        }
        if (txtBody.getText().length() == 0) {
            m.append("Invalid message body it is not greater than 0 characters \n");
            errorCount++;
        }
        if(errorCount == 0) {
            mNames = Main.getUser().getMailingLists();
            User user = Main.getUser();
            String email = Main.getUser().getEmail();
            String name = Main.getUser().getUserName();
            Boolean userType = Main.getUser().getAdmin();
            String lastPost = LocalDate.now().toString();
            LocalDate dateSignup = Main.getUser().getSignUpDate();
            String password = Main.getUser().getPassword();
            Set<String> mailingLists = Main.getUser().getMailingLists();
            int i = 0;
            while (i < users.numberOfUsers()) {
                if (Main.getUser().getUserName().equals(users.getUser(i).getUserName())) {
                    users.updateUser(i, name, email, userType, dateSignup, lastPost, password, mailingLists);
                    Main.setUser(users.getUser(i));
                    users.saveUsers();
                    messages.saveMessages();
                    messages.loadMessage();
                    users.loadUsers();
                }
                i++;
            }
            List<String> list = new ArrayList<>(mNames);
            User us = Main.getUser();
            for (String mName : list) {
                Message message = new Message(us, title, date, priority, mName, body);
                messages.addMessage(message);
            }
            messages.saveMessages();
            messages.loadMessage();
            feedback.setText("Message Sent Successfully!");
        }
        else{
            feedback.setText(m.toString());
        }
    }
    public void sendMessage(ActionEvent e) throws Exception{
        String mName = "";
        String title = txtMessageTitle.getText();
        LocalDate date = LocalDate.now();
        String priority = choicePriority.getValue();
        String body = txtBody.getText();
        int errorCount = 0;
        StringBuilder m = new StringBuilder();
        if (!Main.getUser().getMailingLists().toString().contains(choiceMailList.getValue())) {
            errorCount++;
            m.append("You are not a part of that Mailing List! \n");
        }
        if(txtMessageTitle.getText().length() == 0){
            m.append("Please enter a title longer than 0 characters\n");
            errorCount++;
        }
        if (txtBody.getText().length() == 0) {
            m.append("Invalid message body it is not greater than 0 characters \n");
            errorCount++;
        }
        if(errorCount == 0) {
            int g = 0;
            while (g < mailingLists.numberOfLists()) {
                if (Main.getUser().getMailingLists().toString().contains(choiceMailList.getValue())) {
                    mName = choiceMailList.getValue();
                }
                g++;
            }
                User user = Main.getUser();
                String email = Main.getUser().getEmail();
                String name = Main.getUser().getUserName();
                Boolean userType = Main.getUser().getAdmin();
                String lastPost = LocalDate.now().toString();
                LocalDate dateSignup = Main.getUser().getSignUpDate();
                String password = Main.getUser().getPassword();
                Set<String> mailingLists = Main.getUser().getMailingLists();
                int i = 0;
                while (i < users.numberOfUsers()) {
                    if (Main.getUser().getUserName().equals(users.getUser(i).getUserName())) {
                        users.updateUser(i, name, email, userType, dateSignup, lastPost, password, mailingLists);
                        Main.setUser(users.getUser(i));
                        users.saveUsers();
                        users.loadUsers();
                        messages.saveMessages();
                        messages.loadMessage();
                    }
                    i++;
                }
                User us = Main.getUser();
                Message message = new Message(us, title, date, priority, mName, body);
                for(int v = 0; v < users.numberOfUsers(); v++){
                    if(Main.getUser().getUserName().equals(messages.getMessage(v).getUser().getUserName())){
                        String mailingList = messages.getMessage(v).getMailingList();
                        messages.updateMessage(v,us,title,date,priority,mailingList,body);
                        messages.saveMessages();
                    }
                }
                messages.addMessage(message);
                messages.saveMessages();
                messages.loadMessage();
                feedback.setText("Message Sent Successfully!");
        }
        else{
            feedback.setText(m.toString());
        }
    }

    public void goBack(ActionEvent e) throws Exception{
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../view/UserPage.fxml"));
        primaryStage.setTitle("User Page");
        primaryStage.setScene(new Scene(root, 1400, 800));
        primaryStage.show();
    }

    public void handleNewMessage(ActionEvent e) throws Exception{
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../view/NewMessage.fxml"));
        primaryStage.setTitle("New Message");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }

    public void handleReadButton(ActionEvent e) throws Exception {
            messages.loadMessage();
            users.loadUsers();
            Stack<Message> ms1 = new Stack<>();
            for(int i = 0; i < messages.numberOfMessages(); i++) {
                if (Main.getUser().getMailingLists().contains(messages.getMessage(i).getMailingList())) {
                    ms1.add(messages.getMessage(i));
                }
            }
        ObservableList<Message> data = FXCollections.observableArrayList(ms1);
        txtTitle.setCellValueFactory(new PropertyValueFactory<Message, String>("messageTitle"));
        txtMessageBody.setCellValueFactory(new PropertyValueFactory<Message, String>("messageBody"));
        txtSender.setCellValueFactory(new PropertyValueFactory<Message, User>("user"));
        txtDate.setCellValueFactory(new PropertyValueFactory<Message, LocalDate>("dateTime"));
        txtPriority.setCellValueFactory(new PropertyValueFactory<Message, String>("Priority"));
        txtMailList.setCellValueFactory(new PropertyValueFactory<Message, String>("mailingList"));
        myTable.setItems(data);



            //THIS CODE IS FOR THE SEARCH BAR THE CODE ABOVE FILLS THE TABLE
            FilteredList<Message> filteredData = new FilteredList<>(data, p -> true);
            filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(message -> {
                    // If filter text is empty, display all messages
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (message.getUser().getUserName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (message.getMessageTitle().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (message.getDateTime().toString().contains(lowerCaseFilter)) {
                        return true;
                    } else if (message.getPriority().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (message.getMailingList().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (message.getMessageBody().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;

                });
            });
            SortedList<Message> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(myTable.comparatorProperty());
            myTable.setItems(sortedData);
    }
    public void handleClearButton(ActionEvent e) throws Exception{
        myTable.setItems(null);
    }

    public void handleAddMailList(ActionEvent e) throws Exception{
        String email = Main.getUser().getEmail();
        String name = Main.getUser().getUserName();
        Boolean isAdmin = Main.getUser().getAdmin();
        String lastPost = Main.getUser().getLastPost();
        LocalDate dateSignup = Main.getUser().getSignUpDate();
        String password = Main.getUser().getPassword();
           if (Main.getUser().getMailingLists().toString().contains(choiceMailList.getValue())){
                feedback.setText("You are already a part of that Mailing List!");
            }
        else if(mListChecker(choiceMailList.getValue())) {
            int i = 0;
            while(i < users.numberOfUsers()) {
                if (Main.getUser().getUserName().equals(users.getUser(i).getUserName())) {
                    Set<String> mls = new TreeSet<>();
                    mls = Main.getUser().getMailingLists();
                    mls.add(choiceMailList.getValue());
                    Main.setMailLists(mls);
                    users.updateUser(i,name, email,isAdmin,dateSignup,lastPost,password,Main.getMailLists());
                    feedback.setText("Successfully added " + users.getUser(i).getUserName() + " to the " + choiceMailList.getValue() + " Mailing List");
                    users.saveUsers();
                    users.loadUsers();
                    myTable.setItems(null);

                }
                i++;
            }
        }
    }

    private boolean mListChecker(String mailListName) {
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
            if(mailingList.getName().equals(mailListName)) {
                Main.setMailingList(mailingList);
                return true;
            }
        }
        return false;
    }


    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        messages = new MessageModel();
        mailingLists = new MailingListModel();
        users = new UserModel();
        try {
            messages.loadMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mailingLists.loadMailingLists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            users.loadUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> list = new ArrayList<String>();
        ObservableList<String> mailLists = FXCollections.observableList(list);
        for(int i = 0; i < mailingLists.numberOfLists(); i++) {
            mailLists.add(mailingLists.getMailingList(i).getName());
        }

        choicePriority.setItems(priorities);
        choicePriority.setValue("Low");
        choiceMailList.setItems(mailLists);
        choiceMailList.setValue(mailingLists.getMailingList(0).getName());

    }
}
