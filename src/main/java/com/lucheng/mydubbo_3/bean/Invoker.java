package com.lucheng.mydubbo_3.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucheng28
 * @date 2020-03-13
 */
@Data
public class Invoker implements Serializable {
    private String className;
    private String methodName;
    private Class[] argsType;
    private Object[] args;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getArgsType() {
        return argsType;
    }

    public void setArgsType(Class[] argsType) {
        this.argsType = argsType;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
