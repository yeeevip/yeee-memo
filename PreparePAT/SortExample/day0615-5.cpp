/*
题目描述

        输入10个整数，彼此以空格分隔。重新排序以后输出(也按空格分隔)，要求:
1.先输出其中的奇数,并按从大到小排列；
2.然后输出其中的偶数,并按从小到大排列。


输入

        任意排序的10个整数（0～100），彼此以空格分隔。


输出

        可能有多组测试数据，对于每组数据，按照要求排序后输出，由空格分隔。


样例输入
0 56 19 81 59 48 35 90 83 75
17 86 71 51 30 1 9 36 14 16
样例输出
83 81 75 59 35 19 0 48 56 90
71 51 17 9 1 14 16 30 36 86
提示

        多组数据，注意输出格式


1. 测试数据可能有很多组，请使用while(cin>>a[0]>>a[1]>>...>>a[9])类似的做法来实现;

2. 输入数据随机，有可能相等。
 */

#include<cstdio>
#include <iostream>
#include<algorithm>
using namespace std;
const int maxn=13;
bool cmp(int a,int b){
    return a>b;
};
int main(){
    int num[10],oddNum[10],evenNum[10];
    while(cin>>num[0]>>num[1]>>num[2]>>num[3]>>num[4]>>num[5]>>num[6]>>num[7]>>num[8]>>num[9]){
       int i=0,j=0;
       for(int k=0;k<9;k++){
           if(num[k]%2!=0){
               oddNum[i++]=num[k];
           }else{
               evenNum[j++]=num[k];
           }
       }
        sort(oddNum,oddNum+i,cmp);
        sort(evenNum,evenNum+j);
        for(int m=0;m<i;m++){
            printf("%d ",oddNum[m]);
        }
        for(int m=0;m<j;m++){
            printf("%d ",evenNum[m]);
        }
    }
    return 0;
}