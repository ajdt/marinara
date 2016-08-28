package com.itsaunixsystem.marinara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.itsaunixsystem.marinara.mock.MockSessionsLoader;
import com.itsaunixsystem.marinara.mock.Session;
import com.itsaunixsystem.marinara.stats.LineChartStats;
import com.itsaunixsystem.marinara.stats.PieChartStats;
import com.itsaunixsystem.marinara.util.DateRange;
import com.itsaunixsystem.marinara.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] _spinner_time_intervals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // init spinner and set listener
        initSelectedTimeSpinner() ;
        Spinner spinner = (Spinner)findViewById(R.id.selected_time_spinner) ;
        spinner.setOnItemSelectedListener(this) ;

        // load time interval and create graphs
        _spinner_time_intervals = getResources().getStringArray(R.array.time_intervals_array) ;
        createGraphs() ;
    }

    /****************************** GRAPH CREATION ******************************/
    private void createGraphs() {
        // get sessions
        List<Session> sessions = this.getSessionsForSelectedInterval() ;

        createLineChart(sessions) ;
        createPieChart(sessions) ;
    }

    private void createLineChart(List<Session> sessions) {
        // get computed entries
        LineChartStats stats_obj = new LineChartStats(sessions) ;
        ArrayList<Entry> entries = stats_obj.getEntries() ;

        // create data and dataset objects for entries
        LineDataSet data_set = new LineDataSet(entries,
                this.getString(R.string.line_chart_description)) ;
        LineData data = new LineData(data_set) ;

        // set line chart data and display it
        LineChart chart = (LineChart)findViewById(R.id.line_chart) ;
        chart.setDescription(this.getString(R.string.line_chart_description)) ;
        chart.setData(data) ;
        chart.invalidate() ;
    }

    private void createPieChart(List<Session> sessions) {
        // get pie chart entries and labels
        PieChartStats pie_stats = new PieChartStats(sessions) ;
        ArrayList<PieEntry> entries = pie_stats.getEntries() ;

        // make data sets
        PieDataSet pie_data_set = new PieDataSet(entries,
                this.getString(R.string.pie_chart_description)) ;
        pie_data_set.setColors(ColorTemplate.MATERIAL_COLORS) ; // set colors
        PieData pie_data = new PieData(pie_data_set) ;

        // add data sets to pie chart object
        PieChart pie_chart = (PieChart)findViewById(R.id.pie_chart) ;
        pie_chart.setData(pie_data) ;
        pie_chart.setDescription(this.getString(R.string.pie_chart_description)) ;

        // draw chart
        pie_chart.invalidate() ;
    }

    /****************************** SPINNER CALLBACKS ******************************/

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        createGraphs() ;
    }

    public void onNothingSelected(AdapterView<?> parent) { }

    /****************************** HELPERS ******************************/

    private void initSelectedTimeSpinner() {
        // create adapter and initialize with time interval array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_intervals_array, android.R.layout.simple_spinner_item) ;

        // set drop down layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) ;

        // add adapter to spinner and set first item to selected
        Spinner spinner = (Spinner)findViewById(R.id.selected_time_spinner) ;
        spinner.setAdapter(adapter) ;
        spinner.setSelection(0) ;
    }

    // TODO: rewrite this method to use DB data instead of mock data
    private List<Session> getSessionsForSelectedInterval() {
        // get selected spinner item
        Spinner spinner = (Spinner)findViewById(R.id.selected_time_spinner) ;
        String selected = _spinner_time_intervals[spinner.getSelectedItemPosition()] ;

        // TODO: replace with DateUtil.todayCanonicalized(). Hardcoded date to use mock sessions
        // get today's date and sessions loader
        Date today = DateUtil.canonicalize(DateUtil.parseDateString("2016:08:18 15:11:23")) ;
        MockSessionsLoader session_loader = new MockSessionsLoader() ;

        // obtain range based on selected time interval
        DateRange range ;
        if (selected.equals(this.getString(R.string.today_string)))
            range = new DateRange(today, today) ;
        else if (selected.equals(this.getString(R.string.this_week_string)))
            range = DateUtil.getWeekRangeFromDate(today) ;
        else if (selected.equals(this.getString(R.string.this_month_string)))
            range = DateUtil.getMonthRangeFromDate(today) ;
        else
            range = null ;

        // return sessions in calculated range, or all sessions
        if (null == range)
            return session_loader.getAllSessions() ;
        else
            return session_loader.getSessionsInRange(range.start(), range.stop()) ;
    }
}
