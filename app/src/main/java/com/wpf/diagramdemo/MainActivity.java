package com.wpf.diagramdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wpf.diagram.DiagramInfo;
import com.wpf.diagram.DiagramView;
import com.wpf.diagram.Point;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Point> points = new ArrayList<>();
        points.add(new Point(1,123.5));
        points.add(new Point(2,212.3));
        points.add(new Point(3,345.1));
        points.add(new Point(4,756.2));
        points.add(new Point(6,521.3));

        ArrayList xLineName = new ArrayList<String>(){
            {add("2014");add("2015");add("2017");add("2018");add("2019");}};

        DiagramInfo diagramInfo = new DiagramInfo(points);
        diagramInfo.setXLineName(xLineName);
        diagramInfo.setXLinePointText("时间");
        diagramInfo.setYLinePointText("MB");
        ((DiagramView)findViewById(R.id.diagram)).setDiagramInfo(diagramInfo);
    }
}
