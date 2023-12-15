import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;

public class UtilitiesImpl extends UnicastRemoteObject implements Utilities {

    //A map with all the accounts that have been created with the authTokens as keys and the account as the value.
    private final HashMap<Integer,Account> accountMap;
    //A counter that will help us generate unique 4-digit tokens.
    private static int tokenCounter;


    UtilitiesImpl()throws RemoteException{
        super();
        accountMap=new HashMap<>();  //Initializing the map that will contain the accounts.

        tokenCounter=999;//The counter starts at 999 .(We add 1 at the start of each ''generation'' so it will become 1000 the first time an account is created)
    }

    @Override /*The client wants to create an account.We first check if the username is valid or if it  already exists.*/
    public int createAccount(String username) throws RemoteException {

        if(!isValid(username)) {//If the username is invalid.
            return -1;
        }
        else if (!isAvailable(username)){    //If the username is not available.
           return 0;
        }
        else{//If the username is fitting the conditions we create the account and add it in the servers map,returning the authentication token.
            Account newAccount=new Account(username,generateAuthToken());
            accountMap.put(newAccount.getAuthToken(), newAccount);
            return newAccount.getAuthToken();
        }

    }

    @Override //The server shows all the accounts currently existing if the authentication token is correct
    public String showAccounts(int authToken) throws RemoteException {
        String result="";

        if (isAuthenticated(authToken)){//Always check if the user is  authenticated
            int counter=1;
            for(Account a : accountMap.values()){ //Searching in the accounts map.
                result=result.concat(counter + ". " + a.getUsername() + "\n");
                counter++;
            }
        }
        else//Not authenticated.
            result= "Invalid Auth Token \n";

        return result;
    }

    @Override/*The method for the client to send a message to another user.
    If the receiver does not exist the method returns the proper answer.
    If the receiver exist the message is added to the receiver's messagebox.
    */
    public String sendMessage(int authToken, String receiver, String messageBody) throws RemoteException {

        if (isAuthenticated(authToken)){//Always check if the user is  authenticated

            if (userExists(receiver)){//Check if the receiver exists.
                Message newMessage = new Message(accountMap.get(authToken).getUsername(), receiver, messageBody);
                for(Account a :accountMap.values()){
                    if(a.getUsername().equals(receiver)){
                        a.addNewMessage(newMessage);// Add the message in the messagebox so the receiver can access it in the future.
                    }
                }
                return  "OK";
            }
            else//if the receiver does not exist.
                return "User does not exist \n";
        }
        else//User not authenticated.
            return "Invalid Auth Token \n";
    }


    @Override//This method prints the users inbox.
    public String showInbox(int authToken) throws RemoteException {
        String result="";
        if(isAuthenticated(authToken)){//Always check if the user is  authenticated

            for(Message m: accountMap.get(authToken).getMessageBox()){//For every message in the users messagebox

                result=result.concat(m.getMessageId()+". from:"+ m.getSender());//We create the necessary string.

                if (!m.getIsRead())//If the message has not been read, we add the '*' after the message to mark it as unread.
                    result=result.concat("*");

                result=result.concat("\n");
            }
            return result;
        }
        else//Not authenticated.
            return "Invalid Auth Token \n";
    }

    @Override //This method implements the users request to read one of his messages in his messagebox.
    public String readMessage(int authToken, int messageId) throws RemoteException {

        if(isAuthenticated(authToken)){//Always check if the user is  authenticated

            for(Message m:accountMap.get(authToken).getMessageBox()){//Search for the message ID in the user's messagebox.

                if(m.getMessageId()==messageId){//If the message ID matches an ID from the client's messagebox.
                    m.setIsRead(true);//The message is now read.
                    return "("+m.getSender()+")"+ m.getBody()+"\n";
                }
            }
            return "Message ID does not exist \n";
        }
        else//Not authenticated.
            return "Invalid Auth Token \n";

    }

    @Override//This method implements the deletion of a message from a user's messagebox when requested.
    public void deleteMessage(int authToken, int messageId) throws RemoteException {
        if(isAuthenticated(authToken)){//Always check if the user is  authenticated

            boolean found=false;//The message has not been  found yet.

            for(Message m: accountMap.get(authToken).getMessageBox()){//Search for the message ID in the user's messagebox.

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
        //Check if the username contains only letters numbers and "_".
        return wannabeUsername.matches("[a-zA-Z0-9_]+");
    }

    //With this method we can check if the username is available or if it already exists.
    public boolean isAvailable(String wannabeUsername){
        for (Account a: accountMap.values())  {//Check if username already exists.
            if (a.getUsername() .equals(wannabeUsername) )//If it exists.
                return false;
        }
        return true;//The username does not exist.
    }

    // This method authenticates the user by checking if the token entered corresponds to an account.
    public boolean isAuthenticated(int authTkn){
        //if it exists in our map.
        return accountMap.containsKey(authTkn);

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
            if (a.getUsername().equals(username))
                return true;
        }
        return false;
    }

}
