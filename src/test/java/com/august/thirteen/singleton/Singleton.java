package com.august.thirteen.singleton;

//关于单例，我们总是应该记住：线程安全，延迟加载，序列化与反序列化安全，反射安全是很重重要的。

/*一：懒汉，线程不安全(这种写法lazy loading很明显，但是致命的是在多线程不能正常工作。)*/
/*public class Singleton {
    private static Singleton instance;
    private Singleton(){}
    public static Singleton getInstance(){
        if(instance==null){
            instance = new Singleton();
        }
        return instance;
    }
}*/

/*二：懒汉，线程安全(这种写法能够在多线程中很好的工作，而且看起来它也具备很好的lazy loading，但是，遗憾的是，效率很低，99%情况下不需要同步。)*/
/*public class Singleton{
    private static Singleton instance;
    private Singleton(){};
    public static synchronized Singleton getInstance(){
        if(instance==null){
            instance=new Singleton();
        }
        return instance;
    }

}*/

/*三：饿汉（这种方式基于classloder机制避免了多线程的同步问题，不过，instance在类装载时就实例化，虽然导致类装载的原因有很多种，在单例模式中大多数都是调用
        getInstance方法， 但是也不能确定有其他的方式（或者其他的静态方法）导致类装载，这时候初始化instance显然没有达到lazy loading的效果。）*/
/*public class Singleton{
    private static Singleton instance=new Singleton();
    private Singleton(){};
    public static Singleton getInstance(){
         return instance;
    }
}*/

/*四：饿汉，变种（表面上看起来差别挺大，其实更第三种方式差不多，都是在类初始化即实例化instance。）*/
/*public class Singleton{
    private static Singleton instance;
    private Singleton(){};
    static {
        instance=new Singleton();
    }
    public static Singleton getInstance(){
        return instance;
    }
}*/

/*五：静态内部类（这种方式同样利用了classloder的机制来保证初始化instance时只有一个线程，它跟第三种和第四种方式不同的是（很细微的差别）：
        第三种和第四种方式是只要Singleton类被装载了，那么instance就会被实例化（没有达到lazy loading效果），而这种方式是Singleton类被装载了，
        instance不一定被初始化。因为SingletonHolder类没有被主动使用，只有显示通过调用getInstance方法时，才会显示装载SingletonHolder类，
        从而实例化instance。想象一下，如果实例化instance很消耗资源，我想让他延迟加载，另外一方面，我不希望在Singleton类加载时就实例化，
        因为我不能确保Singleton类还可能在其他的地方被主动使用从而被加载，那么这个时候实例化instance显然是不合适的。这个时候，
        这种方式相比第三和第四种方式就显得很合理。）*/
/*public class Singleton{
    private static class SingletonHolder{
        private static final Singleton INSTANCE=new Singleton();
    }
    private Singleton(){};
    public static Singleton getInstance(){
       return SingletonHolder.INSTANCE;
    }
}*/

import java.lang.reflect.Constructor;

/*似乎静态内部类看起来已经是最完美的方法了，其实不是，可能还存在反射攻击或者反序列化攻击。且看如下代码：*/
/*public class Singleton{

    private static class SingletonHolder{
        private static final Singleton INSTANCE=new Singleton();
    }
    private Singleton(){};
    public static Singleton getInstance(){
        return SingletonHolder.INSTANCE;
    }
    
public static void main(String[] args) throws Exception {
    Singleton instance = Singleton.getInstance();
    Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Singleton instance1 = constructor.newInstance();
    if(instance!=instance1){
        System.out.println("not singleton");
    }
}
}*/

/*这种编写方式被称为“双重检查锁”，主要在getSingleton()方法中，进行两次null检查。这样可以极大提升并发度，进而提升性能。毕竟在单例中new的情况非常少，
        绝大多数都是可以并行的读操作，因此在加锁前多进行一次null检查就可以减少绝大多数的加锁操作，也就提高了执行效率。但是必须注意的是volatile关键字，
        该关键字有两层语义。第一层语义是可见性，可见性是指在一个线程中对该变量的修改会马上由工作内存（Work Memory）写回主内存（Main Memory），
        所以其它线程会马上读取到已修改的值，关于工作内存和主内存可简单理解为高速缓存（直接与CPU打交道）和主存（日常所说的内存条），注意工作内存是线程独享的，
        主存是线程共享的。volatile的第二层语义是禁止指令重排序优化，我们写的代码（特别是多线程代码），由于编译器优化，在实际执行的时候可能与我们编写
        的顺序不同。编译器只保证程序执行结果与源代码相同，却不保证实际指令的顺序与源代码相同，这在单线程并没什么问题，然而一旦引入多线程环境，
        这种乱序就可能导致严重问题。volatile关键字就可以从语义上解决这个问题，值得关注的是volatile的禁止指令重排序优化功能在Java 1.5后才得以实现，
        因此1.5前的版本仍然是不安全的，即使使用了volatile关键字。*/
/*
public class Singleton{
    private static volatile Singleton instance;
    private Singleton(){};
    public static Singleton getInstance(){
        if(instance==null){
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}*/

/*从上述4种单例模式的写法中，似乎也解决了效率与懒加载的问题，但是它们都有两个共同的缺点：反射攻击或者反序列化攻击*/

/*序列化可能会破坏单例模式，比较每次反序列化一个序列化的对象实例时都会创建一个新的实例，解决方案如下：*/
/*public class Singleton implements java.io.Serializable {
    public static Singleton INSTANCE = new Singleton();
    protected Singleton() {}
    //反序列时直接返回当前INSTANCE
    private Object readResolve() {
        return INSTANCE;
    }
}*/
/*使用反射强行调用私有构造器，解决方式可以修改构造器，让它在创建第二个实例的时候抛异常，如下：*/
/*public static Singleton INSTANCE = new Singleton();
private static volatile  boolean  flag = true;
private Singleton(){
        if(flag){
        flag = false;
        }else{
        throw new RuntimeException("The instance  already exists ！");
        }
        }*/

/*使用SingletonEnum.INSTANCE进行访问，这样也就避免调用getInstance方法，更重要的是使用枚举单例的写法，我们完全不用考虑序列化和反射的问题。
        枚举序列化是由jvm保证的，每一个枚举类型和定义的枚举变量在JVM中都是唯一的，在枚举类型的序列化和反序列化上，
        Java做了特殊的规定：在序列化时Java仅仅是将枚举对象的name属性输出到结果中，反序列化的时候则是通过java.lang.Enum的valueOf方法来根据名字查找枚举对象。
        同时，编译器是不允许任何对这种序列化机制的定制的并禁用了writeObject、readObject、readObjectNoData、writeReplace和readResolve等方法，
        从而保证了枚举实例的唯一性*/
/*public enum  SingletonEnum {
    INSTANCE;
    private String name;
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
}*/
