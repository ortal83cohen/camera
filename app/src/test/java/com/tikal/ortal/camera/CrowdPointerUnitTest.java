package com.tikal.ortal.camera;



import org.junit.Before;
import org.junit.Test;



import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CrowdPointerUnitTest {
    private CrowdPointer mCrowdPointer;


    @Before
    public void setUp() {
        mCrowdPointer = new CrowdPointer();
    }

    @Test
    public void objectIsNotNull() {

        assertNotNull(mCrowdPointer);
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
        CrowdPointer.Result result = mCrowdPointer.findCrowd(array,aperture );
        assertEquals(expectedResult.alfa, result.alfa);
        assertArrayEquals(expectedResult.pointList.toArray(), result.pointList.toArray());

    }
}