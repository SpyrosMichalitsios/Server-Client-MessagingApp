public class Message implements java.io.Serializable {

    private boolean isRead;//Informs as if the message has been read or not.
    private String  sender;//The sender of the message.
    private String receiver;//The receiver of the message.
    private String body;//The body of the message.
    private int messageId;//A unique id that corresponds to a message.
    private static int idCounter=0;//A static value to reassure that the ids will be unique.

    /*The constructor of the class. The client gives us the following arguments
     and the isRead variable is by default false as the message cannot be read when created*/
    public  Message(String sender,String receiver,String body){
        isRead=false;
        this.sender=sender;
        this.receiver=receiver;
        this.body=body;
        this.messageId=idCounter;
        idCounter++; //Every time a message is created the counter should change to avoid giving the same id to two different messages. That is why the idCounter variable is static.
    }

    /* Necessary Setters and Getters for each variable*/
    public boolean getIsRead() {
        return isRead;
    }

    public String getSender() {
        return sender;
    }

    public String getBody(){
        return body;
    }

    public void setIsRead(boolean read) {
        isRead = read;
    }

    public int getMessageId() {
        return messageId;
    }
}
