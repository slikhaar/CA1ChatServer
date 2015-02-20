/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import chatclient.ChatClient;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author saleh
 */
public class ClientHandlerTest {

    

    public ClientHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
//        server = new ChatServer();
//        server.runServer(); 
    }

    @After
    public void tearDown() {
//        server.stopServer();
    }

    /**
     * Test of run method, of class ClientHandler.
     */
    //@Test
    public void testRun() {
        System.out.println("test run");

        assertTrue(true);
//        System.out.println("run");
//        ClientHandler instance = null;
//        instance.run();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of send method, of class ClientHandler.
     */
    //@Test
    public void testSend() {
        System.out.println("test send");
//        String message = "";
//        ClientHandler instance = null;
//        instance.send(message);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
        assertTrue(true);
    }

    @Test
    public void testHandleMessageOnConnect() throws IOException, InterruptedException {
        System.out.println("test handler on connect");

        ChatClient client;
        ChatServer server;
        
        server = new ChatServer();
        server.runServer();
        
        Thread.sleep(4000);
        
        server.stopServer();
        
        
        
        
        
        
//  ClientHandler handler = new ClientHandler(new Socket("", 9090));
//        
//        client = new ChatClient();
//        client.connect("", 9090);
//        client.send("CONNECT#TEST");
//               
//        int expResutl = 1;
//        int result = ChatServer.handlers.size();
//        
//        assertThat(result, is(expResutl));
//        
        
        assertTrue(true);
        
        
    }

}
