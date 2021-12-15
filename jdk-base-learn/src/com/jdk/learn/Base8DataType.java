package com.jdk.learn;

/**
 * @Description:
 * @Author: anchun
 * @Date: 2021/12/15 15:02
 */
public class Base8DataType {

    {
        /*
         * int 占32位，范围：符号位1，实体31，所以表示范围 （-2^31） 0 （2^31-1）
        **/
        byte b = 1;                             // byte b = 0;     一个字节 8位
        byte[] shortByte = new byte[2];         // short s = 0;    二个字节 16位
        byte[] intByte = new byte[4];           // int i = 0;      四个字节 32位
        byte[] longByte = new byte[8];          // long l = 0l;    八个字节 64位
        byte[] floatByte = new byte[4];         // float f = 0.0f; 四个字节 32位
        byte[] doubleByte = new byte[8];        // double d = 0.0d;八个字节 64位
        byte[] charByte = new byte[2];          // char c = '';    二个字节 16位
        byte[] booleanByte = new byte[1];       // boolean b = false;  一个字节 8位
    }

    static byte b;

    static short s;

    static int i;

    static long l;

    static float f;

    static double d;

    static char c;

    static boolean bo;

    public static void main(String[] args) {

        System.out.println("byte的大小："+Byte.SIZE+";默认值："+b+";数据范围："+Byte.MIN_VALUE+" - "+Byte.MAX_VALUE);

        System.out.println("short的大小："+Short.SIZE+";默认值："+s+";数据范围："+Short.MIN_VALUE+" - "+Short.MAX_VALUE);

        System.out.println("int的大小："+Integer.SIZE+";默认值："+i+";数据范围："+Integer.MIN_VALUE+" - "+Integer.MAX_VALUE);

        System.out.println("long的大小："+Long.SIZE+";默认值："+l+";数据范围："+Long.MIN_VALUE+" - "+Long.MAX_VALUE);

        System.out.println("float的大小："+Float.SIZE+";默认值："+f+";数据范围："+Float.MIN_VALUE+" - "+Float.MAX_VALUE);

        System.out.println("double的大小："+Double.SIZE+";默认值："+d+";数据范围："+Double.MIN_VALUE+" - "+Double.MAX_VALUE);

        System.out.println("char的大小："+Character.SIZE+";默认值："+c+";数据范围："+Character.MIN_VALUE+" - "+Character.MAX_VALUE);

        System.out.println("boolean的大小："+Byte.SIZE+";默认值："+bo+";数据范围："+Byte.MIN_VALUE+" - "+Byte.MAX_VALUE);

    }

}
