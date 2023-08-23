package com.xianyu.utils.zookeeper;

import com.xianyu.Constant;
import com.xianyu.exceptions.ZookeeperException;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;

@Slf4j
public class ZookeeperUtil {

    /**
     * 使用默认配置创建zookeeper实例
     * @return zookeeper实例
     */
    public static ZooKeeper createZookeeper() {

        //定义连接参数
        String connectString = Constant.DEFAULT_ZK_CONNECT;
        //定义超时时间
        int timeout = Constant.TIME_OUT;
        return createZookeeper(connectString, timeout);
    }

    public static ZooKeeper createZookeeper(String connectString, Integer timeout) {
//        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            // 创建zookeeper实例，建立连接
            final ZooKeeper zooKeeper = new ZooKeeper(connectString, timeout, watchedEvent -> {
                //只有连接成功才放行
                if (watchedEvent.getState().equals(Watcher.Event.KeeperState.Closed)) {
                    System.out.println("客户端连接成功");
//                    countDownLatch.countDown();
                }
            });
            // 等待连接成功
//            countDownLatch.await();
            return zooKeeper;

        } catch (IOException e) {
            log.error("创建zookeeper实例出现异常，异常：", e);
            throw new ZookeeperException();
        }
    }

    /**
     * 床架一个结点的工具方法
     * @param zooKeeper zookeeper实例
     * @param node 结点
     * @param watcher watcher实例
     * @param createMode 结点类型
     * @return true：成功创建    false：已经存在  异常：抛出
     */
    public static Boolean createNode(ZooKeeper zooKeeper, ZookeeperNode node, Watcher watcher, CreateMode createMode){
        try {
            if (zooKeeper.exists(node.getNodePath(), watcher) == null) {
                String result = zooKeeper.create(node.getNodePath(), null,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
                log.info("节点{}创建成功", result);
                return true;
            } else {
                if (log.isDebugEnabled()) {
                    log.info("结点{}已经存在，无需重复创建", node.getNodePath());
                }
                return false;
            }
        } catch (KeeperException | InterruptedException e) {
            log.error("创建基础目录发生异常：", e);
            throw new ZookeeperException();
        }
    }

    /**
     * 判断结点是否存在
     * @param zooKeeper zookeeper实例
     * @param node 结点路径
     * @param watcher watcher
     * @return true：结点存在 | false： 结点不存在
     */
    public static Boolean exists(ZooKeeper zooKeeper, String node, Watcher watcher) {
        try {
            return zooKeeper.exists(node, watcher) != null;
        } catch (KeeperException | InterruptedException e) {
            log.error("判断结点{}是否存在时发生异常", node, e);
            throw new ZookeeperException(e);
        }
    }

    /**
     *  关闭zookeeper连接
     * @param zooKeeper zookeeper实例
     */
    public  static void close(ZooKeeper zooKeeper) {
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            log.error("关不zookeeper时发生异常:", e);
            throw new ZookeeperException();
        }
    }
}
