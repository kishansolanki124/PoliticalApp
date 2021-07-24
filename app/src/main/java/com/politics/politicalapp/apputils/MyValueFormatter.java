package com.politics.politicalapp.apputils;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class MyValueFormatter extends ValueFormatter {
    //private final DecimalFormat mFormat;

    public MyValueFormatter() {
        //mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value) {
        if (value == 0) {
            return "";
        } else {
            //return mFormat.format(value) + " %"; // e.g. append percentage sign
            return Math.round(value) + " %"; // e.g. append percentage sign
        }
    }
}