package koors;
import org.junit.Test;
import static org.junit.Assert.*;


public class FirstFrameTest {
    @Test
    public void testLoginInput() {
        try {
            FirstFrame frame = new FirstFrame();
            frame.login.setText("testUser");
            assertEquals("testUser", frame.login.getText());
        } catch (Exception e) {
            fail("Login input test failed: " + e.getMessage());
        }
    }

}