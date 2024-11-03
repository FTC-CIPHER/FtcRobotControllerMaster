package org.firstinspires.ftc.teamcode;

import static org.junit.Assert.*;
import org.junit.Test;


public class LinearSlideTestingTest {

    @Test
    public void test_outtakeMode() {
        assertEquals(1,0+1);
    }

    // @Test  // uncomment to demo
    public void testSleepCpuUsage() throws InterruptedException {
        int i=0;
        while(true) {
            i = 1;
            Thread.sleep(1);  // comment out for no sleep case. compare CPU utilization
        }
    }

}