package com.marafiki.android.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.marafiki.android.R;
import com.marafiki.android.transactions.MyMarkerView;

import java.util.ArrayList;


public class SummaryFragment extends Fragment implements OnChartValueSelectedListener {

    private LineChart chart;
    protected final String[] months = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    public SummaryFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_line_chart, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = view.findViewById(R.id.chart);
        chart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        // disable description text
        chart.getDescription().setEnabled(false);
        // enable touch gestures
        chart.setTouchEnabled(true);

        // set listeners
        chart.setOnChartValueSelectedListener(this);
        chart.setDrawGridBackground(false);

        // create marker to display box when values are selected
        MyMarkerView mv = new MyMarkerView(requireActivity(), R.layout.custom_marker_view);

        // Set the marker to the chart
        mv.setChartView(chart);
        chart.setMarker(mv);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(12);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return months[(int) value % months.length];
            }
        });

        YAxis yAxis;
        {   // Y-Axis Style //
            yAxis = chart.getAxisLeft();
            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);
        }


        chart.setData(generateLineData());
        chart.getAxisLeft().setTextColor(getResources().getColor(R.color.superWhite));
        chart.getXAxis().setTextColor(getResources().getColor(R.color.superWhite));
        chart.getXAxis().setDrawGridLines(false);
        chart.setExtraOffsets(0, 0, 0, 10);
        // draw points over time
        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);

    }

    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < 12; index++)
            entries.add(new Entry(index + 0.5f, getRandom(1000, 600)));

        LineDataSet set = new LineDataSet(entries, "");
        set.disableDashedLine();
        set.setColor(getResources().getColor(R.color.colorAccent));
        set.setLineWidth(1.5f);
        set.setCircleColor(getResources().getColor(R.color.colorAccent));
        set.setCircleRadius(3f);
        set.setFillColor(getResources().getColor(R.color.colorAccent));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);
        set.setValueTextSize(10f);
        set.setValueTextColor(getResources().getColor(R.color.colorAccent));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        chart.getLegend().setEnabled(false);

        return d;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        //Log.i("LOW HIGH", "low: " + chart.getLowestVisibleX() + ", high: " + chart.getHighestVisibleX());
        //Log.i("MIN MAX", "xMin: " + chart.getXChartMin() + ", xMax: " + chart.getXChartMax() + ", yMin: " + chart.getYChartMin() + ", yMax: " + chart.getYChartMax());
    }


    @Override
    public void onNothingSelected() {

    }
}
