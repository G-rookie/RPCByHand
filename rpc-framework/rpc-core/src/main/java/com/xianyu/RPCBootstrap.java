package com.xianyu;


import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class RPCBootstrap {

    /**
     * -------------------------服务提供方的相关api--------------------------------
     */
    //rpcBootstrap是一个单例，我们希望每个应用程序只有一个实例
    private static RPCBootstrap rpcBootstrap = new RPCBootstrap();

    private RPCBootstrap(){
        //构造启动引导程序时需要做一些初始化的事
    }

    public static RPCBootstrap getInstance() {
        return rpcBootstrap;
    }

    /**
     * 用来定义当前应用的名字
     * @param appName 应用名称
     * @return this当前实例
     */
    public RPCBootstrap application(String appName) {
        return this;
    }


    /**
     * 用来配置一个注册中心
     * @param registryConfig 注册中心
     * @return this当前实例
     */
    public RPCBootstrap registry(RegistryConfig registryConfig) {
        return this;
    }

    /**
     * 配置当前暴露的服务使用的协议
     * @param protocolConfig 协议的封装
     * @return this当前实例
     */
    public RPCBootstrap protocol(ProtocolConfig protocolConfig) {
        if (log.isDebugEnabled()) {
            log.debug("当前工程使用了：{}协议进行序列化", protocolConfig.toString());
        }
        return this;
    }

    /**
     * -------------------------服务提供方的相关api--------------------------------
     */

    /**
     * 发布服务，将将接口实现，注册到服务中心
     * @param service 封装需要发布的服务
     * @return this当前实例
     */
    public RPCBootstrap publish(ServiceConfig<?> service) {
        if (log.isDebugEnabled()) {
            log.debug("当前工程使用了：{} 协议进行序列化", service.getInterface().getName());
        }
        return this;
    }

    /**
     * 批量发布服务
     * @param services 封装需要发布的服务集合
     * @return this当前实例
     */
    public RPCBootstrap publish(List<?> services) {
        return this;
    }

    /**
     * 启动netty服务
     */
    public void start() {
    }


    /**
     * -------------------------服务调用方的相关api--------------------------------
     */
    public RPCBootstrap reference(ReferenceConfig<?> reference) {
        //在这个方法中是否可以拿到相关的配置型-注册中心
        //配置reference，将来调用get方法是，方便生成代理对象
        return this;
    }
}
