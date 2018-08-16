package com.cn.zktd.saber.log.analyzer.test;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dawei on 2018/8/16.
 */
@Component
public class FileReaderTest {

    public void readFile() {
        try {

            String fileName = "F:\\tmp\\log.txt";

            //读取文件
            Stream<String> stringStream = Files
                    .lines(Paths.get(fileName), Charset.defaultCharset())
                    .flatMap(line -> Arrays.stream(line.split(" ")));

            List<String> words = stringStream.collect(Collectors.toList());
            System.out.println("words===>" + words);


            List<String> lineLists = Files
                    .lines(Paths.get(fileName), Charset.defaultCharset())
                    .flatMap(line -> Arrays.stream(line.split("\n")))
                    .collect(Collectors.toList());

            //输出文件函数
            System.out.println("lineLists====" + lineLists.size());

            //输出每一行文件内容
            lineLists.stream().forEach(System.out::println);

            //统计单词的个数
            long uniqueWords = Files.lines(Paths.get(fileName), Charset.defaultCharset())
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();

            System.out.println("There are " + uniqueWords + " unique words in data.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
