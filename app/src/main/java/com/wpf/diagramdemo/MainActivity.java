package com.wpf.diagramdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.wpf.diagram.DiagramInfo;
import com.wpf.diagram.DiagramView;
import com.wpf.diagram.Point;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DiagramView diagramView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Point> points = new ArrayList<>();
        points.add(new Point(0,0));
        points.add(new Point(1,212.3));
        points.add(new Point(2,345.1));
        points.add(new Point(3,756.2));
        points.add(new Point(4,521.3));
        points.add(new Point(5,621.3));
        points.add(new Point(6,921.3));
        points.add(new Point(7,721.3));
        points.add(new Point(8,321.3));
        points.add(new Point(9,0));

        ArrayList xLineName = new ArrayList<String>() {{add("2014");add("2015");add("2017");add("2018");add("2020");add("2021");add("2022");}};

        DiagramInfo diagramInfo = new DiagramInfo(points);
        diagramInfo.setXLineName(xLineName);
        diagramInfo.setXLinePointText("时间");
        diagramInfo.setYLinePointText("MB");

        diagramView = (DiagramView)findViewById(R.id.diagram);
        diagramView.setDiagramInfo(diagramInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_showY:
                diagramView.setShowYLine(!diagramView.isShowYLine());
                break;
            case R.id.action_showY_Number:
                diagramView.setShowYLinePointLine(!diagramView.isShowYLinePointLine());
                break;
            case R.id.action_line:
                diagramView.setShowLineChart(!diagramView.isShowLineChart());
                break;
            case R.id.action_bar:
                diagramView.setShowBar(!diagramView.isShowBar());
                break;
            case R.id.action_peak:
                diagramView.setShowPeak(!diagramView.isShowPeak());
                break;
            case R.id.action_peak_line:
                diagramView.setShowPeakLine(!diagramView.isShowPeakLine());
                break;
            case R.id.action_peak_point:
                diagramView.setShowPeakPoint(!diagramView.isShowPeakPoint());
                break;
            case R.id.action_peak_point_text:
                diagramView.setShowPeakPointText(!diagramView.isShowPeakPointText());
                break;
        }
        diagramView.reStart();
        return super.onOptionsItemSelected(item);
    }
}
