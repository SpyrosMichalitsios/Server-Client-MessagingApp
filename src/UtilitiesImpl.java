import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Map;

public class UtilitiesImpl extends UnicastRemoteObject implements Utilities {

    //A map with all the accounts that have been created with the authTokens as keys and the account as the value.
    private HashMap<Integer,Account> accountMap;
    //A counter that will help us generate unique 4-digit tokens.
    private static int tokenCounter;


    UtilitiesImpl()throws RemoteException{
        super();
        accountMap=new HashMap<Integer,Account>(); //Initializing the map that will contain the accounts.

        tokenCounter=999;//The counter starts at 999 .(We add 1 at the start of each ''generation'' so it will become 1000 the first time an account is created)
    }

    @Override /*The client wants to create an account.We first check if the username is valid or if it  already exists.*/
    public int createAccount(String username) throws RemoteException {

        if(!isValid(username))  //If the username is invalid.
            throw new IllegalArgumentException("Invalid Username \n");
        else if (!isAvailable(username))    //If the username is not available.
            throw new IllegalArgumentException("Sorry, the user already exists \n");
        else{    //If the username is fitting the conditions we create the account and add it in the servers map,returning the authentication token.
            Account newAccount=new Account(username,generateAuthToken());
            accountMap.put(newAccount.getAuthToken(), newAccount);
            return newAccount.getAuthToken();
        }

    }

    @Override //The server shows all the accounts currently existing if the authentication token is correct
    public void showAccounts(int authToken) throws RemoteException {
        int counter=1;
        if (isAuthenticated(authToken)){//Always check if the user is  authenticated
            for(Map.Entry<Integer,Account> entry : accountMap.entrySet()){ //Searching in the accounts map.
                System.out.println( counter + ". " + entry.getValue().getUsername() + "\n");
                counter++;
            }
        }
        else//Not authenticated.
            System.out.println("Invalid Auth Token \n");
    }

    @Override/*The method for the client to send a message to another user.
    If the receiver does not exist the method returns the proper answer.
    If the receiver exist the message is added to the receiver's messagebox.
    */
    public void sendMessage(int authToken, String messageBody, String receiver) throws RemoteException {

        if (isAuthenticated(authToken)){//Always check if the user is  authenticated
            if (userExists(receiver)){//Check if the receiver exists.
                Message newMessage = new Message(accountMap.get(authToken).getUsername(), receiver, messageBody);
                for(Account a :accountMap.values()){
                    if(a.getUsername()==receiver){
                        a.addNewMessage(newMessage);// Add the message in the messagebox so the receiver can access it in the future.
                        System.out.println("OK \n");
                    }
                }
            }
            else//if it does not.
                System.out.println("User does not exist \n");
        }
        else//Not authenticated.
            System.out.println("Invalid Auth Token \n");

    }

    @Override//This method prints the users inbox.
    public void showInbox(int authToken) throws RemoteException {
        if(isAuthenticated(authToken)){//Always check if the user is  authenticated

            for(Message m: accountMap.get(authToken).getMessageBox()){//For every message in the users messagebox

                String output=m.getMessageId()+". from:"+ m.getSender();//We create the necessary string.

                if (!m.getIsRead())//If the message has not been read, we add the '*' after the message to mark it as unread.
                    output+="*";
                System.out.println(output+"\n");
            }
        }
        else//Not authenticated.
            System.out.println("Invalid Auth Token \n");

    }

    @Override //This method implements the users request to read one of his messages in his messagebox.
    public void readMessage(int authToken, int messageId) throws RemoteException {
        if(isAuthenticated(authToken)){//Always check if the user is  authenticated

            boolean found=false;//The message has not been found yet.

            for(Message m:accountMap.get(authToken).getMessageBox()){//Search for the message ID in the user's messagebox.

                if(m.getMessageId()==messageId){//If the message ID matches an ID from the client's messagebox.
                    m.setIsRead(true);//The message is now read.
                    found=true;//The message has been found
                    System.out.println("("+m.getSender()+")"+ m.getBody()+"\n");
                }
            }
            if (!found)//If the message has not been found.
                System.out.println("Message ID does not exist \n");
        }
        else//Not authenticated.
            System.out.println("Invalid Auth Token \n");
    }

    @Override//This method implements the deletion of a message from a user's messagebox when requested.
    public void deleteMessage(int authToken, int messageId) throws RemoteException {
        if(isAuthenticated(authToken)){//Always check if the user is  authenticated

            boolean found=false;//The message has not been  found yet.

            for(Message m:accountMap.get(authToken).getMessageBox()){//Search for the message ID in the user's messagebox.

                if(m.getMessageId()==messageId){//If the message ID matches an ID from the client's messagebox.
                    found =true;//The message has been found
                    accountMap.get(authToken).deleteMessage(m);//we delete the message
                    System.out.println("OK \n");
                }
            }
            if (!found)//If the message has not been found.
                System.out.println("Message ID does not exist \n");
        }
        else//Not authenticated.
            System.out.println("Invalid Auth Token \n");
    }

    //With this method we are checking if the username the client entered is valid.
    public boolean isValid(String wannabeUsername){
        if(wannabeUsername.matches("[a-zA-Z0-9_]+")) //Check if the username contains only letters numbers and "_".
            return true;
        else
            return false;
    }

    //With this method we can check if the username is available or if it already exists.
    public boolean isAvailable(String wannabeUsername){
        for (Map.Entry<Integer,Account> entry : accountMap.entrySet())  { //Check if username already exists.
            if (entry.getValue().getUsername() == wannabeUsername)//If it exists.
                return false;
        }
        return true;//The username does not exist.
    }

    // This method authenticates the user by checking if the token entered corresponds to an account.
    public boolean isAuthenticated(int authTkn){
        if(accountMap.containsKey(authTkn))//if it exists in our map.
            return true;
        else
            return false;

    }

    //With this method we generate a new unique 4-digit token that will be linked to the account created and that the user will use to authenticate himself.
    public int generateAuthToken(){
        tokenCounter++;
        if (tokenCounter<=9999)
            return tokenCounter;
        else
            throw new IllegalArgumentException("Tokens are over.The server cannot server  more clients :/ \n");//if we cannot make more 4-digit codes.
    }

    //A method to check if a user exists before sending him a message.
    public boolean userExists(String username){
        for(Account a: accountMap.values()){
            if (a.getUsername()==username)
                return true;
        }
        return false;
    }

}
