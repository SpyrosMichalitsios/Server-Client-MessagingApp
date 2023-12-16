import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {


    public static void main(String[] args){
        try{
            if(args.length!=1){
                System.out.println("The correct command should be : java Server <port number> ");
                System.exit(1);
            }

            int portNumber=Integer.parseInt(args[0]);//We get the port number from the terminal when we run the server.

            Utilities stub = new UtilitiesImpl();
            Registry reg= LocateRegistry.createRegistry(portNumber);
            reg.rebind("utilities",stub);

            System.out.println("Server is up and running...");
        }catch (Exception e){System.out.println("Failed to connect to the server");}

    }

}