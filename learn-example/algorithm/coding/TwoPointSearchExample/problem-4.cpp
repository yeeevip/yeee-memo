/*
 * 二分法求方程的根
 *
 * 题目描述
用二分法求下面方程在区间(a,b)之间的根：

2x3-4x2+3x-6=0

区间端点a, b由键盘输入，确保输入区间内有根。

计算至误差小于10-6为止。

程序中，浮点型数据请定义为双精度double类型。

提示：二分法求方程根的步骤如下：

先将方程写成f(x)=0的形式，再按照如下步骤计算：

1.求出给出的两个端点之间的值fx1,fx2.当fx1*fx2<0,则表明x1和x2之间必存在一根
           要么就不存在，一直提示输出x1和x2.
2.一旦fx1*fx2<0，就表明在x1和x2之间有根，继续判断，求的x1和x2的中点值x0，求出fx0.
3.在判断fx0*fx1>0,则在x0和x2中间去找根，此时x1不起作用，用x0代替x1，用fx0代替fx1.
  要么就在x0和x1中去找根，此时x2不起作用，用x0代替x2，用fx0代替fx2.


输入
以空格分隔的区间端点值，确保输入的区间内存在方程的根。
输出
二分法求得的方程根，小数点后保留6位小数，末尾换行。
样例输入
-10 10
样例输出
2.000000
 *
 *
 * */