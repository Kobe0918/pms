package com.mjrj.lzh.pms.util.learn.arrayutil;

public class Parent {
    private static int P1 = staticPrintInt("P....初始化父类静态属性", 100);  //3.初始化静态属性并分配内存空间和赋值
    private int P2 = instancePrintInt("P....初始化父类实例属性", 200);//7.初始化父类实例属性并分配内存空间和赋值

    static{
        System.out.println("P....执行父类静态代码段"); // 4. 执行静态代码段
    }
    {
        System.out.println("P....执行父类非静态代码段");//8. 执行父类非静态代码段
    }
    public static int staticPrintInt(String str, int value){
        System.out.println(str);
        return value;
    }

    public  int instancePrintInt(String str, int value){
        System.out.println(str);
        return value;
    }

    public void publicPrintProperty(){

        System.out.println("P....Parent public 方法");
        System.out.println("P....p1 =" + P1);
        System.out.println("P....p2 =" + P2);
    }
    private void privatePrintProperty(){
        System.out.println("P....Parent private 方法");
        System.out.println("P....p1 =" + P1);
        System.out.println("P....p2 =" + P2);
    }

    public Parent()//9.执行构造方法
    {
        System.out.println("P....父类构造方法");
        publicPrintProperty();
        privatePrintProperty();
    }

    public static void main(String[] args) {// 1 ：main函数是程序的入口执行
        Parent p;// 2 ：加载类
        System.out.println("=======================");//5.
        p = new Parent();//6.创建对象

    }

    /**执行顺序
     * 当父类为object时上述代码执行过程
     * 实例化对象执行的流程
     * 1.加载类
     * 2.为静态代码属性分配内存空间并赋值
     * 3.执行静态代码块
     * 4.创建对象
     * 5.为实例属性分配内存并赋值
     * 6.执行非静态代码块
     * 7.执行构造方法
     */
}
