package com.lucheng.mydubbo_3.spi;

import com.lucheng.mydubbo_3.spi.annotation.Extension;
import com.lucheng.mydubbo_3.spi.annotation.SPI;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lucheng28
 * @date 2021-01-09
 * 模拟实现Dubbo中的spi机制
 */
public class ExtensionLoader<T> {
    //一个类一个扩展类加载器
    private Class<T> type;

    //用来缓存接口对应的扩展类加载器
    private static ConcurrentHashMap<Class, ExtensionLoader> localMap = new ConcurrentHashMap<>();
    //缓存具体的扩展类实例
    private static ConcurrentHashMap<String, Holder<Object>> cacheExtensionInstance = new ConcurrentHashMap<>();
    //缓存配置文件中配置的配置信息 key为标识符 value为具体的全限定名
    private static Holder<Map<String,Class>> cachedExtentionClass = new Holder<>();
    private static final Object extentionLock = new Object();
    private static final Object classMapLock = new Object();
    //默认扩展类名称
    private String defaultExtentionName;
    private static String defaultRootPath = "META-INF/services/";

    private ExtensionLoader() {
    }

    private ExtensionLoader(Class<T> type) {
        this.type = type;
    }

    /**
     * 根据具体的接口类获取具体的扩展类加载器
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> ExtensionLoader getExtensionLoader(Class<T> type) throws Exception {
        //判断是否为空
        if (type == null) throw new Exception("type is null");

        //判断是否是接口
        if (!type.isInterface()) throw new Exception("type must be a interface");

        //判断是否带有扩展类标示
        if (!withSpiAnnotation(type)) throw new Exception("interface has not spi annotation");

        //检查本地缓存是否存在扩展类加载器
        localMap.putIfAbsent(type, new ExtensionLoader<>(type));

        //返回具体的类加载器
        return localMap.get(type);
    }

    /**
     * 根据配置中的名称获取具体的扩展类
     * java spi机制是将所有的扩展类都加载
     *
     * @param name
     * @return
     */
    public T getExtension(String name) throws IllegalAccessException, IOException,InstantiationException, ClassNotFoundException {
        return getExtension(name, true);
    }

    /**
     * @param name
     * @param wapper 扩展类是否是包装对象 比如spring的包装对象
     * @return
     */
    public T getExtension(String name, boolean wapper) throws IllegalAccessException, IOException,InstantiationException, ClassNotFoundException {
        //判断名称是否为空
        if (StringUtils.isEmpty(name)) throw new IllegalArgumentException("name is empty");

        //判断扩展类实例是否存在，若不存在则直接创建
        Holder<Object> holder = getHolder(name);
        Object o = holder.getInstance();
        if (o == null) {
            synchronized (extentionLock) {
                o = holder.getInstance();
                if (o == null) {
                    holder.setInstance(createExtension(name, wapper));
                    o = holder.getInstance();
                }
            }
        }

        return (T) o;
    }

    /**
     * 接口是否带有spi注解
     *
     * @param type
     * @return
     */
    private static Boolean withSpiAnnotation(Class type) {
        return type.isAnnotationPresent(SPI.class);
    }

    /**
     * 获取具体的扩展类实例
     *
     * @param name
     * @return
     */
    private Holder<Object> getHolder(String name) {
        cacheExtensionInstance.putIfAbsent(name, new Holder<>());
        return cacheExtensionInstance.get(name);
    }

    /**
     * 创建扩展类实例
     *
     * @param name
     */
    private Object createExtension(String name, boolean wapper) throws IllegalAccessException, IOException,InstantiationException, ClassNotFoundException {
        //先获取扩展类的class
        Class c = getExtensionClass().get(name);

        //最简单的方式直接返回类的实例
        return c.newInstance();
    }

    /**
     * 加载配置文件中的扩展类配置数据
     * key为标识符 value为具体的全限定名
     * @return
     */
    private Map<String,Class> getExtensionClass() throws IllegalAccessException, IOException, ClassNotFoundException {
        //首先判断是否存在本地缓存
        Map<String,Class> classMap = cachedExtentionClass.getInstance();
        if(classMap == null){
            synchronized (classMapLock){
                classMap = cachedExtentionClass.getInstance();
                if(classMap == null){
                    //开始加载文件
                    classMap = loadResource();
                    cachedExtentionClass.setInstance(classMap);
                }
            }
        }

        return classMap;
    }

