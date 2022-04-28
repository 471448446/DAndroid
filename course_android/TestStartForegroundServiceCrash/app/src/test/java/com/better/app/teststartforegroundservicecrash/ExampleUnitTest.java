package com.better.app.teststartforegroundservicecrash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void text() {
        String reason = "android.app.RemoteServiceException: Context.startForegroundService() did not then call Service.startForeground(): ServiceRecord{67f87f9 u0 com.better.app.teststartforegroundservicecrash/.TestService}";
        Matcher matcher = Pattern
                .compile("Context.startForegroundService\\(\\) did not then call Service.startForeground\\(\\)(.+)TestService")
                .matcher(reason);
        assertTrue(matcher.find());
    }
}