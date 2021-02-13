package application.model;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

public class UserModel {
    Stack<User> users = new Stack<>();

    public void addUser(User user){
        users.add(user);
    }

    public String listAllUsers() {
        if (users.size() == 0) {
            return "No Users at the moment";
        } else {
            String listAllUsers = "";
            for (int i = 0; i < users.size(); i++) {
                listAllUsers = listAllUsers + i + ": " + users.get(i) + "\n";
            }
            return listAllUsers;
        }
    }
   public boolean updateUser(int index,String userName, String email, Boolean isAdmin, LocalDate signupDate, String lastPost, String password, Set<String> mailingLists){
        if (index >= 0 && index < users.size()) {
            User u = users.get(index);
            u.setUserName(userName);
            u.setEmail(email);
            u.setAdmin(isAdmin);
            u.setSignUpDate(signupDate);
            u.setLastPost(lastPost);
            u.setPassword(password);
            u.setMailingLists(mailingLists);
            return true;
        }
        else{
            return false;
        }
   }
   public Stack<User> getUsers(){
       return users;
    }


    public User getUser(int User){
        if(users.size() == 0){
            return null;
        }
        else{
            return users.get(User);
        }
    }

    public boolean deleteUser(int index){
        if (index >=0 && index < users.size()) {
            users.remove(index);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteTopUser(Stack<User> user){
        if(users.size() == 0){
            return false;
        }
        User u = (User) users.pop();
        return true;
    }

    public int numberOfUsers(){
        return users.size();
    }

    public void saveUsers() throws Exception

    {

        XStream xstream = new XStream(new DomDriver());

        ObjectOutputStream out = xstream.createObjectOutputStream

                (new FileWriter("users.xml"));

        out.writeObject(users);

        out.close();

    }

    public void loadUsers() throws Exception

    {

        XStream xstream = new XStream(new DomDriver());

        ObjectInputStream is = xstream.createObjectInputStream

                (new FileReader("users.xml"));

        users = (Stack<User>) is.readObject();

        is.close();

    }




}
