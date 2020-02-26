package com.lucheng.mydubbo_2.util;

import java.io.*;

/**
 * 序列化类
 */
public class SerializeUtils {
    public static byte[] serialize(Object message){
        ByteArrayOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        byte[] result = null;
        try {
            //将请求数据写入到内存中
            outputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
            result = outputStream.toByteArray();
            outputStream.close();
            objectOutputStream.close();
            return result;
        } catch (IOException e) {
            System.out.println("序列化数据写入内存异常");
            e.printStackTrace();
        }finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    System.out.println("内存流关闭异常");
                    e.printStackTrace();
                }
            }
            if(objectOutputStream != null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    System.out.println("对象流关闭异常");
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static Object reSerialize(byte[] message){
        ByteArrayInputStream os = null;
        ObjectInputStream objectInputStream = null;
        Object message1 = null;
        try {
            os = new ByteArrayInputStream(message);
            objectInputStream = new ObjectInputStream(os);
            message1 = objectInputStream.readObject();
            os.close();
            objectInputStream.close();
            return message1;
        } catch (IOException e) {
            System.out.println("rpc数据流异常");
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            System.out.println("rpc读取数据异常");
            e.printStackTrace();
        }finally {
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(objectInputStream != null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return message1;
    }
}
