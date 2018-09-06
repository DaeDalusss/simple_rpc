package pers.daedalus.sample.server;

import pers.daedalus.sample.client.HelloService;
import pers.daedalus.sample.client.Person;
import pers.daedalus.server.annocation.RpcService;

/**
 * sample 服务端 实现接口功能； 等待被远程调用
 */
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {
    public String hello(String name) {
        System.out.println("已经调用服务端接口实现，业务处理为：");
        System.out.println("Hello! " + name);
        return "Hello! " + name;
    }

    public String hello(Person person) {
        System.out.println("已经调用服务端接口实现，业务处理为：");
        System.out.println("Hello! " + person.getName() + " age: " + person.getAge());
        return "Hello! " + person.getName() + " age: " + person.getAge();
    }
}
