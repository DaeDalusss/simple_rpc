package pers.daedalus.registry.zkserv;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.daedalus.registry.common.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 服务发现
 *  用于client发现server节点的变化 ，实现负载均衡
 */
public class ServiceDiscovery {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<String>();

    private String registryAddress;

    /**
     * 构造器
     * 传入注册地址在构造中建立 zk 链接
     * @param registryAddress
     */
    public ServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;

        ZooKeeper zk = connectServer();//建立连接
        if (zk != null) {
            watchNode(zk);
        }
    }

    /**
     * 发现新节点
     *      zk节点变化有监听， 所以这里获取节点直接从缓存的list中拿到即可
     * @return
     */
    public String discover() {
        String data = null;
        int size = dataList.size();
        // 存在新节点，使用即可
        if (size > 0) {
            //随机的负载均衡。 如果有其他负载均衡策略可以从这里实现
            if (size == 1) {
                data = dataList.get(0);
                LOGGER.debug("using only data: {}", data);
            } else {
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
                LOGGER.debug("using random data: {}", data);
            }
        }
        return data;
    }

    /**
     * zk 链接
     * @return
     */
    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT,
                    new Watcher() {
                        public void process(WatchedEvent event) {
                            if (event.getState() == Event.KeeperState.SyncConnected) {
                                latch.countDown();
                            }
                        }
                    });
            // 由于zk 建立链接是异步。 这里需要利用 latch 等待阻塞
            latch.await();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return zk;
    }

    /**
     * 监听
     * @param zk
     */
    private void watchNode(final ZooKeeper zk) {
        try {
            // 获取所有子节点
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH,
                    event -> {
                        // 节点改变
                        if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                            watchNode(zk);
                        }
                    });
            List<String> dataList = new ArrayList<String>();
            // 循环子节点
            for (String node : nodeList) {
                // 获取节点中的服务器地址
                byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH + "/"
                        + node, false, null);
                // 存储到list中
                dataList.add(new String(bytes));
            }
            LOGGER.debug("node data: {}", dataList);
            // 将节点信息记录在成员变量
            this.dataList = dataList;
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }
}
