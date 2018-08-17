package com.cn.zktd.saber.log.analyzer.utils;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by dawei on 2018/8/16.
 */
@Component
public class FileReader {

    public void readFile() {
        try {
//            String fileName = "F:\\tmp\\log.txt";
            String fileName = "F:\\tmp\\test.log";

            Pattern p= Pattern.compile(".*B2\\|\\|<<<<<-------.*");

            List<String> lineLists = Files
                    .lines(Paths.get(fileName), Charset.defaultCharset())
                    .flatMap(line -> Arrays.stream(line.split("\n")))
                    .collect(Collectors.toList());

            //输出文件函数
            System.out.println("lineLists====" + lineLists.size());

            //输出每一行文件内容
            lineLists.stream().forEach(line -> {
                if(!line.contains("||PingRequest") && !line.contains("||GetStatusRequest") && !line.contains("||ConnectRequest")) {
                    if(line.contains("B2||<<<<<-------")) {
                        System.out.println(line);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Pattern p= Pattern.compile(".*B2\\|\\|<<<<<-------.*");
        String line = "2018-07-30 11:22:36.741 [B2-work-59] INFO  c.c.z.saber.server.local.B2.handler.B2BaseHandler - B2||<<<<<-------6689ac0d7ceb4f6aa87add08a2fcedb8||GetStatusRequest ROM1708310008959-53670-GREEN_CIRCLE";
        Matcher m = p.matcher(line);
        if(m.find()) {
            System.out.println(m.group(0));
        } else {
            System.out.println("No Match");
        }
    }
}
