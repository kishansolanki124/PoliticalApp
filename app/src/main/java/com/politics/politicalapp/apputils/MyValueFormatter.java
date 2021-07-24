package com.politics.politicalapp.apputils;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class MyValueFormatter extends ValueFormatter {
    private final DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
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

//todo: post this answer : https://stackoverflow.com/questions/32940999/how-to-hide-zero-values-in-pie-chart-mpchart-android/42929380