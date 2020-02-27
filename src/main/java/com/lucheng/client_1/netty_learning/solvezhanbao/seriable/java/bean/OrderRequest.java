package com.lucheng.client_1.netty_learning.solvezhanbao.seriable.java.bean;

import java.io.Serializable;

/**
 * netty 基于java的序列化
 * 必须实现 Serializable
 */
public class OrderRequest implements Serializable {
    private Integer orderId;
    private String userName;
    private String address;
    private String proName;
    private String phoneNumber;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "orderId=" + orderId +
                ", userName='" + userName + '\'' +
                ", address='" + address + '\'' +
                ", proName='" + proName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
