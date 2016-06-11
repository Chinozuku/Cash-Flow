package com.example.restia.cashflow;

/**
 * Created by Restia on 6/11/2016.
 */
public class GraphHelper
{
    private int[] count;

    public GraphHelper()
    {
        count = new int[31];
    }

    public int[] getCount() { return count; }

    public void increaseCount(int day)
    {
        count[day - 1]++;
    }
}
