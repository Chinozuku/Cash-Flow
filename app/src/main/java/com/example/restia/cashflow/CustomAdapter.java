package com.example.restia.cashflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Restia on 6/8/2016.
 */
public class CustomAdapter extends ArrayAdapter<Model>
{
    private Context context;
    private ArrayList<Model> model;

    public CustomAdapter(Context context, ArrayList<Model> model)
    {
        super(context, R.layout.adapter, model);
        this.context = context;
        this.model = model;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater myInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = myInflater.inflate(R.layout.adapter, parent, false);
        ImageView imgFlow = (ImageView)row.findViewById(R.id.imgFlow);

        TextView txtTitle = (TextView)row.findViewById(R.id.txtTitle);
        TextView txtAmount = (TextView)row.findViewById(R.id.txtAmount);
        TextView txtId = (TextView)row.findViewById(R.id.txtId);

        txtTitle.setText(model.get(position).getTitle());
        txtAmount.setText(model.get(position).getAmount() + "");
        txtId.setText(model.get(position).getId() + "");
        imgFlow.setImageResource(model.get(position).getType().toString().equals("In") ? R.drawable.green : R.drawable.red);
        return row;
    }
}
