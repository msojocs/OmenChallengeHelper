package org.example;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

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
        // 2021-05-26T11:11:06.115767Z
        SimpleDateFormat endTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

        endTime.setTimeZone(TimeZone.getTimeZone("+0"));

        String start = endTime.format(new Date(System.currentTimeMillis() - 1000 * 60 * 4));
        String end = endTime.format(new Date());

        System.out.println(start);
        System.out.println(end);

    }
}
