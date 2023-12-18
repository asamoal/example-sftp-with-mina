package org.example;
import org.junit.Test;

import static org.junit.Assert.fail;

public class AppTest {

    @Test
    public void testAppMainWithoutArgs() {
        try {
            App.main(new String[]{});
            fail("Exception not thrown in case of missing arguments");
        } catch (Exception e) {
            // Exception was thrown, so the test case passed
        }
    }
}