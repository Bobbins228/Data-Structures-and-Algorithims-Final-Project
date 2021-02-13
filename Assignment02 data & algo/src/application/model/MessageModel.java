package application.model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.Set;
import java.util.Stack;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class MessageModel {
    Stack<Message> messages = new Stack<>();

    public void addMessage(Message message){
        messages.add(message);
    }

    public Stack<Message> getMessages() {
        return messages;
    }

    public String listMessages(){
        if (messages.size() == 0) {
            return "No messages at the moment";
        } else {
            String listAllMessages = "";
            for (int i = 0; i < messages.size(); i++) {
                listAllMessages = listAllMessages + i + ": " + messages.get(i) + "\n";
            }
            return listAllMessages;
        }
    }
    public boolean deleteMessage(int index){
        if (index >=0 && index < messages.size()) {
            messages.remove(index);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean updateMessage(int index, User user, String title, LocalDate date, String priority, String mailingList, String body){
        if (index >= 0 && index < messages.size()) {
            Message m = messages.get(index);
            m.setUser(user);

            title = messages.get(index).getMessageTitle();
            m.setMessageTitle(title);

            date = messages.get(index).getDateTime();
            m.setDateTime(date);

            priority = messages.get(index).getPriority();
            m.setPriority(priority);

            mailingList = messages.get(index).getMailingList();
            m.setMailingList(mailingList);

            body = messages.get(index).getMessageBody();
            m.setMessageBody(body);
            return true;
        }
        else{
            return false;
        }
    }

    public Message getMessage(int Message){
        if(messages.size() == 0){
            return null;
        }
        else{
            return messages.get(Message);
        }
    }

    public int numberOfMessages(){
        return messages.size();
    }
    public void saveMessages() throws Exception

    {

        XStream xstream = new XStream(new DomDriver());

        ObjectOutputStream out = xstream.createObjectOutputStream

                (new FileWriter("messages.xml"));

        out.writeObject(messages);

        out.close();

    }

    public void loadMessage() throws Exception

    {

        XStream xstream = new XStream(new DomDriver());

        ObjectInputStream is = xstream.createObjectInputStream

                (new FileReader("messages.xml"));

        messages = (Stack<Message>) is.readObject();

        is.close();

    }


}

