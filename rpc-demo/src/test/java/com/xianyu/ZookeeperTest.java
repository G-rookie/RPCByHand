package com.xianyu;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ZookeeperTest {
    ZooKeeper zooKeeper;
//    CountDownLatch countDownLatch = new CountDownLatch(1);

    @Before
    public void createZK() {

        //定义一个连接参数
//        String connectString = "192.168.2.130:2181, 192.168.2.133:2181, 192.168.2.132:2181";
        String connectString = "127.0.0.1:2181";

        //定义超时时间
        int timeout = 1000;

        try {
            //new MyWatcher() 默认的监听器
            // 创建一个zookeeper实习，是否需要时间，是否需要等待
            //闯将zookeeper是否需要连接
//            zooKeeper = new ZooKeeper(connectString, timeout, new MyWatcher());
            zooKeeper = new ZooKeeper(connectString, timeout, watchedEvent -> {
                //只有连接成功才放行
                if (watchedEvent.getState().equals(Watcher.Event.KeeperState.Closed)) {
                    System.out.println("客户端连接成功");
//                    countDownLatch.countDown();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCreatePNode() {
        String result;
        try {
            // 等待连接成功
//            countDownLatch.await();
            result = zooKeeper.create("/xianyu", "hello".getBytes(StandardCharsets.UTF_8),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("result = " + result);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zooKeeper == null) {
                    assert false;
                    zooKeeper.close();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testExistPNode() {
        try {
            // version: cas 乐观锁 ， 也可以无视版本号 -1
            Stat stat = zooKeeper.exists("/xianyu", null);

            //当前结点的数据版本
            zooKeeper.setData("/xianyu", "world".getBytes(StandardCharsets.UTF_8), -1);

            int version = stat.getVersion();
            System.out.println("version = " + version);

            //当前结点的acl数据版本
            int aversion = stat.getAversion();
            System.out.println("aversion = " + aversion);

            //当前子节点数据的版本
            int cversion = stat.getCversion();
            System.out.println("cversion = " + cversion);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zooKeeper == null) {
                    assert false;
                    zooKeeper.close();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testWatcher() {
        try {
            // version: cas 乐观锁 ， 也可以无视版本号 -1
            zooKeeper.exists("/xianyu", true);

//            while (true) {
//                Thread.sleep(1000);
//            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zooKeeper == null) {
                    assert false;
                    zooKeeper.close();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
