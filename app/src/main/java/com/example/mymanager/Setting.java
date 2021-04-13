package com.example.mymanager;

public class Setting {
    String l,s;

    public String getL() {
        return l;
    }

    public String getS() {
        return s;
    }

    public Setting(String label, String subtitle)
    {
        this.l = label;
        this.s = subtitle;
    }
}
