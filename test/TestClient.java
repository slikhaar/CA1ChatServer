
import chatclient.ChatClient;
import chatclient.ChatListener;
import chatserver.ChatServer;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Lars Mortensen
 */
public class TestClient {

    private String testResult = "";
    private CountDownLatch lock;

    public TestClient() {
    }

    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatServer.main(null);
            }
        }).start();
    }

    @AfterClass
    public static void tearDownClass() {
        ChatServer.stopServer();
    }

    @Before
    public void setUp() {
    }

    @Test
    public void sendMessage() throws IOException, InterruptedException {
        lock = new CountDownLatch(1);
        testResult = "";
        ChatClient tester = new ChatClient();
        tester.connect("localhost", 9090);

        tester.registerChatListener(new ChatListener() {
            @Override
            public void messageArrived(String data) {
                testResult = data;
                lock.countDown();

            }
        });
        tester.send("Hello");
        lock.await(1000, TimeUnit.MILLISECONDS);
        assertEquals("HELLO", testResult);
        tester.stopChat();
    }

}


