package me.loki2302.jadehelpers;

import java.text.SimpleDateFormat;
import java.util.Date;


public class JadeDateHelper {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy"); 
    private final static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    
    public String makeDate(Date date) {
        return dateFormat.format(date);
    }
    
    public String makeTime(Date date) {
        return timeFormat.format(date);
    }
}