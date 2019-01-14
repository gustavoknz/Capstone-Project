package com.bora.gustavo;

import com.bora.gustavo.helper.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BoraUnitTest {
    @Test
    public void UuidIsNotNull() {
        String uuid = Utils.createUuid();
        assertNotNull("UUID must not be null", uuid);
        assertNotEquals("UUID must not be empty", "", uuid);
    }
}