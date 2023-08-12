package com.xianyu;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

public class NettyTest {

    @Test
    public void testByteBuf(){
        ByteBuf header = Unpooled.buffer();
        ByteBuf body = Unpooled.buffer();

        //通过逻辑组装而不是物理拷贝，实现JVM中的零拷贝
        CompositeByteBuf byteBuf = Unpooled.compositeBuffer();
        byteBuf.addComponents(header, body);

    }

    @Test
    public void testWrapper(){
        byte[] buf = new byte[1024];
        byte[] buf1 = new byte[1024];

        //共享byte数组的内容而不是拷贝，这也算零拷贝
        ByteBuf byteBuf = Unpooled.wrappedBuffer(buf, buf1);
    }


    @Test
    public void testMessage() throws IOException {
        ByteBuf msg = Unpooled.buffer();
        //magic
        msg.writeBytes("xianyu".getBytes(StandardCharsets.UTF_8));
        //version
        msg.writeByte(1);
        //head length
        msg.writeShort(125);
        //full length
        msg.writeInt(256);
        // mt
        msg.writeByte(1);
        // serializable
        msg.writeByte(0);
        // compress
        msg.writeByte(2);

        //RequestId
        msg.writeLong(231414L);

        //用对象流转化为字节数据
        AppClient appClient = new AppClient();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(appClient);
        byte[] bytes = outputStream.toByteArray();
        msg.writeBytes(bytes);

//        System.out.println(msg);
        printAsBinary(msg);
    }

    //打印为二进制形式
    public static void printAsBinary(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(byteBuf.readerIndex(), bytes);

        String binaryString = ByteBufUtil.hexDump(bytes);
        StringBuilder formattedBinary = new StringBuilder();

        for (int i = 0; i < binaryString.length(); i+= 2) {
            formattedBinary.append(binaryString, i, i + 2).append(" ");
        }

        System.out.println("Binary representation:" + formattedBinary);
    }
}
