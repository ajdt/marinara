package com.itsaunixsystem.marinara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

    String[] _time_intervals ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // init spinner and set listener
        initSelectedTimeSpinner() ;
        Spinner spinner = (Spinner)findViewById(R.id.selected_time_spinner) ;
        spinner.setOnItemSelectedListener(this) ;

        // load time interval and create graphs
        _time_intervals = getResources().getStringArray(R.array.time_intervals_array) ;
        createGraphs() ;
    }

    // TODO: rewrite this method to use DB data instead of mock data
    private List<Session> getSessions() {
        Spinner spinner = (Spinner)findViewById(R.id.selected_time_spinner) ;
        String selected = _time_intervals[spinner.getSelectedItemPosition()] ;

        // TODO: replace with DateUtil.todayCanonicalized(). Hardcoded date to use mock sessions
        Date today = DateUtil.parseDateString("2016:08:18 15:11:23") ;
        MockSessionsLoader session_loader = new MockSessionsLoader() ;

        if (selected.equals(this.getString(R.string.today_string))) {
            return session_loader.getSessionsInRange(today, today) ;
        } else if (selected.equals(this.getString(R.string.this_week_string))) {
            DateRange range = DateUtil.getWeekRangeFromDate(today) ;
            Log.d("blah", "date range:" + DateUtil.toCalendarDateString(range.start())
            + " to " + DateUtil.toCalendarDateString(range.stop())) ;
            return session_loader.getSessionsInRange(range.start(), range.stop()) ;
        } else if (selected.equals(this.getString(R.string.this_month_string))) {
            DateRange range = DateUtil.getMonthRangeFromDate(today) ;
            Log.d("blah", "month date range:" + DateUtil.toCalendarDateString(range.start())
                    + " to " + DateUtil.toCalendarDateString(range.stop())) ;
            return session_loader.getSessionsInRange(range.start(), range.stop()) ;
        } else {
            return session_loader.getAllSessions() ;
        }
    }

    private void createGraphs() {
        // get sessions
        List<Session> sessions = this.getSessions() ;

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
        // get pie chart entries and labels
        PieChartStats pie_stats = new PieChartStats(sessions) ;
        ArrayList<PieEntry> entries = pie_stats.getEntries() ;

        // make data sets
        PieDataSet pie_data_set = new PieDataSet(entries, "sessions per task") ;
        pie_data_set.setColors(ColorTemplate.MATERIAL_COLORS) ; // set colors
        PieData pie_data = new PieData(pie_data_set) ;

        // add data sets to pie chart object
        PieChart pie_chart = (PieChart)findViewById(R.id.pie_chart) ;
        pie_chart.setData(pie_data) ;
        pie_chart.setDescription("sessions per task") ;

        // draw chart
        pie_chart.invalidate() ;
    }


    private void initSelectedTimeSpinner() {
        // create adapter and initialize with time interval array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_intervals_array, android.R.layout.simple_spinner_item) ;

        // set drop down layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) ;

        // add adapter to spinner and set first item to selected
        Spinner spinner = (Spinner)findViewById(R.id.selected_time_spinner) ;
        spinner.setAdapter(adapter) ;
        spinner.setSelection(0) ; // set first to selected


    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        createGraphs() ;
    }

    public void onNothingSelected(AdapterView<?> parent) { }
}
