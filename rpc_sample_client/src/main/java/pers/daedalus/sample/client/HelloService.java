package pers.daedalus.sample.client;

/**
 * service 接口
 */
public interface HelloService {
    String hello(String name);
    String hello(Person person);
}
