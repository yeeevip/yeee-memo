/*
题目描述
        中位数定义：一组数据按从1小到大的顺序依次排列，处在中间位置的一个数（或最中间两个数据的平均数）.
给出一组无序整数，求出中位数，如果求最中间两个数的平均数，向下取整即可（不需要使用浮点数）

输入
        该程序包含多组测试数据，每一组测试数据的第一行为N，代表该组测试数据包含的数据个数，1<=N<=10000.
接着N行为N个数据的输入，N=0时结束输入

输出
        输出中位数，每一组测试数据输出一行

        样例输入
1
468
15
501
170
725
479
359
963
465
706
146
282
828
962
492
996
943
0
样例输出
468
501
提示

        按题目要求模拟即可
        */

#include<cstdio>
#include<algorithm>
using namespace std;
const int maxn=10010;
int main(){
    int n,num[maxn];
    while(scanf("%d",&n)!=EOF){
        if(n==0)break;
        for(int i=0;i<n;i++){
            scanf("%d",&num[i]);
        }
        sort(num,num+n);
        if(n%2!=0) {
            printf("%d\n",num[(n-1)/2]);
        }
        else {
            printf("%d\n",(num[n/2]+num[n/2-1])/2);
        }
    }
    return 0;
}