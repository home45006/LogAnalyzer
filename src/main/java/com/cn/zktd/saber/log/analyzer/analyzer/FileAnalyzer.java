package com.cn.zktd.saber.log.analyzer.analyzer;

import com.cn.zktd.saber.log.analyzer.entity.AnalyzerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dawei on 2018/8/16.
 */
@Slf4j
@Component
public class FileAnalyzer {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("MMdd-HH:mm:ss");

    public Map<String, List<AnalyzerEntity>> getTreeMap() throws ParseException {
        Map<String, String> roomMap = new Hashtable<>();
        initRoomMap(roomMap);

        try {
            String fileName = "F:\\tmp\\guoanfu\\all.log";

            List<String> lineLists = Files
                    .lines(Paths.get(fileName), Charset.defaultCharset())
                    .flatMap(line -> Arrays.stream(line.split("\n")))
                    .collect(Collectors.toList());

            //输出文件函数
            log.info("lineLists: {}", lineLists.size());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");

            Map<String, List<AnalyzerEntity>> map = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            String roomId = null;
            for (int i = 0; i < lineLists.size(); i++) {
                String line = lineLists.get(i);
                if (line.contains("===========")) {
                    String[] arr = line.split("===========");
                    roomId = arr[1];
//                    log.info("{}", arr[1]);
                    map.put(roomMap.get(arr[1]), new ArrayList<>());
                } else {
                    AnalyzerEntity analyzerEntity = new AnalyzerEntity();
                    String[] arr = line.split(" ");
//                    log.info("{} {}", arr[0], arr[1]);
                    List<AnalyzerEntity> list = map.get(roomMap.get(roomId));
                    Date date = formatter.parse(arr[0] + " " + arr[1]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    if (calendar.get(Calendar.MONTH) == 7 && calendar.get(Calendar.DATE) < 29) {
//                        System.out.println("" + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DATE));
                        continue;
                    }

                    analyzerEntity.setCalendar(calendar);
                    if (line.contains("智能网关接入")) {
                        analyzerEntity.setType("connect");
                    } else if (line.contains("D1会话超时")) {
                        analyzerEntity.setType("timeout");
                        analyzerEntity.setSessionId(arr[8].split("-")[1]);
                    } else if (line.contains("连接断开")) {
                        analyzerEntity.setType("disconnect");
                        analyzerEntity.setSessionId(arr[7].split(":")[2].replace("连接断开", ""));
                    }
                    list.add(analyzerEntity);
                }
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void analyzer() throws ParseException {

        Map<String, List<AnalyzerEntity>> map = getTreeMap();

        System.out.println("==========统计区间 {8月29-9月14}=========");
        for (String key : map.keySet()) {
            List<AnalyzerEntity> list = map.get(key);
            int disconnect = 0;
            int timeout = 0;
            int connect = 0;
            for (int i = 0; i < list.size(); i++) {
                AnalyzerEntity analyzerEntity = list.get(i);
                switch (analyzerEntity.getType()) {
                    case "connect":
                        connect++;
                        break;
                    case "timeout":
                        timeout++;
                        break;
                    case "disconnect":
                        disconnect++;
                        break;
                }
            }
            System.out.println("room: " + key +
                    " 总数: " + list.size() +
                    " 连接断开: " + disconnect +
                    " 会话超时: " + timeout +
                    " 连接: " + connect
            );
        }
    }

    public void analyzer_1() throws ParseException {

        Map<String, List<AnalyzerEntity>> map = getTreeMap();

        System.out.println("==========连接时间点统计区间 {8月29-9月14}=========");
        for (String key : map.keySet()) {
            List<AnalyzerEntity> list = map.get(key);
            StringBuffer sb = new StringBuffer(key + ":");
            for (int i = 0; i < list.size(); i++) {
                AnalyzerEntity analyzerEntity = list.get(i);
                switch (analyzerEntity.getType()) {
                    case "connect":
                        sb.append(formatter.format(analyzerEntity.getCalendar().getTime()));
                        sb.append("->");
                        break;
                }
            }
            System.out.println(sb.toString());

        }
    }

    public void analyzer_2() throws ParseException {

        Map<String, List<AnalyzerEntity>> map = getTreeMap();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        System.out.println("==========每日连接次数统计区间 {8月29-9月14}=========");
        for (String key : map.keySet()) {
            List<AnalyzerEntity> list = map.get(key);
            StringBuffer sb = new StringBuffer(key + ": ");

            Map<String, Integer> analyzerMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            for(AnalyzerEntity analyzerEntity: list) {
                if(!analyzerEntity.getType().contains("connect")) continue;
                String dateStr = formatter.format(analyzerEntity.getCalendar().getTime());
                if(analyzerMap.containsKey(dateStr)) {
                    Integer count = analyzerMap.get(dateStr);
                    count = count + 1;
                    analyzerMap.put(dateStr, count);
                } else {
                    analyzerMap.put(dateStr, 0);
                }
            }

            for(String dateStr : analyzerMap.keySet()) {
                if(analyzerMap.get(dateStr).intValue() == 0) continue;
                sb.append(dateStr + "-" + analyzerMap.get(dateStr).intValue() + ">>");
            }
            System.out.println(sb.toString());
        }
    }

    public static void main(String[] args) throws ParseException {
        FileAnalyzer fileAnalyzer = new FileAnalyzer();
        fileAnalyzer.analyzer_2();
    }

    private void initRoomMap(Map<String, String> roomMap) {
        roomMap.put("ROM1612070003484", "1-1-0301");
        roomMap.put("ROM1612070003448", "1-1-0302");
        roomMap.put("ROM1708310005248", "1-1-0401");
        roomMap.put("ROM1708310008454", "1-1-0402");
        roomMap.put("ROM1708310005501", "1-1-0501");
        roomMap.put("ROM1708310008460", "1-1-0502");
        roomMap.put("ROM1708310005350", "1-1-0601");
        roomMap.put("ROM1708310011011", "1-1-0602");
        roomMap.put("ROM1708310005401", "1-1-0701");
        roomMap.put("ROM1708310011013", "1-1-0702");
        roomMap.put("ROM1708310005797", "1-1-0801");
        roomMap.put("ROM1708310011015", "1-1-0802");
        roomMap.put("ROM1708310005848", "1-1-0901");
        roomMap.put("ROM1708310011017", "1-1-0902");
        roomMap.put("ROM1708310005899", "1-1-1001");
        roomMap.put("ROM1708310011019", "1-1-1002");
        roomMap.put("ROM1708310005950", "1-1-1101");
        roomMap.put("ROM1708310011021", "1-1-1102");
        roomMap.put("ROM1708310006049", "1-1-1201");
        roomMap.put("ROM1708310011023", "1-1-1202");
        roomMap.put("ROM1612070003412", "1-2-0301");
        roomMap.put("ROM1612070003376", "1-2-0302");
        roomMap.put("ROM1708310010235", "1-2-0401");
        roomMap.put("ROM1708310010586", "1-2-0402");
        roomMap.put("ROM1708310010237", "1-2-0501");
        roomMap.put("ROM1708310010588", "1-2-0502");
        roomMap.put("ROM1708310010239", "1-2-0601");
        roomMap.put("ROM1708310010590", "1-2-0602");
        roomMap.put("ROM1708310010241", "1-2-0701");
        roomMap.put("ROM1708310010592", "1-2-0702");
        roomMap.put("ROM1708310010243", "1-2-0801");
        roomMap.put("ROM1708310010594", "1-2-0802");
        roomMap.put("ROM1708310010245", "1-2-0901");
        roomMap.put("ROM1708310010596", "1-2-0902");
        roomMap.put("ROM1708310010247", "1-2-1001");
        roomMap.put("ROM1708310010598", "1-2-1002");
        roomMap.put("ROM1708310010251", "1-2-1101");
        roomMap.put("ROM1708310010600", "1-2-1102");
        roomMap.put("ROM1708310010249", "1-2-1201");
        roomMap.put("ROM1708310010602", "1-2-1202");
        roomMap.put("ROM1612070003340", "1-3-0301");
        roomMap.put("ROM1612070003295", "1-3-0302");
        roomMap.put("ROM1612070003256", "1-3-0303");
        roomMap.put("ROM1708310008945", "1-3-0401");
        roomMap.put("ROM1708310009370", "1-3-0402");
        roomMap.put("ROM1708310009811", "1-3-0403");
        roomMap.put("ROM1708310008947", "1-3-0501");
        roomMap.put("ROM1708310009372", "1-3-0502");
        roomMap.put("ROM1708310009813", "1-3-0503");
        roomMap.put("ROM1708310008949", "1-3-0601");
        roomMap.put("ROM1708310009374", "1-3-0602");
        roomMap.put("ROM1708310009815", "1-3-0603");
        roomMap.put("ROM1708310008951", "1-3-0701");
        roomMap.put("ROM1708310009376", "1-3-0702");
        roomMap.put("ROM1708310009817", "1-3-0703");
        roomMap.put("ROM1708310008953", "1-3-0801");
        roomMap.put("ROM1708310009378", "1-3-0802");
        roomMap.put("ROM1708310009819", "1-3-0803");
        roomMap.put("ROM1708310008955", "1-3-0901");
        roomMap.put("ROM1708310009380", "1-3-0902");
        roomMap.put("ROM1708310009821", "1-3-0903");
        roomMap.put("ROM1708310008957", "1-3-1001");
        roomMap.put("ROM1708310009382", "1-3-1002");
        roomMap.put("ROM1708310009823", "1-3-1003");
        roomMap.put("ROM1708310008959", "1-3-1101");
        roomMap.put("ROM1708310009384", "1-3-1102");
        roomMap.put("ROM1708310009825", "1-3-1103");
        roomMap.put("ROM1708310008961", "1-3-1201");
        roomMap.put("ROM1708310009386", "1-3-1202");
        roomMap.put("ROM1708310009827", "1-3-1203");
        roomMap.put("ROM1612070003120", "2-1-0301");
        roomMap.put("ROM1612070003211", "2-1-0302");
        roomMap.put("ROM1708310007935", "2-1-0401");
        roomMap.put("ROM1708310008442", "2-1-0402");
        roomMap.put("ROM1708310007937", "2-1-0501");
        roomMap.put("ROM1708310008444", "2-1-0502");
        roomMap.put("ROM1708310007939", "2-1-0601");
        roomMap.put("ROM1708310008446", "2-1-0602");
        roomMap.put("ROM1708310007941", "2-1-0701");
        roomMap.put("ROM1708310008448", "2-1-0702");
        roomMap.put("ROM1708310007943", "2-1-0801");
        roomMap.put("ROM1708310008450", "2-1-0802");
        roomMap.put("ROM1708310007945", "2-1-0901");
        roomMap.put("ROM1708310008452", "2-1-0902");
        roomMap.put("ROM1708310007947", "2-1-1001");
        roomMap.put("ROM1708310008456", "2-1-1002");
        roomMap.put("ROM1708310007949", "2-1-1101");
        roomMap.put("ROM1708310008458", "2-1-1102");
        roomMap.put("ROM1708310007951", "2-1-1201");
        roomMap.put("ROM1708310008462", "2-1-1202");
        roomMap.put("ROM1612070003074", "2-2-0301");
        roomMap.put("ROM1612070003166", "2-2-0302");
        roomMap.put("ROM1708310006100", "2-2-0401");
        roomMap.put("ROM1708310006988", "2-2-0402");
        roomMap.put("ROM1708310006150", "2-2-0501");
        roomMap.put("ROM1708310007039", "2-2-0502");
        roomMap.put("ROM1708310006200", "2-2-0601");
        roomMap.put("ROM1708310007090", "2-2-0602");
        roomMap.put("ROM1708310006590", "2-2-0701");
        roomMap.put("ROM1708310007092", "2-2-0702");
        roomMap.put("ROM1708310006640", "2-2-0801");
        roomMap.put("ROM1708310007290", "2-2-0802");
        roomMap.put("ROM1708310006690", "2-2-0901");
        roomMap.put("ROM1708310007292", "2-2-0902");
        roomMap.put("ROM1708310006740", "2-2-1001");
        roomMap.put("ROM1708310007342", "2-2-1002");
        roomMap.put("ROM1708310006790", "2-2-1101");
        roomMap.put("ROM1708310007392", "2-2-1102");
        roomMap.put("ROM1708310006792", "2-2-1201");
        roomMap.put("ROM1708310007394", "2-2-1202");
    }
}
