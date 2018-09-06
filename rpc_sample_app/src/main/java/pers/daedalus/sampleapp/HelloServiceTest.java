package pers.daedalus.sampleapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.daedalus.client.zkclient.RpcProxy;
import pers.daedalus.sample.client.HelloService;
import pers.daedalus.sample.client.Person;

/**
 * 客户应用端
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class HelloServiceTest {
    @Autowired
    private RpcProxy rpcProxy;

    /**
     * 测试方法1
     * 测试远程调用 传递参数String
     */
    @Test
    public void test1() {
        // 调用代理的create方法，代理HelloService接口
        HelloService helloService = rpcProxy.create(HelloService.class);
        // 调用代理的方法，执行invoke
        String result = helloService.hello("World");
        System.out.println("服务端返回结果：");
        System.out.println(result);
    }

    /**
     * 测试方法2
     * 测试远程调用 传递参数Object
     */
    @Test
    public void test2() {
        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello(new Person("Simth", 20));
        System.out.println("服务端返回结果：");
        System.out.println(result);
    }
}
