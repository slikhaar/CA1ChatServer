/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
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
    EchoServer echo;
    String userID;

    public ClientHandler(Socket socket) throws IOException {
        this.userID = userID;
        this.socket = socket;
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        echo = new EchoServer();

    }

    @Override
    public void run() {
        String message = input.nextLine(); //IMPORTANT blocking call
        Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
        while (!message.equals(ProtocolStrings.STOP)) {
            //writer.println(message.toUpperCase());
            handleMessage(message);
            Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
            message = input.nextLine(); //IMPORTANT blocking call
        }
        writer.println(ProtocolStrings.STOP);//Echo the stop message back to the client for a nice closedown
        echo.removeHandler(this);
        echo.unregisterUser(userID);
        try {
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, "Closed a Connection");
    }

    public void send(String message) {
        writer.append(message.toUpperCase() + "\n");
        writer.flush();
    }

    private void handleMessage(String msg) {
        if (msg.startsWith("CONNECT#")) {
            userID = msg.substring(8);
            echo.registerUser(userID);
        }
        else if(msg.startsWith("CLOSE#")){
            userID = msg.substring(6);
            echo.unregisterUser(userID);
        }
        else
        {
            echo.broadcast(msg);
        }       
    }
}
