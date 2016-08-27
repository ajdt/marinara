package com.itsaunixsystem.marinara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.itsaunixsystem.marinara.mock.MockSessionsLoader;
import com.itsaunixsystem.marinara.mock.Session;
import com.itsaunixsystem.marinara.stats.LineChartStats;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        createGraphs() ;
    }

    private void createGraphs() {
        // get sessions
        List<Session> sessions = (new MockSessionsLoader()).getAllSessions() ;

        createLineChart(sessions) ;
        createPieChart(sessions) ;
    }

    private void createLineChart(List<Session> sessions) {
        // get computed entries
        LineChartStats stats_obj = new LineChartStats(sessions) ;
        ArrayList<Entry> entries = stats_obj.getEntries() ;

        // create data and dataset objects for entries
        LineDataSet data_set = new LineDataSet(entries, "sessions per day") ; // TODO: strings.xml
        LineData data = new LineData(data_set) ;

        // set line chart data
        LineChart chart = (LineChart)findViewById(R.id.line_chart) ;
        chart.setDescription("sessions per day") ;
        chart.setData(data) ;
        chart.invalidate() ;
    }

    private void createPieChart(List<Session> sessions) {
        //throw new Exception() ;
    }

    // TODO:
    // (1) obtain sessions list
    // (2) initialize pieChart and LineChart with data
    // (3)
}
