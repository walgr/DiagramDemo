package com.wpf.diagram;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
        SurfaceHolder.Callback2, View.OnTouchListener {

    private DiagramInfo diagramInfo;
    private Paint paint_LineChart,paint_Diagram_Peak, paint_Diagram_Peak_Line,paint_Diagram_Point,
            paint_XLine,paint_YLine, paint_YLinePointLine,paint_Bar, paint_XYLineText,paint_Point_Text;
    private int width_XLine,width_YLine,width_LineChart,width_Diagram_Peak_Line,width_Bar, size_XYText,size_Point_Text;
    private int color_LineChart,color_Diagram_Peak, color_Diagram_Peak_Line,color_Diagram_Point,color_XLine,color_YLine,
            color_YLinePointLine,color_Bar, color_XYText,color_Point_Text;
    private boolean isShowYLine,isShowLineChart,isShowYLinePointLine,isShowPeak,isShowPeakPoint,
            isShowPeakLine,isShowPeakPointText, isShowBar;
    private int maxXLine,start_YLine;
    private Drawable image_Point;
    private Bitmap bmp_Point;
    private Rect rect_f = new Rect();

    //不需要设置的参数
    private final SurfaceHolder holder;
    private Canvas canvas;
    private int[] xy_X = new int[2];
    private int[] xy_Y = new int[2];
    private int[] x_XLine,y_YLine;
    private int length_X,length_Y,left;
    private float oldX,move_PointX;
    private int width,height;
    private ValueAnimator va_XLine;

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
        color_XYText = typedArray.getColor(R.styleable.DiagramView_color_XYLineText,Color.GRAY);
        color_Point_Text = typedArray.getColor(R.styleable.DiagramView_color_PointText,Color.GRAY);

        width_XLine = typedArray.getInt(R.styleable.DiagramView_width_XLine,3);
        width_YLine = typedArray.getInt(R.styleable.DiagramView_width_YLine,3);
        width_LineChart = typedArray.getInt(R.styleable.DiagramView_width_LineChart,5);
        width_Diagram_Peak_Line = typedArray.getInt(R.styleable.DiagramView_width_Diagram_Peak_Line,5);
        width_Bar = typedArray.getInt(R.styleable.DiagramView_width_Bar,56);
        size_XYText = typedArray.getInt(R.styleable.DiagramView_size_XYText,42);
        size_Point_Text = typedArray.getInt(R.styleable.DiagramView_size_PointText,36);

        maxXLine = typedArray.getInt(R.styleable.DiagramView_maxXLine,1)-1;
        start_YLine = typedArray.getInt(R.styleable.DiagramView_start_YLine,0);

        image_Point = typedArray.getDrawable(R.styleable.DiagramView_point_BackImage);

        typedArray.recycle();
    }

    private void init() {
        if (diagramInfo == null) diagramInfo = new DiagramInfo();
        width = getWidth();
        height = getHeight();
        if(paint_XLine == null) {
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

            paint_XYLineText = new Paint();
            paint_XYLineText.setColor(color_XYText);
            paint_XYLineText.setTextSize(size_XYText);

            paint_Point_Text = new Paint();
            paint_Point_Text.setColor(color_Point_Text);
            paint_Point_Text.setTextSize(size_Point_Text);
        }
        calLocation();
        va_XLine = ValueAnimator.ofInt(0,length_X);
        va_XLine.setDuration(200);
        va_XLine.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                length_X = (int) valueAnimator.getAnimatedValue();
                reDraw();
            }
        });
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        init();
        startAll();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//        reStart();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public void setDiagramInfo(DiagramInfo diagramInfo) {
        if(diagramInfo == null) return;
        this.diagramInfo = diagramInfo;
        if(maxXLine == 0) maxXLine = diagramInfo.points.size();
        if(maxXLine<1) maxXLine = 1;
        holder.addCallback(this);
    }

    private void calLocation() {
        int bp_XLine = width / 10,bp_YLine = height / 10;
        xy_X[0] = (int) getTextWidth(paint_XYLineText,String.valueOf(diagramInfo.getYLineMax()));
        xy_X[1] = height - bp_YLine;
        xy_Y[0] = (int) getTextWidth(paint_XYLineText,String.valueOf(diagramInfo.getYLineMax()));
        xy_Y[1] = 0;
        length_X = width-xy_X[0]-bp_XLine;
        length_Y = height - bp_YLine;

        int start_XLine = xy_X[0],
                end_XLine = xy_X[0]+length_X,
                start_YLine = xy_Y[1]+length_Y,
                end_YLine = xy_Y[1]+bp_YLine + getPointTextAndImageHeight();
        int len_XLineText = end_XLine - start_XLine,len_YLineText = start_YLine - end_YLine;
        int size = diagramInfo.points.size();
        double len_XLinePart = len_XLineText / (diagramInfo.getXLineRange()>maxXLine?maxXLine:diagramInfo.getXLineRange()),
                len_YLinePart = len_YLineText / diagramInfo.getYLineRange();
        x_XLine = new int[size];
        y_YLine = new int[size];

        for (int i = 0; i < size; ++i) {
            x_XLine[i] = (int) (start_XLine + len_XLinePart * (diagramInfo.points.get(i).x - diagramInfo.getXLineMin()));
            y_YLine[i] = (int) (start_YLine - len_YLinePart * (diagramInfo.points.get(i).y - this.start_YLine));
        }
        left = x_XLine[0];
    }

    private void draw() {
        synchronized(holder) {
            drawAll();
            holder.unlockCanvasAndPost(canvas);
            if(!va_XLine.isStarted()) va_XLine.start();
        }
        setOnTouchListener(this);
    }

    private void drawAll() {
        drawNumLineByXLength(length_X+getBarWidth());
        drawNumLineByYLength(length_Y);
        drawNumDiagram();
        drawLineText();
        drawXLinePointLine();
        drawNumDiagramPointText(getMoveOnPoint((int) move_PointX));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = motionEvent.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                move_PointX = motionEvent.getX();
                moveDiagram(motionEvent.getX() - oldX);
                oldX = motionEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                oldX = 0;
                move_PointX = 0;
                break;
        }
        reDraw();
        return true;
    }

    //移动绘画
    private void moveDiagram(float x) {
        if(x_XLine[0]+x>left) x=left-x_XLine[0];
        if(x_XLine[x_XLine.length-1] + x < length_X + left) x=length_X+left-x_XLine[x_XLine.length-1];
        for(int i=0;i<x_XLine.length;++i) x_XLine[i]+=x;
    }

    private int getMoveOnPoint(int x) {
        for(int i = 0;i<x_XLine.length;++i) {
            if(x>=x_XLine[i]-20 && x<=x_XLine[i]+20) return i;
        }
        return -1;
    }

    //画数据图
    private void drawNumDiagram() {
        for (int i = 0; i < diagramInfo.points.size(); i++)
            if(isShowBar) getCanvas().drawRect(x_XLine[i],y_YLine[i],x_XLine[i] + width_Bar, xy_X[1],paint_Bar);
        for (int i = 0; i < diagramInfo.points.size(); i++) {
            if(i < diagramInfo.points.size() - 1) {
                android.graphics.Point point_start = new android.graphics.Point(x_XLine[i]+getBarWidth()/2, y_YLine[i]);
                android.graphics.Point point_end = new android.graphics.Point(x_XLine[i+1]+getBarWidth()/2, y_YLine[i+1]);
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
                    getCanvas().drawPath(path_peak, paint_Diagram_Peak);
                }
                if(isShowPeakLine) getCanvas().drawPath(path, paint_Diagram_Peak_Line);
                if(isShowLineChart) getCanvas().drawLine(point_start.x,point_start.y,point_end.x,point_end.y,paint_LineChart);
            }
            if(isShowPeakPoint) getCanvas().drawCircle(x_XLine[i]+getBarWidth()/2,y_YLine[i],paint_Diagram_Peak_Line.getStrokeWidth()+5,paint_Diagram_Point);
            if(isShowPeakPointText) drawNumDiagramPointText(i);
        }
    }

    private Canvas getCanvas() {
        if(canvas == null) canvas = holder.lockCanvas();
        return canvas;
    }

    private int getBarWidth() {
        if(isShowBar) return width_Bar;
        else return 0;
    }

    private int getPointTextAndImageHeight() {
        if(maxXLine == diagramInfo.points.size()) return image_Point.getIntrinsicHeight();
        if(isShowPeakPointText) return image_Point.getIntrinsicHeight();
        else return 0;
    }

    //提示数字
    private void drawNumDiagramPointText(int i) {
        if(i == -1) return;
        if(bmp_Point == null) {
            bmp_Point = drawableToBitmap(image_Point);
            rect_f = new Rect(0,0, image_Point.getIntrinsicWidth(),image_Point.getIntrinsicHeight());
        }
        Rect rect_t = new Rect(x_XLine[i]-image_Point.getIntrinsicWidth()/2 + getBarWidth()/2,
                y_YLine[i]-image_Point.getIntrinsicHeight()-10,
                x_XLine[i]+image_Point.getIntrinsicWidth()/2 + getBarWidth()/2,
                y_YLine[i]-10);
        getCanvas().drawBitmap(bmp_Point,rect_f,rect_t, paint_Point_Text);
        getCanvas().drawText(diagramInfo.yLineName.get(i),
                x_XLine[i] - getTextWidth(paint_Point_Text,diagramInfo.yLineName.get(i)) / 2 + getBarWidth()/2,
                y_YLine[i] - getTextHeight(paint_XYLineText) - ((bmp_Point != null)?image_Point.getIntrinsicHeight()/2:0),
                paint_Point_Text);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable == null) return null;
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //X轴
    private void drawNumLineByXLength(int length) {
        getCanvas().drawLine(xy_X[0],xy_X[1],xy_X[0] + length,xy_X[1], paint_XLine);
    }

    //Y轴
    private void drawNumLineByYLength(int length) {
        if(isShowYLine) getCanvas().drawLine(xy_Y[0],xy_Y[1],xy_Y[0],xy_Y[1] + length, paint_YLine);
    }

    //所有文字
    private void drawLineText() {
        //getCanvas().drawText(String.valueOf(start_YLine),xy_Y[0]-getTextWidth(paint_XYLineText,String.valueOf(start_YLine)),xy_X[1]+getTextHeight(paint_XYLineText),paint_XYLineText);
        for(int i = 0;i<diagramInfo.points.size();++i) {
            drawXLineText(i);
            drawYLineText(i);
            drawYLinePointLine(i);
        }
    }

    //X轴文字
    private void drawXLineText(int i) {
        String xName = i>=diagramInfo.xLineName.size()?"":diagramInfo.xLineName.get(i);
        getCanvas().drawText(xName,x_XLine[i]-getTextWidth(paint_XYLineText,xName)/2 +getBarWidth()/2,xy_X[1]+50, paint_XYLineText);
        drawXLinePointText();
    }

    //X轴提示文字
    private void drawXLinePointText() {
        if(!diagramInfo.xLinePointText.isEmpty()) getCanvas().drawText(diagramInfo.xLinePointText,width-width/10+getBarWidth()/2-getTextWidth(paint_XYLineText,diagramInfo.xLinePointText),xy_X[1]+100, paint_XYLineText);
    }

    //Y轴文字
    private void drawYLineText(int i) {
        if(isShowYLinePointLine) {
            String yName = i >= diagramInfo.yLineName.size() ? "" : diagramInfo.yLineName.get(i);
            getCanvas().drawText(yName, xy_Y[0] - getTextWidth(paint_XYLineText,String.valueOf(diagramInfo.getYLineMax())), y_YLine[i] + getTextHeight(paint_XYLineText), paint_XYLineText);
        }
        drawYLinePointText();
    }

    //Y轴提示文字
    private void drawYLinePointText() {
        if(!diagramInfo.yLinePointText.isEmpty()) getCanvas().drawText(diagramInfo.yLinePointText,0,50, paint_XYLineText);
    }

    //Y轴标示线
    private void drawYLinePointLine(int i) {
        if(isShowYLinePointLine) getCanvas().drawLine(xy_Y[0],y_YLine[i],xy_Y[0]+length_X+getBarWidth(),y_YLine[i],paint_YLinePointLine);
    }

    //X轴提示线
    private void drawXLinePointLine() {
        if(move_PointX == 0) return;
        getCanvas().drawLine(move_PointX,xy_Y[1],move_PointX,xy_Y[1]+length_Y,paint_YLinePointLine);
    }

    //文字宽度
    private float getTextWidth(Paint paint, String text) {
        return paint.measureText(text);
    }

    //文字高度
    private float getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics= paint.getFontMetrics();
        return (-fontMetrics.ascent-fontMetrics.descent)/2;
    }

    public void startAll() {
        draw();
    }

    public void reStart() {
        init();
        reDraw();
    }

    public void reDraw() {
        canvas = holder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        drawAll();
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

    public int getColor_Point_Text() {
        return color_Point_Text;
    }

    public void setColor_Point_Text(int color_Point_Text) {
        this.color_Point_Text = color_Point_Text;
    }

    public int getColor_XLine() {
        return color_XLine;
    }

    public void setColor_XLine(int color_XLine) {
        this.color_XLine = color_XLine;
    }

    public int getColor_XYText() {
        return color_XYText;
    }

    public void setColor_XYText(int color_XYText) {
        this.color_XYText = color_XYText;
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

    public Drawable getImage_Point() {
        return image_Point;
    }

    public void setImage_Point(Drawable image_Point) {
        this.image_Point = image_Point;
    }

    public Bitmap getBmp_Point() {
        return bmp_Point;
    }

    public void setBmp_Point(Bitmap bmp_Point) {
        this.bmp_Point = bmp_Point;
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

    public int getStart_YLine() {
        return start_YLine;
    }

    public void setStart_YLine(int start_YLine) {
        this.start_YLine = start_YLine;
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
