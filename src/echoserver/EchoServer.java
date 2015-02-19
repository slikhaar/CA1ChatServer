package echoserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;

public class EchoServer {

    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private static final Properties properties = Utils.initProperties("server.properties");
    private static List<ClientHandler> clientHandlers = new ArrayList<>();
    private static List<String> onlineList = new ArrayList<>();
    private static Map<String, ClientHandler> handlers = new HashMap<>();


    public static void stopServer() {
        keepRunning = false;
    }

    public void removeHandler(ClientHandler ch) {
        clientHandlers.remove(ch);
    }

    public void broadcast(String msg) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.send(msg);
        }
    }
    
    public void registerUser(String user)
    {
        onlineList.add(user);
        sendOnlineMessages();
    }
    public void addUser(String user, ClientHandler handler){
        System.out.println(user + ":" + handler);
    handlers.put(user, handler);
    }
    
    public void unregisterUser(String user)
    {    
        
        onlineList.remove(user);        
//        sendOfflineMessages(user);
        sendOnlineMessages();
        
    }
    
  private void sendOfflineMessages(String user)
    {
        StringBuilder msg = new StringBuilder();      
        msg.append("OFFLINE");
        broadcast(msg.toString());
    }
    
    private void sendOnlineMessages()
    {
        StringBuilder msg = new StringBuilder();
        
        msg.append("ONLINE#");
        for(int i = 0; i < onlineList.size(); ++i)
        {
            if(i > 0) msg.append(',');         
            msg.append(onlineList.get(i));
            
        }
        broadcast(msg.toString());
    }

    public void privateMessage(String userID, String recs, String message) {
        String recievers[] = recs.split(",");
        for (String reciever : recievers) {
            System.out.println(reciever);
                    
             ClientHandler clientHandler = handlers.get(reciever);
             clientHandler.send(message);
        }
        
        
        
//        clientHandler.send(msg)
    }

    private void runServer() {

        String logFile = properties.getProperty("logFile");
        Utils.setLogFile(logFile, EchoServer.class.getName());

        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");

        Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, "Sever started. Listening on: " + port + ", bound to: " + ip);
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, "Connected to a client");
                ClientHandler client = new ClientHandler(socket);
                clientHandlers.add(client);
                client.start();

            } while (keepRunning);
        } catch (IOException ex) {
            Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Utils.closeLogger(EchoServer.class.getName());
        }
    }

    public static void main(String[] args) {

        new EchoServer().runServer();

    }

}
