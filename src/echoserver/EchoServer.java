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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;

public class EchoServer {

    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private static final Properties properties = Utils.initProperties("server.properties");
    private static List<ClientHandler> clientHandlers = new ArrayList<>();
    private static List<String> userID = new ArrayList<>();
     
    public static void stopServer() {
        keepRunning = false;
    }

    public void removeHandler(ClientHandler ch) {
        clientHandlers.remove(ch);
    }

    public void send(String msg) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.send(msg);
        }
    }

    public void send(String sender, String msg, String target) {
        if (clientHandlers.equals(target)) {

            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.send("MESSAGE#" + sender + "#" + msg);
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
