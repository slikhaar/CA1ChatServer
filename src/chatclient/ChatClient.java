package chatclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;

public class ChatClient extends Thread implements ChatListener {

    Socket socket;
    private int port;
    private String userID;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    List<ChatListener> listeners = new ArrayList();
    ChatListener chat;

    private void notifyListeners(String msg) {
        for (ChatListener listener : listeners) {
            listener.messageArrived(msg);
        }
    }

    public void registerChatListener(ChatListener l) {
        listeners.add(l);
    }

    public void unRegisterChatListener(ChatListener l) {
        listeners.remove(l);
    }

    public void connect(String address, int port) throws UnknownHostException, IOException {
        this.userID = userID;
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
    }

    public void send(String msg) {
        output.println(msg);
    }

//    public void stop() throws IOException {
//        output.println(ProtocolStrings.STOP);
//    }
    @Override
    public void run() {

        String msg = input.nextLine();
        System.out.println(msg);
        while (!msg.equals(ProtocolStrings.STOP)) {
            notifyListeners(msg);
            msg = input.nextLine();
        }
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        int port = 9090;
        String ip = "137.135.209.201";
        if (args.length == 2) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
        }
        try {

            ChatClient tester = new ChatClient();
            tester.registerChatListener(tester);
            tester.connect(ip, port);
            System.out.println("Sending 'Hello world'");
            tester.send("Hello World");
            System.out.println("Waiting for a reply");
            System.out.println("Received: "); //Important Blocking call         
            tester.stop();
            //System.in.read();      

        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatClient.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void messageArrived(String data) {
        data.toUpperCase();

    }

    public void stopChat() {
        output.println(ProtocolStrings.STOP);
    }
}
