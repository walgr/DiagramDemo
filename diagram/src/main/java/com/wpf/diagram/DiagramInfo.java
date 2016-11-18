package com.wpf.diagram;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王朋飞 on 10-26-0026.
 * 图标数据
 */

public class DiagramInfo {
    //坐标点
    public List<Point> points = new ArrayList<>();
    //坐标轴上的文字
    public List<String> xLineName = new ArrayList<>(),yLineName = new ArrayList<>();
    //坐标轴的提示文字
    public String xLinePointText = "", yLinePointText = "";

    public double max_XLine = Integer.MIN_VALUE,max_YLine = Integer.MIN_VALUE,min_XLine = Integer.MAX_VALUE,min_YLine = Integer.MAX_VALUE;
    public double range_XLine = 0, range_YLine = 0;

    public DiagramInfo() {

    }

    public DiagramInfo(List<Point> points) {
        this.points = points;
        for(int i = 0;i<points.size();++i) {
            xLineName.add(String.valueOf(points.get(i).x));
            yLineName.add(String.valueOf(points.get(i).y));
        }
    }

    public double getXLineMax() {
        if(points.isEmpty()) return 0;
        if(max_XLine != Integer.MIN_VALUE) return max_XLine;
        for(Point point : points) {
            if(max_XLine < point.x) max_XLine = point.x;
        }
        return max_XLine;
    }

    public double getXLineMin() {
        if(points.isEmpty()) return 0;
        if(min_XLine != Integer.MAX_VALUE) return min_XLine;
        for(Point point : points) {
            if(min_XLine > point.x) min_XLine = point.x;
        }
        return min_XLine;
    }

    public double getYLineMax() {
        if(points.isEmpty()) return 0;
        if(max_YLine != Integer.MIN_VALUE) return max_YLine;
        for(Point point : points) {
            if(max_YLine < point.y) max_YLine = point.y;
        }
        return max_YLine;
    }

    public double getYLineMin() {
        if(points.isEmpty()) return 0;
        if(min_YLine != Integer.MAX_VALUE) return min_YLine;
        for(Point point : points) {
            if(min_YLine > point.y) min_YLine = point.y;
        }
        return min_YLine;
    }

    public double getXLineRange() {
        if(range_XLine != 0) return range_XLine;
        range_XLine = getXLineMax() - getXLineMin();
        return range_XLine;
    }

    public double getYLineRange() {
        if(range_YLine != 0) return range_YLine;
        range_YLine = getYLineMax() - getYLineMin();
        return range_YLine;
    }

    public List<String> getXLineName() {
        return xLineName;
    }

    public void setXLineName(List<String> xLineName) {
        this.xLineName = xLineName;
//        yLineName.clear();
//        for(Point point:points) {
//            yLineName.add(String.valueOf(point.y));
//        }
    }

    public List<String> getYLineName() {
        return yLineName;
    }

    public void setYLineName(List<String> yLineName) {
        this.yLineName = yLineName;
        xLineName.clear();
        for(Point point:points) {
            xLineName.add(String.valueOf(point.x));
        }
    }

    public String getXLinePointText() {
        return xLinePointText;
    }

    public void setXLinePointText(String xLinePointText) {
        this.xLinePointText = xLinePointText;
    }

    public String getYLinePointText() {
        return yLinePointText;
    }

    public void setYLinePointText(String yLinePointText) {
        this.yLinePointText = yLinePointText;
    }
}
