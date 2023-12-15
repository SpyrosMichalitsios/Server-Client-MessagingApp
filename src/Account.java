import java.util.ArrayList;
import java.util.List;


public class Account implements java.io.Serializable {
    private String username;//The account's owner's username.
    private int authToken;//The authentication token that the user will use to authenticate himself.
    private List<Message> messageBox;//A list with all the messages the account's owner has received.

    /*Constructor of the Account class. When making an account the client will choose a username for himself
    and the server will grant him an authentication token.The messageBox starts off empty.
     */
    public Account(String username,int authToken){
        this.username=username;
        this.authToken=authToken;
        messageBox=new ArrayList<Message>();
    }

    //Necessary getters and setters.
    public String getUsername(){
        return username;
    }

    public int getAuthToken(){
        return authToken;
    }

    public List<Message> getMessageBox(){
        return messageBox;
    }

    //A method to use for adding messages in the user's messagebox.
    public void addNewMessage(Message newMessage){
        messageBox.add(newMessage);
    }

    //A method to use for deleting messages from the user's messagebox.
    public void deleteMessage(Message aMessage){
        messageBox.remove(aMessage);
    }

    //This generates a unique 4-digit authentication token for the user.The first time it will become 999


}
