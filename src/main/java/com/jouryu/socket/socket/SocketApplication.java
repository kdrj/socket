package com.jouryu.socket.socket;
import com.jouryu.socket.socket.netty.RunNetty;
import com.jouryu.socket.socket.util.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class SocketApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(SocketApplication.class, args);
        SpringUtil springUtil = new SpringUtil();
        springUtil.setApplicationContext(context);
        SpringUtil.getBean(RunNetty.class).run();
    }
}
