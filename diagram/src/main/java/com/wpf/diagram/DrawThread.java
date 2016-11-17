package com.wpf.diagram;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.SurfaceHolder;

/**
 * Created by 王朋飞 on 10-26-0026.
 * 绘图线程
 */

class DrawThread extends Thread {
    private final SurfaceHolder surfaceHolder;
    private DiagramInfo diagramInfo;
    private Canvas canvas;
    private Paint paint_LineChart,paint_Diagram_Peak,paint_Diagram_Line,paint_Diagram_Point,paint_XLine, paint_YLine,
            paint_YLinePointLine,paint_Bar,paint_Text;
    private boolean isRun,isShowYLine,isShowLineChart, isShowYLinePointLine,isShowBar,isShowPeak,
            isShowPeakLine,isShowPeakPoint,isShowPeakPointText;
    private int width,height;
    //XY轴坐标
    private float[] xy_X = new float[2];
    private float[] xy_Y = new float[2];
    private int[] x_XLine,y_YLine;
    private int length_X, length_Y, width_Bar;

    DrawThread(SurfaceHolder surfaceHolder, DiagramInfo diagramInfo,
               Paint paint_LineChart, Paint paint_Diagram_Peak, Paint paint_Diagram_Line, Paint paint_Diagram_Point,
               Paint paint_Bar, Paint paint_XLine, Paint paint_YLine, Paint paint_YLinePointLine,
               Paint paint_Text,
               boolean isShowYLine, boolean isShowYLinePointLine, boolean isShowLineChart, boolean isShowPeak,
               boolean isShowPeakLine, boolean isShowPeakPoint, boolean isShowBar,boolean isShowPeakPointText,
                int width_Bar) {

        this.surfaceHolder = surfaceHolder;
        this.diagramInfo = diagramInfo;
        this.width = surfaceHolder.getSurfaceFrame().width();
        this.height = surfaceHolder.getSurfaceFrame().height();

        this.paint_LineChart = paint_LineChart;
        this.paint_Diagram_Peak  = paint_Diagram_Peak;
        this.paint_Diagram_Line  = paint_Diagram_Line;
        this.paint_Diagram_Point  = paint_Diagram_Point;
        this.paint_Bar  = paint_Bar;
        this.paint_XLine = paint_XLine;
        this.paint_YLine = paint_YLine;
        this.paint_YLinePointLine = paint_YLinePointLine;
        this.paint_Text = paint_Text;

        this.isShowYLine = isShowYLine;
        this.isShowYLinePointLine = isShowYLinePointLine;
        this.isShowLineChart = isShowLineChart;
        this.isShowPeak = isShowPeak;
        this.isShowPeakLine = isShowPeakLine;
        this.isShowPeakPoint = isShowPeakPoint;
        this.isShowBar = isShowBar;
        this.isShowPeakPointText = isShowPeakPointText;

        this.width_Bar = width_Bar;
        calLocation();
    }

    private void calLocation() {
        int bp_XLine = width/10,bp_YLine = height/10;
        xy_X[0] = bp_XLine;
        xy_X[1] = height - bp_YLine;
        xy_Y[0] = bp_XLine;
        xy_Y[1] = 0;
        length_X = width - bp_XLine;
        length_Y = height - bp_YLine;

        int start_XLine = (int) (xy_X[0]+bp_XLine/2),
                end_XLine = (int) (xy_X[0]+length_X-bp_XLine/2),
                start_YLine = (int) (xy_Y[1]+length_Y-bp_YLine/2),
                end_YLine = (int) (xy_Y[1]+bp_YLine/2);
        int len_XLineText = end_XLine - start_XLine,len_YLineText = start_YLine - end_YLine;
        int size = diagramInfo.points.size();
        if(size > 5) size = 5;
        double len_XLinePart = len_XLineText / diagramInfo.getXLineRange(),
                len_YLinePart = len_YLineText / diagramInfo.getYLineRange();
        x_XLine = new int[size];
        y_YLine = new int[size];

        for(int i =0;i<size;++i) {
            x_XLine[i] = (int) (start_XLine + len_XLinePart *(diagramInfo.points.get(i).x-diagramInfo.getXLineMin()));
            y_YLine[i] = (int) (start_YLine - len_YLinePart *(diagramInfo.points.get(i).y-diagramInfo.getYLineMin()));
        }
    }

