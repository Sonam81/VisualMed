package com.example.visualmed;

import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.*;

public class ManageMedicineActivityTest {

    @Rule
    public ActivityTestRule<ManageMedicineActivity> activityTestRule  = new ActivityTestRule<ManageMedicineActivity>(ManageMedicineActivity.class);
    private ManageMedicineActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = activityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view = mActivity.findViewById(R.id.manage_medicine_relative_layout);
        assertNotNull(view);
    }

    @Test
    public void test_welcome_message(){
        View view = mActivity.findViewById(R.id.text);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}