package application.model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Stack;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class MailingListModel {
    Stack<MailingList> mailingLists = new Stack<>();

    public void addMailingList(MailingList mailingList){
        mailingLists.add(mailingList);
    }

    public String listMailingLists(){
        if (mailingLists.size() == 0) {
            return "No Users at the moment";
        } else {
            String listAllMailingLists = "";
            for (int i = 0; i < mailingLists.size(); i++) {
                listAllMailingLists = listAllMailingLists + i + ": " + mailingLists.get(i) + "\n";
            }
            return listAllMailingLists;
        }
    }

    public boolean deleteMailingList(int index){
        if (index >=0 && index < mailingLists.size()) {
            mailingLists.remove(index);
            return true;
        }
        else {
            return false;
        }
    }

    public MailingList getMailingList(int MailingList){
        if(mailingLists.size() == 0){
            return null;
        }
        else{
            return mailingLists.get(MailingList);
        }
    }
    public boolean updateMailingList(int index, String name, String Description, ArrayList<String> userNames){
        if (index >= 0 && index < mailingLists.size()) {
            MailingList m = mailingLists.get(index);
            if (name.equals("")) {
                name = mailingLists.get(index).getName();
            }
            else{
                m.setName(name);
            }
            if(Description.equals("")){
                Description = mailingLists.get(index).getDescription();
            }
            else {
                m.setDescription(Description);
            }
            m.setUsers(userNames);
            return true;
        }
        else {
            return false;
        }
    }

    public int numberOfLists(){
        return mailingLists.size();
    }
    public void saveMailingLists() throws Exception

    {

        XStream xstream = new XStream(new DomDriver());

        ObjectOutputStream out = xstream.createObjectOutputStream

                (new FileWriter("mailingLists.xml"));

        out.writeObject(mailingLists);

        out.close();

    }

    public void loadMailingLists() throws Exception

    {

        XStream xstream = new XStream(new DomDriver());

        ObjectInputStream is = xstream.createObjectInputStream

                (new FileReader("mailingLists.xml"));

        mailingLists = (Stack<MailingList>) is.readObject();

        is.close();

    }


}
