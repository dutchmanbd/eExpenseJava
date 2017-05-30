package backbencers.nub.dailycostcalc.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import backbencers.nub.dailycostcalc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceFragment extends Fragment {


    private PieChart pie_chart_balance;


    private float[] yData = {2500.00f, 5000.00f, 10000.00f, 1923.00f, 2309.00f, 2300.00f, 4350.0f,4000.00f,2323.00f,1000.00f,1000.00f,1000.00f};
    private String[] xData = {"January", "February" , "March" , "April", "May", "June", "July","August","September","October","November","December"};

    private Context context;

    public BalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_balance, container, false);

        this.context = getActivity().getApplicationContext();

        getActivity().setTitle("Balance");

        inits(view);

        return view;
    }

    private void inits(View view){

        pie_chart_balance = (PieChart) view.findViewById(R.id.pie_chart_balance);

        pie_chart_balance.setDescription("Monthly Cost(In ৳) ");
        pie_chart_balance.setRotationEnabled(true);
        pie_chart_balance.setHoleRadius(25f);
        pie_chart_balance.setTransparentCircleAlpha(0);
        pie_chart_balance.setCenterText("Super Cool Chart");
        pie_chart_balance.setCenterTextSize(10);

        addDataSet();

        pie_chart_balance.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                String hlight = h.toString();   //Highlight, x: 11.0, y: 1000.0, dataSetIndex: 0, stackIndex (only stacked barentry): -1

                String[] sCommas = hlight.split(","); // x: 11.0  and y: 1000.0

                String[] sX = sCommas[1].trim().split(" ");

                int index = (int) Float.parseFloat(sX[1].trim());

                Toast.makeText(context, xData[index] + "\n" + "Cost: ৳" + yData[index], Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });

    }


    private void addDataSet() {

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
        }

        for(int i = 0; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Monthly Cost");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        colors.add(Color.DKGRAY);
        colors.add(Color.WHITE);
        colors.add(Color.LTGRAY);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pie_chart_balance.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pie_chart_balance.setData(pieData);
        pie_chart_balance.invalidate();
    }

}
