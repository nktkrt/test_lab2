package koors;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    private User user;

    @Before
    public void setUp() {
        user = new User("testUser");
    }

    @Test
    public void testUserInitialization() {
        assertEquals("testUser", user.nickname);
        assertTrue(user.adminu.isEmpty());
        assertTrue(user.sostoyu.isEmpty());
    }

    @Test
    public void testAddAdminConference() {
        Konferen konferen = new Konferen(user, "testConference");
        user.adminu.add(konferen);
        assertTrue(user.adminu.contains(konferen));
    }

    @Test
    public void testAddConferenceMembership() {
        Konferen konferen = new Konferen(user, "testConference");
        user.sostoyu.add(konferen);
        assertTrue(user.sostoyu.contains(konferen));
    }
}