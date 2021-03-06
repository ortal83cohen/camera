package com.tikal.ortal.camera;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;

import java.util.ArrayList;

/**
 * Created by ortalcohen on 31/03/2016.
 */
@RunWith(AndroidJUnit4.class)
public class CrowdPointerTest {

    private CrowdPointer mCrowdPointer;

    @Before
    public void setUp() {
        mCrowdPointer = new CrowdPointer();
    }

    @Test
    public void objectIsNotNull() {

        Assert.assertNotNull(mCrowdPointer);
    }

    @Test
    public void test1() {
        ArrayList<Point> array = new ArrayList<>();
        array.add(new Point(4, 44));
        array.add(new Point(4, 44));
        array.add(new Point(44, 44));
        array.add(new Point(77, 77));
        array.add(new Point(44, -44));
        array.add(new Point(434, -434));
        array.add(new Point(44, -47));
        array.add(new Point(-44, 44));
        array.add(new Point(-4, 44));
        array.add(new Point(-4, 44));
        array.add(new Point(-44, -44));

        CrowdPointer.Result expectedResult = new CrowdPointer.Result();
        expectedResult.alfa = 352;
        expectedResult.pointList = new ArrayList<>();
        expectedResult.pointList.add(new Point(-4, 44));
        expectedResult.pointList.add(new Point(-4, 44));
        expectedResult.pointList.add(new Point(4, 44));
        expectedResult.pointList.add(new Point(4, 44));

        makeTest(array, 13, expectedResult);
    }

    private void makeTest(ArrayList<Point> array, int aperture,
            CrowdPointer.Result expectedResult) {
        CrowdPointer.Result result = mCrowdPointer.findCrowd(array, aperture);
        Assert.assertEquals(expectedResult.alfa, result.alfa);
        Assert.assertArrayEquals(expectedResult.pointList.toArray(), result.pointList.toArray());

    }
}