    @Override
    public void run() {
        super.run();
        while (isRun) {
            synchronized(surfaceHolder) {
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas != null) drawAll(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
                stopAll();
                try {
                    Thread.sleep(17);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawAll(Canvas canvas) {
        this.canvas = canvas;
        drawNumLine();
    }

    private void drawNumLine() {
        drawNumLineByXLength(length_X);
        drawNumLineByYLength(length_Y);
        drawNumDiagram();
        drawLineText();
    }

    private void drawNumDiagram() {
        for (int i = 0; i < diagramInfo.points.size(); i++)
            if(isShowBar) canvas.drawRect(x_XLine[i]-width_Bar/2,y_YLine[i],x_XLine[i] + width_Bar/2, xy_X[1],paint_Bar);
        for (int i = 0; i < diagramInfo.points.size(); i++) {
            drawNumDiagramPointText(i);
            if(i < diagramInfo.points.size() - 1) {
                Point point_start = new Point(x_XLine[i], y_YLine[i]);
                Point point_end = new Point(x_XLine[i + 1], y_YLine[i + 1]);
                int wt = (point_start.x + point_end.x) / 2;
                Point p3 = new Point();
                Point p4 = new Point();
                p3.y = point_start.y;
                p3.x = wt;
                p4.y = point_end.y;
                p4.x = wt;

                Path path = new Path();
                path.moveTo(point_start.x, point_start.y);
                path.cubicTo(p3.x, p3.y, p4.x, p4.y, point_end.x, point_end.y);
                if(isShowPeak) {
                    Path path_peak = new Path(path);
                    path_peak.lineTo(point_end.x,xy_X[1]);
                    path_peak.lineTo(point_start.x,xy_X[1]);
                    path_peak.lineTo(point_start.x,point_start.y);
                    canvas.drawPath(path_peak, paint_Diagram_Peak);
                }
                if(isShowPeakLine) canvas.drawPath(path, paint_Diagram_Line);
                if(isShowLineChart) canvas.drawLine(point_start.x,point_start.y,point_end.x,point_end.y,paint_LineChart);
            }
            if(isShowPeakPoint) canvas.drawCircle(x_XLine[i],y_YLine[i],paint_Diagram_Line.getStrokeWidth()+5,paint_Diagram_Point);
        }
    }

    private void drawNumDiagramPointText(int i) {
        if(isShowPeakPointText) canvas.drawText(diagramInfo.yLineName.get(i),
                x_XLine[i]-getTextWidth(diagramInfo.yLineName.get(i))/2,y_YLine[i]-20,paint_Text);
    }

    //X轴
    private void drawNumLineByXLength(int length) {
        canvas.drawLine(xy_X[0],xy_X[1],xy_X[0] + length,xy_X[1], paint_XLine);
    }

    //Y轴
    private void drawNumLineByYLength(int length) {
        if(isShowYLine) canvas.drawLine(xy_Y[0],xy_Y[1],xy_Y[0],xy_Y[1] + length, paint_YLine);
    }

    //所有文字
    private void drawLineText() {
        for(int i = 0;i<diagramInfo.points.size();++i) {
            drawXLineText(i);
            drawYLineText(i);
            drawYLinePointLine(i);
        }
    }

    //X轴文字
    private void drawXLineText(int i) {
        canvas.drawText(diagramInfo.xLineName.get(i),x_XLine[i]-getTextWidth(diagramInfo.xLineName.get(i))/2,xy_X[1]+50,paint_Text);
        drawXLinePointText();
    }

    //X轴提示文字
    private void drawXLinePointText() {
        canvas.drawText(diagramInfo.xLinePointText,length_X+xy_X[0]/2 - getTextWidth(diagramInfo.xLinePointText),xy_X[1]+100,paint_Text);
        drawYLinePointText();
    }

    //Y轴文字
    private void drawYLineText(int i) {
        canvas.drawText(diagramInfo.yLineName.get(i),xy_Y[0]-100, y_YLine[i] + getTextHeight(),paint_Text);
    }

    //Y轴提示文字
    private void drawYLinePointText() {
        canvas.drawText(diagramInfo.yLinePointText,xy_X[0]-50-getTextWidth(diagramInfo.yLinePointText),50,paint_Text);
    }

    //Y轴标示线
    private void drawYLinePointLine(int i) {
        if(isShowYLinePointLine) canvas.drawLine(xy_Y[0],y_YLine[i],xy_Y[0]+length_X,y_YLine[i],paint_YLinePointLine);
    }

    //文字宽度
    private float getTextWidth(String text) {
        return paint_Text.measureText(text);
    }

    //文字高度
    private float getTextHeight() {
        Paint.FontMetrics fontMetrics= paint_Text.getFontMetrics();
        return (-fontMetrics.ascent-fontMetrics.descent)/2;
    }

    void startAll() {
        isRun = true;
        start();
    }

    void stopAll() {
        isRun = false;
    }
}
