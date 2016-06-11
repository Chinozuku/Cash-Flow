package com.example.restia.cashflow;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Restia on 6/11/2016.
 */
public class SpinnerOnClick implements AdapterView.OnItemSelectedListener
{
    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        System.out.println(position);
    }
}
