package koors;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class KlientTest {
    @Test
    public void testUpdateSpiss() {
        try {
            Klient klient = new Klient();
            klient.my_confs = new ArrayList<>();
            Konferen konferen1 = new Konferen("Conference1");
            klient.my_confs.add(konferen1);
            
            klient.updateSpiss();

            assertEquals(1, klient.konfSpis.getItemCount());
            assertEquals("Conference1", klient.konfSpis.getItem(0));
        } catch (Exception e) {
            fail("Updating spiss failed: " + e.getMessage());
        }
    }
}
