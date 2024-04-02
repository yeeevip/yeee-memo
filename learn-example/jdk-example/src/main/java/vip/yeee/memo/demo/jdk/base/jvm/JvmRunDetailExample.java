package vip.yeee.memo.demo.jdk.base.jvm;

/**
 * 程序在JVM内部是怎么运行的
 *
 * 1.JAVA程序的执行过程简单来说包括：
 *
 * 2.JAVA源代码编译成字节码；
 *
 * 3.字节码校验并把JAVA程序通过类加载器加载到JVM内存中；
 *
 * 4.在加载到内存后针对每个类创建Class对象并放到方法区；
 *
 * 5.字节码指令和数据初始化到内存中；
 *
 * 6.找到main方法，并创建栈帧；
 *
 * 7.初始化程序计数器内部的值为main方法的内存地址；
 *
 * 8.程序计数器不断递增，逐条执行JAVA字节码指令，把指令执行过程的数据存放到操作数栈中（入栈）
 * ，执行完成后从操作数栈取出后放到局部变量表中，遇到创建对象，则在堆内存中分配一段连续的空间存储对象
 * ，栈内存中的局部变量表存放指向堆内存的引用；遇到方法调用则再创建一个栈帧，压到当前栈帧的上面。
 *
 *
 * #################################################
 *
 * 进入class文件目录，javap -v JvmRunDetailExample   生成字节码指令
 *
 * #################################################
 *
 *
 * @author https://www.yeee.vip
 * @since 2023/5/16 14:30
 */
public class JvmRunDetailExample {

    private Integer sum = 0;

    public void sum(Integer i, Integer j) {
        this.sum = i + j;
    }

    public int getSum() {
        return sum;
    }

    public static void main(String[] args) {
        JvmRunDetailExample detailExample = new  JvmRunDetailExample();
        detailExample.sum(3, 5);
        int sum = detailExample.getSum();
        System.out.println(sum);
    }

}
