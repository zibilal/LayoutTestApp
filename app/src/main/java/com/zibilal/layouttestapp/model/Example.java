package com.zibilal.layouttestapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bilal on 1/27/2016.
 */
public class Example implements IDataAdapter {
    private String name;
    public Example() {}

    public Example(String name) {
        this.name = name;
    }

    public int add(int a, int b) {
        return a + b;
    }

    public static int mul(int a, int b) {
        return a * b;
    }

    public String lower(String a) {
        return a.toLowerCase();
    }

    public void printDate(Date date) {
        Log.d(Example.class.getSimpleName(), "The date: " + date);
    }

    public List<Integer> getIntegers() {
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(10);
        ints.add(100);
        ints.add(1000);
        return ints;
    }

    @Override
    public String toString() {
        return name;
    }
}
