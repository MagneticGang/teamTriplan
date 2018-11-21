package com.hanul.team1.triplan.ysh.util;

public class Distance {

    public double calcDistance(double y1, double x1, double y2, double x2){
        double theta, dist;
        theta = x1 - x2;
        dist = Math.sin(deg2rad(y1)) * Math.sin(deg2rad(y2)) + Math.cos(deg2rad(y1))
                * Math.cos(deg2rad(y2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.

        dist = Math.round(dist*100)/100.0;
        return dist;
    }
    private double deg2rad(double deg){
        return (double)(deg * Math.PI / (double)180d);
    }
    private double rad2deg(double rad){
        return (double)(rad * (double)180d / Math.PI);
    }
}
