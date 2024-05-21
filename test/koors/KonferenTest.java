package koors;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class KonferenTest {
    public Konferen konferen;
    public User admin;

    @Before
    public void setUp() {
        admin = new User("adminUser");
        konferen = new Konferen(admin, "testConference");
    }

    @Test
    public void testKonferenInitialization() {
        assertEquals("testConference", konferen.konfName);
        assertEquals(admin, konferen.adminit);
        assertTrue(konferen.my_users.contains(admin));
    }

    @Test
    public void testAddUserToKonferen() {
        User user = new User("testUser");
        konferen.my_users.add(user);
        assertTrue(konferen.my_users.contains(user));
    }
}