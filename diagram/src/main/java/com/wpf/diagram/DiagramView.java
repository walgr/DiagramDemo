package com.wpf.diagram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by 王朋飞 on 10-26-0026.
 * 图表
 */

public class DiagramView extends SurfaceView implements
        SurfaceHolder.Callback2, Runnable ,View.OnTouchListener {

    private DiagramInfo diagramInfo;
    private Paint paint_LineChart,paint_Diagram_Peak, paint_Diagram_Peak_Line,paint_Diagram_Point,
            paint_XLine,paint_YLine, paint_YLinePointLine,paint_Bar, paint_Text;
    private int width_XLine,width_YLine,width_LineChart,width_Diagram_Peak_Line,width_Bar,size_text;
    private int color_LineChart,color_Diagram_Peak, color_Diagram_Peak_Line,color_Diagram_Point,color_XLine,color_YLine,
            color_YLinePointLine,color_Bar;
    private boolean isRun,isShowYLine,isShowLineChart,isShowYLinePointLine,isShowPeak,isShowPeakPoint,
            isShowPeakLine,isShowPeakPointText, isShowBar;
    private int maxXLine;

    //不需要设置的参数
    private final SurfaceHolder holder;
    private Canvas canvas;
    private Thread drawThread;
    private float[] xy_X = new float[2];
    private float[] xy_Y = new float[2];
    private int[] x_XLine,y_YLine;
    private int length_X,length_Y,left;
    private float oldX;
    private int width,height;

    public DiagramView(Context context) {
        this(context,null);
    }

    public DiagramView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DiagramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        holder = getHolder();
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);

        TypedArray typedArray =  context.obtainStyledAttributes(attrs, R.styleable.DiagramView,defStyleAttr,0);
        isShowYLine = typedArray.getBoolean(R.styleable.DiagramView_isShow_YLine,true);
        isShowLineChart = typedArray.getBoolean(R.styleable.DiagramView_isShowLineChart,false);
        isShowYLinePointLine = typedArray.getBoolean(R.styleable.DiagramView_isShow_YLinePointLine,false);
        isShowPeak = typedArray.getBoolean(R.styleable.DiagramView_isShow_Peak,false);
        isShowPeakLine = typedArray.getBoolean(R.styleable.DiagramView_isShow_Peak_Line,false);
        isShowPeakPoint = typedArray.getBoolean(R.styleable.DiagramView_isShow_Peak_Point,false);
        isShowBar = typedArray.getBoolean(R.styleable.DiagramView_isShow_Bar,false);
        isShowPeakPointText = typedArray.getBoolean(R.styleable.DiagramView_isShow_Peak_Point_Text,true);

        color_XLine = typedArray.getColor(R.styleable.DiagramView_color_XLine,Color.GRAY);
        color_YLine = typedArray.getColor(R.styleable.DiagramView_color_YLine,Color.GRAY);
        color_YLinePointLine = typedArray.getColor(R.styleable.DiagramView_color_YLinePointLine,Color.GRAY);
        color_LineChart = typedArray.getColor(R.styleable.DiagramView_color_LineChart,Color.GREEN);
        color_Diagram_Peak = typedArray.getColor(R.styleable.DiagramView_color_Diagram_Peak,Color.RED);
        color_Diagram_Peak_Line = typedArray.getColor(R.styleable.DiagramView_color_Diagram_Peak_Line,Color.RED);
        color_Diagram_Point = typedArray.getColor(R.styleable.DiagramView_color_Diagram_Peak_Point,Color.RED);
        color_Bar = typedArray.getColor(R.styleable.DiagramView_color_Bar,Color.RED);

        width_XLine = typedArray.getInt(R.styleable.DiagramView_width_XLine,3);
        width_YLine = typedArray.getInt(R.styleable.DiagramView_width_YLine,3);
        width_LineChart = typedArray.getInt(R.styleable.DiagramView_width_LineChart,5);
        width_Diagram_Peak_Line = typedArray.getInt(R.styleable.DiagramView_width_Diagram_Peak_Line,5);
        width_Bar = typedArray.getInt(R.styleable.DiagramView_width_Bar,56);
        size_text = typedArray.getInt(R.styleable.DiagramView_size_text,42);

        maxXLine = typedArray.getInt(R.styleable.DiagramView_maxXLine,5)-1;
        if(maxXLine<1) maxXLine = 1;

        typedArray.recycle();
    }

    private void init() {
        if(diagramInfo == null) diagramInfo = new DiagramInfo();
        width = getWidth();
        height = getHeight();
        paint_LineChart = new Paint();
        paint_LineChart.setStrokeWidth(width_LineChart);
        paint_LineChart.setColor(color_LineChart);
        paint_LineChart.setStyle(Paint.Style.STROKE);
        paint_LineChart.setAntiAlias(true);

        paint_Diagram_Peak = new Paint();
        paint_Diagram_Peak.setStyle(Paint.Style.FILL);
        paint_Diagram_Peak.setColor(color_Diagram_Peak);
        paint_Diagram_Peak.setStrokeWidth(1);
        paint_Diagram_Peak.setAntiAlias(true);

        paint_Diagram_Peak_Line = new Paint();
        paint_Diagram_Peak_Line.setStyle(Paint.Style.STROKE);
        paint_Diagram_Peak_Line.setColor(color_Diagram_Peak_Line);
        paint_Diagram_Peak_Line.setStrokeWidth(width_Diagram_Peak_Line);
        paint_Diagram_Peak_Line.setAntiAlias(true);

        paint_Diagram_Point = new Paint();
        paint_Diagram_Point.setStyle(Paint.Style.FILL);
        paint_Diagram_Point.setColor(color_Diagram_Point);
        paint_Diagram_Point.setStrokeWidth(1);
        paint_Diagram_Point.setAntiAlias(true);

        paint_Bar = new Paint();
        paint_Bar.setStyle(Paint.Style.FILL);
        paint_Bar.setColor(color_Bar);
        paint_Bar.setStrokeWidth(1);
        paint_Bar.setAntiAlias(true);

        paint_XLine = new Paint();
        paint_XLine.setColor(color_XLine);
        paint_XLine.setStrokeWidth(width_XLine);
        paint_XLine.setAntiAlias(true);

        paint_YLine = new Paint();
        paint_YLine.setColor(color_YLine);
        paint_YLine.setStrokeWidth(width_YLine);
        paint_YLine.setAntiAlias(true);

        paint_YLinePointLine = new Paint();
        paint_YLinePointLine.setColor(color_YLinePointLine);
        paint_YLinePointLine.setStrokeWidth(1);
        paint_YLinePointLine.setAntiAlias(true);

        paint_Text = new Paint();
        paint_Text.setColor(Color.GRAY);
        paint_Text.setTextSize(size_text);

        calLocation();
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        init();
        if(drawThread == null) drawThread = new Thread(this);
        if(!drawThread.isAlive()) startAll();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(drawThread != null && !drawThread.isAlive()) startAll();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(drawThread != null && drawThread.isAlive()) stopAll();
    }

    public void setDiagramInfo(DiagramInfo diagramInfo) {
        if(diagramInfo == null) return;
        this.diagramInfo = diagramInfo;
        holder.addCallback(this);
    }

    private void calLocation() {
        int bp_XLine = width / 10,bp_YLine = height / 10;
        xy_X[0] = (int) getTextWidth(String.valueOf(diagramInfo.getYLineMax()));
        xy_X[1] = height - bp_YLine;
        xy_Y[0] = (int) getTextWidth(String.valueOf(diagramInfo.getYLineMax()));
        xy_Y[1] = 0;
        length_X = width - bp_XLine;
        length_Y = height - bp_YLine;

        int start_XLine = (int) (xy_X[0]),
                end_XLine = (int) (xy_X[0]+length_X- bp_XLine /2),
                start_YLine = (int) (xy_Y[1]+length_Y),
                end_YLine = (int) (xy_Y[1]+ bp_YLine /2);
        int len_XLineText = end_XLine - start_XLine,len_YLineText = start_YLine - end_YLine;
        int size = diagramInfo.points.size();
        double len_XLinePart = len_XLineText / (diagramInfo.getXLineRange()>maxXLine?maxXLine:diagramInfo.getXLineRange()),
                len_YLinePart = len_YLineText / diagramInfo.getYLineRange();
        x_XLine = new int[size];
        y_YLine = new int[size];

        for(int i =0;i<size;++i) {
            x_XLine[i] = (int) (start_XLine + len_XLinePart *(diagramInfo.points.get(i).x-diagramInfo.getXLineMin()));
            y_YLine[i] = (int) (start_YLine - len_YLinePart *(diagramInfo.points.get(i).y-diagramInfo.getYLineMin()));
        }
        left = x_XLine[0];
    }

    @Override
    public void run() {
        while (isRun) {
            synchronized(holder) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) drawAll(canvas);
                holder.unlockCanvasAndPost(canvas);
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
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = motionEvent.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveDiagram((int) (motionEvent.getX() - oldX));
                oldX = motionEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                oldX = 0;
                break;
        }
        return true;
    }

    private void drawNumLine() {
        drawNumLineByXLength(length_X);
        drawNumLineByYLength(length_Y);
        drawNumDiagram();
        drawLineText();
    }

    //移动绘画
    private void moveDiagram(int x) {
        if(x_XLine[0]+x>left) x=left-x_XLine[0];
        if(x_XLine[x_XLine.length-1] + x < length_X) x=length_X-x_XLine[x_XLine.length-1];
        for(int i=0;i<x_XLine.length;++i) x_XLine[i]+=x;
        reDraw();
    }

    private void drawNumDiagram() {
        for (int i = 0; i < diagramInfo.points.size(); i++)
            if(isShowBar) canvas.drawRect(x_XLine[i],y_YLine[i],x_XLine[i] + width_Bar, xy_X[1],paint_Bar);
        for (int i = 0; i < diagramInfo.points.size(); i++) {
            if(i < diagramInfo.points.size() - 1) {
                android.graphics.Point point_start = new android.graphics.Point(x_XLine[i]+getBarWidth(), y_YLine[i]);
                android.graphics.Point point_end = new android.graphics.Point(x_XLine[i+1]+getBarWidth(), y_YLine[i+1]);
                int wt = (point_start.x + point_end.x) / 2;
                android.graphics.Point p3 = new android.graphics.Point();
                android.graphics.Point p4 = new android.graphics.Point();
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
                if(isShowPeakLine) canvas.drawPath(path, paint_Diagram_Peak_Line);
                if(isShowLineChart) canvas.drawLine(point_start.x,point_start.y,point_end.x,point_end.y,paint_LineChart);
            }
            if(isShowPeakPoint) canvas.drawCircle(x_XLine[i]+getBarWidth(),y_YLine[i],paint_Diagram_Peak_Line.getStrokeWidth()+5,paint_Diagram_Point);
            drawNumDiagramPointText(i);
        }
    }

    private int getBarWidth() {
        if(isShowBar) return width_Bar/2;
        else return 0;
    }

    //提示数字
    private void drawNumDiagramPointText(int i) {
        if(isShowPeakPointText) canvas.drawText(diagramInfo.yLineName.get(i),
                x_XLine[i]-getTextWidth(diagramInfo.yLineName.get(i))/2+getBarWidth(),
                y_YLine[i]-getTextHeight(), paint_Text);
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
        String xName = i>=diagramInfo.xLineName.size()?"":diagramInfo.xLineName.get(i);
        canvas.drawText(xName,x_XLine[i]-getTextWidth(xName)/2 +getBarWidth(),xy_X[1]+50,paint_Text);
        drawXLinePointText();
    }

    //X轴提示文字
    private void drawXLinePointText() {
        if(!diagramInfo.xLinePointText.isEmpty()) canvas.drawText(diagramInfo.xLinePointText,width - getTextWidth(diagramInfo.xLinePointText),xy_X[1]+100,paint_Text);
    }

    //Y轴文字
    private void drawYLineText(int i) {
        if(isShowYLinePointLine) {
            String yName = i >= diagramInfo.yLineName.size() ? "" : diagramInfo.yLineName.get(i);
            canvas.drawText(yName, xy_Y[0] - getTextWidth(String.valueOf(diagramInfo.getYLineMax())), y_YLine[i] + getTextHeight(), paint_Text);
        }
        drawYLinePointText();
    }

    //Y轴提示文字
    private void drawYLinePointText() {
        if(!diagramInfo.yLinePointText.isEmpty()) canvas.drawText(diagramInfo.yLinePointText,0,50,paint_Text);
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

    public void startAll() {
        isRun = true;
        new Thread(this).start();
    }

    public void stopAll() {
        isRun = false;
    }

    public void reStart() {
        init();
        stopAll();
        reDraw();
    }

    private void reDraw() {
        canvas = holder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        drawAll(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    public int getColor_Bar() {
        return color_Bar;
    }

    public void setColor_Bar(int color_Bar) {
        this.color_Bar = color_Bar;
    }

    public int getColor_Diagram_Peak() {
        return color_Diagram_Peak;
    }

    public void setColor_Diagram_Peak(int color_Diagram_Peak) {
        this.color_Diagram_Peak = color_Diagram_Peak;
    }

    public int getColor_Diagram_Peak_Line() {
        return color_Diagram_Peak_Line;
    }

    public void setColor_Diagram_Peak_Line(int color_Diagram_Peak_Line) {
        this.color_Diagram_Peak_Line = color_Diagram_Peak_Line;
    }

    public int getColor_Diagram_Point() {
        return color_Diagram_Point;
    }

    public void setColor_Diagram_Point(int color_Diagram_Point) {
        this.color_Diagram_Point = color_Diagram_Point;
    }

    public int getColor_LineChart() {
        return color_LineChart;
    }

    public void setColor_LineChart(int color_LineChart) {
        this.color_LineChart = color_LineChart;
    }

    public int getColor_XLine() {
        return color_XLine;
    }

    public void setColor_XLine(int color_XLine) {
        this.color_XLine = color_XLine;
    }

    public int getColor_YLine() {
        return color_YLine;
    }

    public void setColor_YLine(int color_YLine) {
        this.color_YLine = color_YLine;
    }

    public int getColor_YLinePointLine() {
        return color_YLinePointLine;
    }

    public void setColor_YLinePointLine(int color_YLinePointLine) {
        this.color_YLinePointLine = color_YLinePointLine;
    }

    public DiagramInfo getDiagramInfo() {
        return diagramInfo;
    }

    public boolean isShowBar() {
        return isShowBar;
    }

    public void setShowBar(boolean showBar) {
        isShowBar = showBar;
    }

    public boolean isShowLineChart() {
        return isShowLineChart;
    }

    public void setShowLineChart(boolean showLineChart) {
        isShowLineChart = showLineChart;
    }

    public boolean isShowPeak() {
        return isShowPeak;
    }

    public void setShowPeak(boolean showPeak) {
        isShowPeak = showPeak;
    }

    public boolean isShowPeakLine() {
        return isShowPeakLine;
    }

    public void setShowPeakLine(boolean showPeakLine) {
        isShowPeakLine = showPeakLine;
    }

    public boolean isShowPeakPoint() {
        return isShowPeakPoint;
    }

    public void setShowPeakPoint(boolean showPeakPoint) {
        isShowPeakPoint = showPeakPoint;
    }

    public boolean isShowPeakPointText() {
        return isShowPeakPointText;
    }

    public void setShowPeakPointText(boolean showPeakPointText) {
        isShowPeakPointText = showPeakPointText;
    }

    public boolean isShowYLine() {
        return isShowYLine;
    }

    public void setShowYLine(boolean showYLine) {
        isShowYLine = showYLine;
    }

    public boolean isShowYLinePointLine() {
        return isShowYLinePointLine;
    }

    public void setShowYLinePointLine(boolean showYLinePointLine) {
        isShowYLinePointLine = showYLinePointLine;
    }

    public int getMaxXLine() {
        return maxXLine;
    }

    public void setMaxXLine(int maxXLine) {
        this.maxXLine = maxXLine;
    }

    public int getSize_text() {
        return size_text;
    }

    public void setSize_text(int size_text) {
        this.size_text = size_text;
    }

    public int getWidth_Bar() {
        return width_Bar;
    }

    public void setWidth_Bar(int width_Bar) {
        this.width_Bar = width_Bar;
    }

    public int getWidth_Diagram_Peak_Line() {
        return width_Diagram_Peak_Line;
    }

    public void setWidth_Diagram_Peak_Line(int width_Diagram_Peak_Line) {
        this.width_Diagram_Peak_Line = width_Diagram_Peak_Line;
    }

    public int getWidth_LineChart() {
        return width_LineChart;
    }

    public void setWidth_LineChart(int width_LineChart) {
        this.width_LineChart = width_LineChart;
    }

    public int getWidth_XLine() {
        return width_XLine;
    }

    public void setWidth_XLine(int width_XLine) {
        this.width_XLine = width_XLine;
    }

    public int getWidth_YLine() {
        return width_YLine;
    }

    public void setWidth_YLine(int width_YLine) {
        this.width_YLine = width_YLine;
    }
}
