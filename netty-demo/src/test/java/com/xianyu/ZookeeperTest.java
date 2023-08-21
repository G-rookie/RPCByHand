package com.xianyu;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ZookeeperTest {
    ZooKeeper zooKeeper;

    @Before
    public void createZK() {

        //定义一个连接参数
        String connectString = "127.0.0.1:2181";

        //定义超时时间
        int timeout = 1000;

        try {
            //new MyWatcher() 默认的监听器
            zooKeeper = new ZooKeeper(connectString, timeout, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCreatePNode() {
        String result;
        try {
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

            while (true) {
                Thread.sleep(1000);
            }
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
