package com.lucheng.learn.mydubbo_1.bean;

import java.io.Serializable;
import java.util.Arrays;

/**
 * rpc 请求类
 */
public class RequestMessage implements Serializable {
    //接口名称
    private String interName;
    //方法名称
    private String methodName;
    //参数
    private Object[] args;
    //参数类型
    private Class[] types;

    private RequestMessage(RequestMessageBuilder builder){
        this.interName = builder.interName;
        this.methodName = builder.methodName;
        this.args = builder.args;
        this.types = builder.types;
    }

    public static class RequestMessageBuilder{
        //接口名称
        private String interName;
        //方法名称
        private String methodName;
        //参数
        private Object[] args;
        //参数类型
        private Class[] types;

        public RequestMessageBuilder buildInterName(String interName){
            this.interName = interName;
            return this;
        }
        public RequestMessageBuilder buildMethodName(String methodName){
            this.methodName = methodName;
            return this;
        }
        public RequestMessageBuilder buildArgs(Object[] args){
            this.args = args;
            return this;
        }
        public RequestMessageBuilder buildTypes(Class[] types){
            this.types = types;
            return this;
        }

        public RequestMessage build(){
            return new RequestMessage(this);
        }
    }

    public String getInterName() {
        return interName;
    }

    public void setInterName(String interName) {
        this.interName = interName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class[] getTypes() {
        return types;
    }

    public void setTypes(Class[] types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "RequestMessage{" +
                "interName='" + interName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", types=" + Arrays.toString(types) +
                '}';
    }
}
