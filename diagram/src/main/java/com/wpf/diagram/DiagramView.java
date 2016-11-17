package com.wpf.diagram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by 王朋飞 on 10-26-0026.
 * 图表
 */

public class DiagramView extends SurfaceView implements
        SurfaceHolder.Callback2 {

    private SurfaceHolder holder;
    private DrawThread drawThread;
    private DiagramInfo diagramInfo;
    private Paint paint_LineChart,paint_Diagram_Peak, paint_Diagram_Peak_Line,paint_Diagram_Point,
            paint_XLine,paint_YLine, paint_YLinePointLine,paint_Bar, paint_Test;
    private int width_XLine,width_YLine,width_LineChart,width_Diagram_Peak_Line,width_Bar,size_text;
    private int color_LineChart,color_Diagram_Peak, color_Diagram_Peak_Line,color_Diagram_Point,color_XLine,color_YLine,
            color_YLinePointLine,color_Bar;
    private boolean isShowYLine,isShowLineChart,isShowYLinePointLine,isShowPeak,isShowPeakPoint,
            isShowPeakLine,isShowPeakPointText, isSHowBar;

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
        isSHowBar = typedArray.getBoolean(R.styleable.DiagramView_isShow_Bar,false);
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

        typedArray.recycle();
    }

    private void init() {
        if(diagramInfo == null) diagramInfo = new DiagramInfo();

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

        paint_Test = new Paint();
        paint_Test.setColor(Color.GRAY);
        paint_Test.setTextSize(size_text);
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        init();
        if(drawThread == null)
            drawThread = new DrawThread(surfaceHolder,diagramInfo,
                paint_LineChart,paint_Diagram_Peak, paint_Diagram_Peak_Line,paint_Diagram_Point,
                paint_Bar,paint_XLine,paint_YLine,paint_YLinePointLine,paint_Test,
                isShowYLine,isShowYLinePointLine,isShowLineChart,isShowPeak,isShowPeakLine,
                isShowPeakPoint,isSHowBar,isShowPeakPointText,
                width_Bar);
        if(!drawThread.isAlive()) drawThread.startAll();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(drawThread != null && !drawThread.isAlive()) drawThread.startAll();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(drawThread != null && drawThread.isAlive()) drawThread.stopAll();
    }

    public void setDiagramInfo(DiagramInfo diagramInfo) {
        if(diagramInfo == null) return;
        this.diagramInfo = diagramInfo;
        holder.addCallback(this);
    }
}
