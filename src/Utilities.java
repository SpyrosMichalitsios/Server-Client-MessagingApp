import java.rmi.*;

//The interface including all utilities that a client can ask.
public interface Utilities extends Remote{
    //The user/client can create an account choosing his own username.
    int createAccount(String username)
            throws RemoteException;
    //The user/client can ask to see a list of all the accounts.
    void showAccounts(int authToken)
            throws RemoteException;
    //The user/client can send a message to another user.
    void sendMessage(int authToken,String messageBody,String receiver)
            throws RemoteException;
    //The user/client can see a list of all the messages send to him
    void showInbox(int authToken)
            throws RemoteException;
    //The user/client can read the message with the given id from his messagebox.
    void readMessage(int authToken,int messageId)
            throws RemoteException;
    //The user/client can delete the message with the given id from his messagebox.
    void deleteMessage(int authToken,int messageId)
            throws RemoteException;
}
