/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;

/**
 *
 * @author Afrooz
 */
public class ClientHandler extends Thread {

    Socket socket;
    Scanner input;
    PrintWriter writer;
    ChatServer chat;
    String userID;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        chat = new ChatServer();

    }

    @Override
    public void run() {
        String message = input.nextLine(); //IMPORTANT blocking call
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
        while (!message.equals(ProtocolStrings.STOP)) {
            //writer.println(message.toUpperCase());
            handleMessage(message);
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
            message = input.nextLine(); //IMPORTANT blocking call
        }
        writer.println(ProtocolStrings.STOP);//Chat the stop message back to the client for a nice closedown
        chat.removeHandler(this);
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Closed a Connection");
    }

    public void send(String message) {
        writer.append(message + "\n");
        writer.flush();
    }

    private void handleMessage(String msg) {
        String arr[] = msg.split("#");
        String token = arr[0];

        if (token.equals("CONNECT")) {
            userID = arr[1];
            chat.addUser(userID, this);
        } else if (token.equals("CLOSE")) {
            chat.removeUser(userID);
        } else if (token.equals("SEND")) {
            String recievers = arr[1];
            String message = arr[2];
            chat.privateMessage(userID, recievers, message);
        } else {
            chat.broadcast(msg);
        }
    }
}
