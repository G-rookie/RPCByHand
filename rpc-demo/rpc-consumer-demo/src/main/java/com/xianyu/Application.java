package com.xianyu;

public class Application {
    public static void main(String[] args) {

        // 相近一切办法获取代理对象，使用referenceConfig进行封装
        //reference一定用生成代理的模板方法，get（）
        ReferenceConfig<HelloRPC> reference = new ReferenceConfig<>();
        reference.setInterface(HelloRPC.class);


        //代理做了写什么
        //1、连接注册中心
        //2、拉取服务列表
        //3、选择一个服务并建立连接
        //4、发送请求，携带一些信息（接口名，参数列表，方法的名字），获得结果

        RPCBootstrap.getInstance()
                .application("first-rpc-consumer")
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .reference(reference);

        //获取一个代理对象
        HelloRPC helloRPC =  reference.get();
        helloRPC.sayHi("你好！！！");

    }
}
