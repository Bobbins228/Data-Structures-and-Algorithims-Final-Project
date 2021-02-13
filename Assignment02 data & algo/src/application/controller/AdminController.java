package application.controller;

import application.model.*;
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
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class AdminController implements Initializable {
    protected MessageModel messages;
    protected MailingListModel mailingLists;
    protected UserModel users;
    @FXML
    private TextField txtIndex;
    @FXML
    private TextField txtMailName;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextArea feedback;
    @FXML
    private ChoiceBox<String> choiceUser = new ChoiceBox<String>(FXCollections.observableArrayList());
    //Messages
    @FXML
    private TableView<Message> myTable = new TableView<>();
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


    public void handleDeleteMessages(ActionEvent e) throws Exception{
                for (int i = 0; i < messages.numberOfMessages(); i++) {
                    if(myTable.getSelectionModel().getSelectedItems().toString().contains(messages.getMessage(i).getMessageTitle())){
                        messages.deleteMessage(i);
                    }
                }
                messages.saveMessages();
                messages.loadMessage();
    }
    public void handleGoBack(ActionEvent e) throws  Exception{
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../view/AdminPage.fxml"));
        primaryStage.setTitle("Admin Page");
        primaryStage.setScene(new Scene(root, 750, 800));
        primaryStage.show();
    }
    public void handleMessagePage(ActionEvent e) throws Exception{
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../view/AdminMessages.fxml"));
        primaryStage.setTitle("Message Management Page");
        primaryStage.setScene(new Scene(root, 1400, 800));
        primaryStage.show();
    }

    public void deleteUserBttn(ActionEvent e) throws Exception{
        String name = "";
        String names;
        String desc = "";
        StringBuilder m = new StringBuilder();
        int errorCount = 0;
        String index1 = txtIndex.getText();
        if(index1.length() == 0){
            m.append("please enter a valid index Number!");
            errorCount++;
        }
        ArrayList<String> mLists;
        if(errorCount == 0){
            int index = Integer.parseInt(index1);
            for(int i = 0; i < mailingLists.numberOfLists(); i++){
                if(mailingLists.getMailingList(i).getUsers().contains(users.getUser(index).getUserName())){
                    mailingLists.getMailingList(i).getUsers().remove(users.getUser(i).getUserName());
                    mLists = mailingLists.getMailingList(i).getUsers();
                    mailingLists.updateMailingList(i,name,desc,mLists);
                }
            }
            users.deleteUser(index);
            mailingLists.saveMailingLists();
            users.saveUsers();
            feedback.setText("User successfully deleted");
        }
        else{
            feedback.setText(m.toString());
        }
    }

    public void handleDisplayUsers(ActionEvent e) throws Exception{
        StringBuilder m = new StringBuilder();
        for(int i = 0; i < users.numberOfUsers(); i++){
            m.append("Index: " + i + "\n User Name: " + users.getUser(i).getUserName() + "\n MailingLists: " + users.getUser(i).getMailingLists() + "\n");
        }
        feedback.setText(m.toString());
    }

    public void handleReadButton(ActionEvent e) throws Exception {
        messages.loadMessage();
        Stack<Message> ms1 = new Stack<>();
        for(int i = 0; i < messages.numberOfMessages(); i++) {
            if (messages.getMessage(i).getMailingList().contains(choiceMailList.getValue())) {
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

        //Search
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


    public void handleAddBttn(ActionEvent e) throws Exception{
        mailingLists.loadMailingLists();
        users.loadUsers();
        int g = 0;
        int i = 0;
        String name = txtMailName.getText();
        String desc = txtDescription.getText();
        int errorCount = 0;
        StringBuilder m = new StringBuilder();

        while(i < mailingLists.numberOfLists()){
            if(mailingLists.getMailingList(i).getName().equals(txtMailName.getText()) || txtMailName.getText().length() == 0){
                m.append("ERROR there is already a Mailing List with that Name or the name you have entered is less than 1 characters long try a different one! \n");
                errorCount++;
            }
            i++;
        }
        if(txtDescription.getText().length() < 10){
            m.append("Description must be at least 10 character long");
            errorCount++;
        }
        if (errorCount == 0){
            ArrayList<String> userNames = new ArrayList<>();
            userNames.add(choiceUser.getValue());
            MailingList mailingList = new MailingList(name, userNames, desc );
            Set<String> mls;

            mailingLists.addMailingList(mailingList);
            while (g < users.numberOfUsers()){
                if(users.getUser(g).getUserName().equals(choiceUser.getValue())){
                    mls = users.getUser(g).getMailingLists();
                    mls.add(txtMailName.getText());
                    Main.setMailLists(mls);
                    users.updateUser(g,users.getUser(g).getUserName(),users.getUser(g).getEmail(),users.getUser(g).getAdmin(),users.getUser(g).getSignUpDate(),users.getUser(g).getLastPost(),users.getUser(g).getPassword(),mls);
                }
                g++;
            }
            users.saveUsers();
            users.loadUsers();
            mailingLists.saveMailingLists();
            mailingLists.loadMailingLists();
            feedback.setText("Successfully added MailingList!");
        }
        else{
            feedback.setText(m.toString());
        }
    }
    public void handleDisplayBttn(ActionEvent e) throws Exception{
        StringBuilder m = new StringBuilder();
        for(int i = 0; i < mailingLists.numberOfLists(); i++){
            m.append("Index: " + i + "\n MailingList Name: " + mailingLists.getMailingList(i).getName() + "\n Users: " + mailingLists.getMailingList(i).getUsers() + "\n Description: " + mailingLists.getMailingList(i).getDescription() + "\n");
        }
        feedback.setText(m.toString());
    }

    public void deleteMailList(ActionEvent e) throws Exception{
        users.loadUsers();
        int errorCount = 0;
        String txtIndexs = txtIndex.getText();
        Set<String> mls = new TreeSet<>();
        for(int h = 0; h < mailingLists.numberOfLists(); h++){
            mls.add(mailingLists.getMailingList(h).getName());
            Main.setMailLists(mls);
        }
        Main.setMailLists(mls);
        if(txtIndexs.length() == 0){
            feedback.setText("Please enter a valid index Number! \n");
            errorCount++;
        }
        if(errorCount == 0){
            Set<String> mNames = Main.getMailLists();
            int index = Integer.parseInt(txtIndex.getText());
            if(index >= 0 && index < mailingLists.numberOfLists()) {
                for (int g = 1; g < users.numberOfUsers(); g++) {
                if (users.getUser(g).getMailingLists().contains(mailingLists.getMailingList(index).getName())) {
                        mNames = users.getUser(g).getMailingLists();
                        mNames.remove(mailingLists.getMailingList(index).getName());
                        users.updateUser(g, users.getUser(g).getUserName(), users.getUser(g).getEmail(), users.getUser(g).getAdmin(), users.getUser(g).getSignUpDate(), users.getUser(g).getLastPost(), users.getUser(g).getPassword(), mNames);
                        users.saveUsers();
                        mNames.clear();
                    }
                }
                mailingLists.deleteMailingList(index);
                mailingLists.saveMailingLists();
                mailingLists.loadMailingLists();
                feedback.setText("MailingList Successfully deleted!");
            }
            }
        }

        public void handleDisplayEmail(ActionEvent e) throws Exception{
            StringBuilder m = new StringBuilder();
            for(int i = 0; i < mailingLists.numberOfLists(); i++){
                m.append("MailingList Name: " + mailingLists.getMailingList(i).getName() + "\n");
                for(int g = 1; g < users.numberOfUsers(); g++){
                    if(users.getUser(g).getMailingLists().contains(mailingLists.getMailingList(i).toString())) {
                        m.append("User's Email: " + users.getUser(g).getEmail() + "\n");
                    }
                }
            }
            feedback.setText(m.toString());
        }

        public void handleUpdateMember(ActionEvent e) throws Exception{
            mailingLists.loadMailingLists();
            users.loadUsers();
            int g = 0;
            int errorCount = 0;
            String name = txtMailName.getText();
            String desc = txtDescription.getText();
            String txtIndexs = txtIndex.getText();
            StringBuilder m = new StringBuilder();
            StringBuilder m2 = new StringBuilder();

            if(txtMailName.getText().equals("")){
                m2.append("Mailing List Name has been left blank it will use it's previous value \n");
            }
            if(txtDescription.getText().equals("")){
                m2.append("Mailing List description has been left blank it will use it's previous value \n");
            }
            if(txtIndexs.length() == 0){
                m.append("Please enter a valid index Number! \n");
                errorCount++;
            }
            if(errorCount == 0) {
                ArrayList<String> userNames = new ArrayList<>();
                int indexTRUE = Integer.parseInt(txtIndexs);
                userNames = mailingLists.getMailingList(indexTRUE).getUsers();
                if (!mailingLists.getMailingList(indexTRUE).getUsers().contains(choiceUser.getValue())) {
                    userNames.add(choiceUser.getValue());
                    m2.append("UserName is already a part of this Mailing List users will not be altered! \n");
                }
                mailingLists.updateMailingList(indexTRUE, name, desc, userNames);
                Set<String> mls;
                while (g < users.numberOfUsers()) {
                    if (users.getUser(g).getUserName().equals(choiceUser.getValue())) {
                        mls = users.getUser(g).getMailingLists();
                        mls.add(mailingLists.getMailingList(indexTRUE).getName());
                        Main.setMailLists(mls);
                        users.updateUser(g, users.getUser(g).getUserName(), users.getUser(g).getEmail(), users.getUser(g).getAdmin(), users.getUser(g).getSignUpDate(), users.getUser(g).getLastPost(), users.getUser(g).getPassword(), mls);
                    }
                    g++;
                }
                mailingLists.saveMailingLists();
                mailingLists.loadMailingLists();
                users.saveUsers();
                users.loadUsers();
                m2.append("Successfully updated Mailing List ").append(mailingLists.getMailingList(indexTRUE).getName()).append("!\n");
                feedback.setText(m2.toString());
            }
            else{
                feedback.setText(m.toString());
            }
        }










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
        ObservableList<String> usersList = FXCollections.observableList(list);
        for(int i = 0; i < users.numberOfUsers(); i++) {
            usersList.add(users.getUser(i).getUserName());
        }
        List<String> list2 = new ArrayList<String>();
        ObservableList<String> mailLists = FXCollections.observableList(list2);
        for(int i = 0; i < mailingLists.numberOfLists(); i++) {
            mailLists.add(mailingLists.getMailingList(i).getName());
        }

        choiceMailList.setItems(mailLists);
        choiceMailList.setValue(mailingLists.getMailingList(0).getName());
        choiceUser.setItems(usersList);
        choiceUser.setValue(users.getUser(0).getUserName());
    }
}
