package com.lucheng.mydubbo_3.spi;

import com.lucheng.mydubbo_3.inner_extention.MyExtention;

public class test {
    public static void main(String[] args) throws Exception {
        ExtensionLoader<MyExtention> extentionExtensionLoader = ExtensionLoader.getExtensionLoader(MyExtention.class);
        MyExtention myExtention = extentionExtensionLoader.getExtension("one",false);
        System.out.println(myExtention);
    }
}
