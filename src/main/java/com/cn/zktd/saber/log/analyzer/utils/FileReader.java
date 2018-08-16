package com.cn.zktd.saber.log.analyzer.utils;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
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

            Pattern p= Pattern.compile("([a-z]+)(\\d+)");

            List<String> lineLists = Files
                    .lines(Paths.get(fileName), Charset.defaultCharset())
                    .flatMap(line -> Arrays.stream(line.split("\n")))
                    .anyMatch(line -> p.matcher(line).find())
                    .collect(Collectors.toList());

            //输出文件函数
            System.out.println("lineLists====" + lineLists.size());

            //输出每一行文件内容
            lineLists.stream().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
