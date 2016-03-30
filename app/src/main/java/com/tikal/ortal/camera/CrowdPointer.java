package com.tikal.ortal.camera;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ortal on 3/28/2016.
 */
public class CrowdPointer {

    public class Result {

        public int alfa = 0;

        public ArrayList<Point> pointList = new ArrayList<>();
    }

    Result findCrowd(ArrayList<Point> pois, int aperture) {
        Result result = new Result();
        HashMap<Integer, ArrayList<Point>> list = new HashMap<>();
        HashMap<Integer, Integer> countList = new HashMap<>();
        for (Point point : pois) {
            int alfa = (int) Math.toDegrees(Math.atan2(point.x, point.y));
            alfa = (alfa >= 0) ? alfa : 360 + alfa;
            if (list.containsKey(alfa)) {
                list.get(alfa).add(point);
                countList.put(alfa, countList.get(alfa) + 1);
            } else {
                ArrayList<Point> pointList = new ArrayList<>();
                pointList.add(point);
                list.put(alfa, pointList);
                countList.put(alfa, 1);
            }
        }

        int current = 0;
        for (int i = 0; i < aperture; i++) {
            current += countList.containsKey(i) ? countList.get(i) : 0;
        }
        int max = current;
        int index = 0;
        for (int i = 0; i < 360; i++) {
            current -= countList.containsKey(i) ? countList.get(i) : 0;
            current += countList.containsKey(i + aperture) ? countList.get(i + aperture) : 0;
            if (current > max) {
                max = current;
                index = i;
            }
        }
        result.alfa = index;
        for (int i = index; i <= index+aperture; i++) {
           if( list.containsKey(i) ){
               result.pointList.addAll(list.get(i));
           }
        }
         return result;
    }

}
