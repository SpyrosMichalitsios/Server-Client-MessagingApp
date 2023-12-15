import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        int portNumber;
        String ipAddress;
        int fnID;
        try {//Checking if the arguments entered by the client are correct

            /*The args string array passed in the main should have the following.
            args[0]=IP address of the server.
            args[1]=port number that the server is listening to.
            args[2]=FN_ID
            args[3...]=the rest of the arguments.
             */
            if(args.length!=0) {
                ipAddress = args[0];
                portNumber = Integer.parseInt(args[1]);
                fnID = Integer.parseInt(args[2]);

                if (args[1].length() != 4) {//Check if the port number given is valid.
                    System.out.println("Please try entering a 4-digit integer for a port");
                    System.exit(1);
                }
                if (!((fnID >= 1) && (fnID <= 6))) {//The FN_ID should be between 1 and 6.
                    System.out.println("""
                            Please enter a valid FN_ID.The available FN_IDs are:\s
                            FN_ID:1 = Create account
                            FN_ID:2 = Show Accounts
                            FN_ID:3 = Send Message
                            FN_ID:4 = Show Inbox
                            FN_ID:5 = Read Message
                            FN_ID:6 = Delete Message
                            """
                    );
                }


                try {//Connecting to the registry.

                    Registry reg = LocateRegistry.getRegistry(ipAddress, portNumber);
                    Utilities stub = (Utilities) reg.lookup("utilities");

                    switch (fnID) {//Depending on the fnID do accordingly.
                        case 1://Create account.
                            System.out.println("we are in 1");//FOR DEBUGGING
                            int result=stub.createAccount(args[3]);
                            switch (result) {
                                case -1 ->//The username is invalid.
                                        System.out.println("Invalid Username\n");
                                case 0 ->//The username is not available.
                                        System.out.println("Sorry, the user already exists");
                                default ->//The account has been created.
                                        System.out.println(result);
                            }
                            break;
                        case 2://Show Accounts.
                            try {//Check if the authToken entered is an int.
                                System.out.println("we are in 2");//FOR DEBUGGING
                                int tkn = Integer.parseInt(args[3]);
                                System.out.println(stub.showAccounts(tkn));
                            } catch (NumberFormatException e) {
                                System.out.println("The authToken should be an integer\n");
                            }
                            break;
                        case 3://Send message.
                            try {//Check if the authToken entered is an int.
                                System.out.println("we are in 3");//FOR DEBUGGING
                                int tkn = Integer.parseInt(args[3]);
                                stub.sendMessage(tkn, args[4], args[5]);
                            } catch (NumberFormatException e) {
                                System.out.println("The authToken should be an integer\n");
                            }
                            break;
                        case 4://Show inbox.
                            try {//Check if the authToken entered is an int.
                                System.out.println("we are in 4");//FOR DEBUGGING
                                int tkn = Integer.parseInt(args[3]);
                                stub.showInbox(tkn);
                            } catch (NumberFormatException e) {
                                System.out.println("The authToken should be an integer\n");
                            }
                            break;
                        case 5://Read message.
                            try {//Check if the authToken and the message id entered are an int.
                                System.out.println("we are in 5");//FOR DEBUGGING
                                int tkn = Integer.parseInt(args[3]);
                                int msgID = Integer.parseInt(args[4]);
                                stub.readMessage(tkn, msgID);
                            } catch (NumberFormatException e) {
                                System.out.println("The authToken should be an integer\n");
                            }
                            break;
                        case 6://Delete message.
                            try {//Check if the authToken and the message id entered are an int.
                                System.out.println("we are in 6");//FOR DEBUGGING
                                int tkn = Integer.parseInt(args[3]);
                                int msgID = Integer.parseInt(args[4]);
                                stub.deleteMessage(tkn, msgID);
                            } catch (NumberFormatException e) {
                                System.out.println("The authToken should be an integer\n");
                            }


                    }
                } catch (Exception e) {
                    System.out.println("Failed to connect to the registry");
                }
            }
            else
                System.out.println("The correct command should be : java Client <ip> <port number> <FN_ID> <args>");
        } catch (NumberFormatException e) {
            System.out.println("The portNumber and FN_ID should be an integer");
        }


    }}