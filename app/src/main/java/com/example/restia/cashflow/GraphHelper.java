package com.example.restia.cashflow;

/**
 * Created by Restia on 6/11/2016.
 */
public class GraphHelper
{
    private int[] inCount;
    private int[] outCount;
    private int[] inValue;
    private int[] outValue;

    public GraphHelper()
    {
        inCount = new int[31];
        outCount = new int[31];
        inValue = new int[31];
        outValue = new int[31];
        for (int i = 0; i < 31; i++)
        {
            inCount[i] = 0;
            outCount[i] = 0;
            inValue[i] = 0;
            outValue[i] = 0;
        }
    }

    public int[] getInCount() {
        return inCount;
    }

    public int[] getOutCount() {
        return outCount;
    }

    public int[] getInValue() {
        return inValue;
    }

    public int[] getOutValue() {
        return outValue;
    }

    public void increaseCount(String type, int day, int value)
    {
        if(type.equals("In"))
        {
            inCount[day - 1]++;
            inValue[day - 1] += value;
        }
        else
        {
            outCount[day - 1]++;
            outValue[day - 1] += value;
        }
    }
}
