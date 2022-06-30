package com.yeee.execSort;

/**
 * 测试代码执行顺序
 *
 *              静态成员变量
 *                   |
 *              静态代码块
 *                   |
 *              普通成员变量
 *                   |
 *               动态代码块
 *                   |
 *                构造函数
 *
 * @author yeeee
 * @since 2022/6/30 9:36
 */
public class TestClassExecSort {

    private String val1 = getVal1();
    private static String val2 = getVal2();

    private static String getVal2() {
        System.out.println("我是静态成员变量");
        return "我是静态成员变量";
    }

    private String getVal1() {
        System.out.println("我是普通成员变量");
        return "我是普通成员变量";
    }

    static {
        System.out.println("我是静态代码块");
    }

    {
        System.out.println("我是动态代码块");
    }

    public TestClassExecSort() {
        System.out.println("我是构造函数");
    }

    public static void main(String[] args) {
        new TestClassExecSort();
    }

}
