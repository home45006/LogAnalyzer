package com.cn.zktd.saber.log.analyzer.entity;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by dawei on 2018/9/14.
 */
@Data
public class AnalyzerEntity {

    private Calendar calendar;

    private String type;

    private String sessionId;

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SS");
        return formatter.format(calendar.getTime()) + "-" + type + "-" + sessionId;
    }
}
