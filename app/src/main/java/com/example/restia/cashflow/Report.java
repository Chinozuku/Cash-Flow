package com.example.restia.cashflow;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.w3c.dom.Text;

public class Report extends AppCompatActivity
{
    private GraphHelper helper;
    private Database db;
    private SQLiteDatabase database;
    private String[] column = {"due", "transactionType", "amount"};
    TextView txtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        db = new Database(this);
        database = db.getReadableDatabase();
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        init();
    }
    private void init()
    {
        String month = getIntent().getStringExtra("month");
        String year = getIntent().getStringExtra("year");
        helper = new GraphHelper();
        GraphView countGraph = (GraphView) findViewById(R.id.countGraph);
        GraphView valueGraph = (GraphView) findViewById(R.id.valueGraph);

        Cursor cursor = database.query("cash", column, "due like '%/" + month + "/" + year + "'", null, null, null, "due asc");
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++)
        {
            helper.increaseCount(cursor.getString(1), Integer.parseInt(cursor.getString(0).split("/")[0]), cursor.getInt(2));
            cursor.moveToNext();
        }
        DataPoint[] inPoint = new DataPoint[31];
        DataPoint[] outPoint = new DataPoint[31];
        DataPoint[] inValuePoint = new DataPoint[31];
        DataPoint[] outValuePoint = new DataPoint[31];

        for (int i = 0; i < 31; i++)
        {
            inPoint[i] = new DataPoint(i + 1, helper.getInCount()[i]);
            outPoint[i] = new DataPoint(i + 1, helper.getOutCount()[i]);
            inValuePoint[i] = new DataPoint(i + 1, helper.getInValue()[i]);
            outValuePoint[i] = new DataPoint(i + 1, helper.getOutValue()[i]);
        }

        //count graph adding
        BarGraphSeries<DataPoint> in = new BarGraphSeries<>(inPoint);
        countGraph.addSeries(in);
        BarGraphSeries<DataPoint> out = new BarGraphSeries<>(outPoint);
        out.setColor(Color.RED);
        countGraph.addSeries(out);

        //value graph adding
        BarGraphSeries<DataPoint> valueIn = new BarGraphSeries<>(inValuePoint);
        valueGraph.addSeries(valueIn);
        BarGraphSeries<DataPoint> valueOut = new BarGraphSeries<>(outValuePoint);
        valueOut.setColor(Color.RED);
        valueGraph.addSeries(valueOut);

        //legend
        //count
        in.setTitle("Income Count");
        out.setTitle("Outcome Count");
        countGraph.getLegendRenderer().setVisible(true);
        countGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        //value
        valueIn.setTitle("Income Value");
        valueOut.setTitle("Outcome Value");
        valueGraph.getLegendRenderer().setVisible(true);
        valueGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        txtTitle.setText("Report for " + getIntent().getStringExtra("mon") + " " + year);
    }
}