    /**
     * 加载meta-inf下的文件
     * @return
     */
    private Map<String,Class> loadResource() throws IllegalAccessException, IOException, ClassNotFoundException {
        //首先判断是否存在默认的扩展类
        //若存在并将其缓存起来
        cacheDefaultExtentionName();

        //开始加载具体的扩展类配置文件
        Map<String,Class> extentionMap = new HashMap<>();
        loadDir(extentionMap);

        return extentionMap;

    }

    /**
     * 缓存默认的扩展类
     */
    private void cacheDefaultExtentionName(){
        //获取扩展类的spi注解
        SPI spi = type.getAnnotation(SPI.class);

        //解析默认扩展类全限定名
        String value = spi.value().trim();
        if(StringUtils.isNotEmpty(value)){
            //使用逗号来分隔数据
            String[] values = value.split(",");
            if(value.length() > 1) throw new IllegalArgumentException("default extention must be one");

            defaultExtentionName = values[0];
        }
    }

    private void loadDir(Map<String,Class> map) throws IOException, IllegalAccessException, ClassNotFoundException {
        String filePath = defaultRootPath + type.getName();

        //获取文件的url
        Enumeration<URL> urls = this.getClass().getClassLoader().getResources(filePath);

        if(urls == null || !urls.hasMoreElements()) throw new IllegalAccessException("dir has not file");

        while (urls.hasMoreElements()){
            URL url = urls.nextElement();
            loadExtention(url,map);
        }
    }

    /**
     * InputStreamReader 字节流字符流转换器
     * @param url
     * @param map
     * @throws IOException
     */
    private void loadExtention(URL url,Map<String,Class> map) throws IOException, ClassNotFoundException, IllegalAccessException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = bufferedReader.readLine();
        while (StringUtils.isNotEmpty(line)){
            String key = line.substring(0,line.indexOf("="));
            String value = line.substring(line.indexOf("=") + 1);
            map.put(key,loadClass(value));
            line = bufferedReader.readLine();
        }
    }

    private Class loadClass(String name) throws ClassNotFoundException, IllegalAccessException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Class c = Class.forName(name,false,classLoader);

        //判断当前类是否是当前扩展类的子类
        if(!type.isAssignableFrom(c)){
            throw new IllegalAccessException("current class is not subtype of " + type.getName());
        }

        //判断当前扩展点是否是Aop增强扩展点
        if(isWapperClass(c)){
            //将当前类缓存在wapper缓存中
        }

        //普通扩展类
        //判断下是否是真正的扩展类
        String annotationName = findAnnotationName(c);
        if(StringUtils.isEmpty(annotationName)){
            throw new IllegalAccessException("no such class with name " + c.getName());
        }

        return c;
    }

    private boolean isWapperClass(Class clazz){
        try {
            clazz.getConstructor(type);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
    private String findAnnotationName(Class clazz){
        Extension extension = (Extension) clazz.getAnnotation(Extension.class);
        if(extension != null){
            return extension.value();
        }

        return null;
    }

    /**
     * 获取classpath下文件的方式
     * @param args
     */
    public static void main(String[] args) throws IOException {
        String fileName = "META-INF/services/";
        ExtensionLoader extensionLoader = new ExtensionLoader();
        URL url = extensionLoader.getClass().getClassLoader().getResource(fileName);
        System.out.println(url);
        System.out.println(url.getPath());

        //第一种方式
        File file = new File(url.getPath());
        System.out.println(file.isDirectory());
        for(File fileNames : file.listFiles()){
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileNames));
            String line = bufferedReader.readLine();
            while (StringUtils.isNotEmpty(line)){
                System.out.println(line);
                line = bufferedReader.readLine();
            }
        }

        //第二种方式
        for(File file1 : file.listFiles()){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file1.toURL().openStream()));
            String line  = bufferedReader.readLine();
            while (StringUtils.isNotEmpty(line)){
                System.out.println(line);
                line = bufferedReader.readLine();
            }
        }

    }
}
