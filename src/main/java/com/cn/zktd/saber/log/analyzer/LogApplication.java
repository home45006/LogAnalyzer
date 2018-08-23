package com.cn.zktd.saber.log.analyzer;

import com.cn.zktd.saber.log.analyzer.utils.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootApplication
@EntityScan(value = {"com.cn.zktd.saber.log.analyzer"})
public class LogApplication implements CommandLineRunner {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    @Autowired
    private FileReader fileReader;

    @Override
    public void run(String... args) {
        log.info("初始化系统...");
        fileReader.readFile();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("发生异常");
        }
    }

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(LogApplication.class).run(args);
    }

}

