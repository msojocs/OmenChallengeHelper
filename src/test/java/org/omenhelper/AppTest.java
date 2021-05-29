package org.omenhelper;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.omenhelper.Omen.VsJSONProgressEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testA(){
        // int rn = (int) (255 * Math.random());
        // String rs = Integer.toString(rn, 16);
        // if(rs.length()==1) rs += Integer.toString((int)(16 * Math.random()), 16);
        // System.out.println(rs.toUpperCase());

        String uuid1 = UUID.randomUUID().toString();
        uuid1 = uuid1.replaceAll("-", "").toUpperCase();

        String uuid2 = UUID.randomUUID().toString();
        uuid2 = uuid2.replaceAll("-", "").toUpperCase();

        String desktop = uuid1.substring(0, 3) + uuid2.substring(0, 3);
        System.out.println(desktop);

        StringBuilder uuid = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            uuid.append(uuid1.charAt(i)).append(uuid2.charAt(i));
            if(i < 31)uuid.append("-");
        }
        System.out.println(uuid);

    }

    // @Test
    // public void signature() throws Exception {
    //     System.out.println(new VsJSONProgressEvent(
    //             "PLAY:LEAGUE_OF_LEGENDS",
    //             "2021-05-27T04:10:07.769485Z",
    //             "2021-05-27T04:10:10.430310Z",
    //             1
    //     ).GetSignature("6589915c-6aa7-4f1b-9ef5-32fa2220c844",""));
    // }
}
