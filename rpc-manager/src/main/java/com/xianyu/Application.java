package com.xianyu;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * 注册中心的管理页面
 */
@Slf4j
public class Application {
    public static void main(String[] args) {
        //创建基础目录
        ZooKeeper zooKeeper;
//        CountDownLatch countDownLatch = new CountDownLatch(1);

        String connectString = Constant.DEFAULT_ZK_CONNECT;

        //定义超时时间
        int timeout = Constant.TIME_OUT;

        try {
            // 创建zookeeper实例，建立连接
            zooKeeper = new ZooKeeper(connectString, timeout, watchedEvent -> {
                //只有连接成功才放行
                if (watchedEvent.getState().equals(Watcher.Event.KeeperState.Closed)) {
                    System.out.println("客户端连接成功");
//                    countDownLatch.countDown();
                }
            });

            // 等待连接成功
//            countDownLatch.await();
            // 定义结点和数据
            String basePath = "/rpc-metadata";

            String providerPath = basePath + "/providers";
            String consumerPath = basePath + "/consumers";

            if (zooKeeper.exists(basePath, null) == null) {
                String result = zooKeeper.create(basePath, null,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("根节点{}创建成功", result);
            }

            if (zooKeeper.exists(providerPath, null) == null) {
                String result = zooKeeper.create(providerPath, null,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("从节点{}创建成功", result);
            }

            if (zooKeeper.exists(consumerPath, null) == null) {
                String result = zooKeeper.create(consumerPath, null,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("从节点{}创建成功", result);
            }

        } catch (IOException | KeeperException | InterruptedException e) {
            log.error("创建根节点出现异常，异常：", e);
        }
    }
}
