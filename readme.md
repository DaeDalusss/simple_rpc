## Project
### 简介
    自定义搭建实现 rpc框架
    基于netty 和 zookeeper
    整个rpc的调用过程 相当于 feign的调用过程, 这里zookeeper 则充当了 eureka 等同的角色
    遥想当年的 hessian 爬虫，无非就是一个 rpc调用处理而已，而且当年的没有做服务注册的负载均衡；所有的结果进出都是在单独的 队列服务； 队列服务做备份轮     询而已

     
## 框架结构
    整体的框架内容包括 
        rpc_client 用于打成jar 供客户端 rpc_sample_app 调用 (相当于eureka.jar)
        rpc_server 用于客户端 rpc_sample_server 调用， 注册自己的服务给zk（相当于 eureka-server.jar）
        rpc_registry 被依赖的注册相关服务
        rpc_common   被依赖的通用代码
    
    客户端包括：
        rpc_sample_app  前端 创建代理， 调用接口 发送消息
        rpc_sample_server 后台 启动服务，实现接口 处理消息(相当于feign server) 调用rpc_client.jar 的方法注册服务
        rpc_sample_client 定义接口服务和协议 (相当于feign client)
        
        rpc_sample_app 和 rpc_sample client 可以合并成一个， 这是rpc_sample_client 直接充当一个service 的接口定义层
        
## 测试
    启动 rpc_sample_server 的 RpcBootstrap ，会自动注入服务到zk
    运行 rpc_sample_app HelloServiceTest 的test 方法

## 启动以及调用的过程
   ![image](https://github.com/DaeDalusss/simple_rpc/blob/master/resc/rpc.png)

