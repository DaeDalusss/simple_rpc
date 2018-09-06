package pers.daedalus.sample.server;

import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * sample server 的服务启动类， 用于启动server 注册服务 并被调用
 */
public class RpcBootstrap {
    public static void main(String[] args) {
        try {
            new ClassPathXmlApplicationContext("spring.xml");
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }
}
