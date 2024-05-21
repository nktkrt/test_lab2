package koors;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServTest {
    public Serv serv;
    public Path usersTempFile;
    public Path confsTempFile;

    @Before
    public void setUp() throws IOException {
        serv = new Serv();
        usersTempFile = Files.createTempFile("users", ".tmp");
        confsTempFile = Files.createTempFile("confs", ".tmp");
    }

    @Test
    public void testServerInitialization() {
        assertNotNull(serv);
        assertNotNull(serv.userList);
        assertNotNull(serv.confList);
    }


    @Test
    public void testUpdateSpiss() {
        User user = new User("testUser");
        Konferen konferen = new Konferen(user, "testConference");
        serv.userList.add(user);
        serv.confList.add(konferen);

        serv.updateSpiss();

        assertEquals(1, serv.userSpis.getItemCount());
        assertEquals(1, serv.konfSpis.getItemCount());
    }

    @Test
    public void testChangeConference() {
        User user = new User("testUser");
        Konferen konferen = new Konferen(user, "testConference");
        serv.confList.add(konferen);

        serv.konfSpis.add("testConference");
        serv.konfSpis.select(0);
        serv.changed();

        assertTrue(konferen.my_textarea.isVisible());
    }


}