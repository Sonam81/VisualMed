package com.example.visualmed;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimeValidatorTest {

    @Test(expected = NullPointerException.class)
    public void timeValidator_correctTime_returnsTrue(){
        assertTrue(TimeValidator.validate("2:30p.m."));
    }

    @Test(expected = NullPointerException.class)
    public void timeValidator_timeWithoutAm_Pm_returnsFalse(){
        assertFalse(TimeValidator.validate("2:30"));
    }

    @Test(expected = NullPointerException.class)
    public void timeValidator_string_returnsFalse(){
        assertFalse(TimeValidator.validate("time"));
    }

    @Test(expected = NullPointerException.class)
    public void timeValidator_number_returnsFalse(){
        assertFalse(TimeValidator.validate("12345"));
    }

    @Test(expected = NullPointerException.class)
    public void timeValidator_timeWithSpaceInBetween_returnsFalse(){
        assertFalse(TimeValidator.validate("2 : 30 p.m."));
    }

    @Test(expected = NullPointerException.class)
    public void timeValidator_timeWithoutDash_returnsFalse(){
        assertFalse(TimeValidator.validate("2 30 p.m."));
    }

}
