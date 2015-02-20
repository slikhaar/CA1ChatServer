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
    private static Map<String, ClientHandler> handlers = new HashMap<>();

    public static void stopServer() {
        keepRunning = false;
    }

    public void removeHandler(ClientHandler ch) {
        handlers.remove(ch.userID);
    }

    public void broadcast(String msg) {
        for (ClientHandler clientHandler : handlers.values()) {
            clientHandler.send(msg);
        }
    }

    public void addUser(String user, ClientHandler handler) {
        System.out.println(user + ":" + handler);
        handlers.put(user, handler);
        sendOnlineMessages();
    }

    public void removeUser(String user) {
        handlers.remove(user);
        sendOnlineMessages();
    }

    private void sendOnlineMessages() {
        StringBuilder msg = new StringBuilder();
        msg.append("ONLINE#");
        for (String userName : handlers.keySet()) {
            msg.append(userName);
            msg.append(",");
        }
        String temp = msg.substring(0, msg.lastIndexOf(","));
        broadcast(temp);
    }

    public void privateMessage(String userID, String recs, String message) {
        if (recs.equals("*")) {
            broadcast(message);
        } else {
            String recievers[] = recs.split(",");
            for (String reciever : recievers) {
                System.out.println(reciever);
                ClientHandler clientHandler = handlers.get(reciever);
                clientHandler.send(message);
            }
        }
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
//                clientHandlers.add(client);                
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
