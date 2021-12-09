/*
题目描述
        请写一个程序，对于一个m行m列的（1<m<10）的方阵，求其每一行，每一列及主对角线元素之和，最后按照从大到小的顺序依次输出。

输入
        共一组数据，输入的第一行为一个正整数，表示m，接下来的m行，每行m个整数表示方阵元素。

输出
        从大到小排列的一行整数，每个整数后跟一个空格，最后换行。

样例输入
4
15 8 -2 6
31 24 18 71
-3 -9 27 13
17 21 38 69

样例输出
159 145 144 135 81 60 44 32 28 27
 */

#include<cstdio>
#include<algorithm>
using namespace std;
bool cmp(int a,int b){
    return a>b;
}
int main(){
    int m,num[11][11],sumArray[24]={0},sum,kSum1=0,kSum2=0,k;
    while(scanf("%d",&m)!=EOF){
        for(int i=0;i<m;i++){
            sum=0;
            for(int j=0;j<m;j++){
                scanf("%d",&num[i][j]);
                sum = sum+num[i][j];
            }
            sumArray[i]=sum;
        }
        k=m;
        for(int i=0;i<m;i++){
            sum=0;
            for(int j=0;j<m;j++){
               sum = sum+num[j][i];
               if(i==j){
                   kSum1= kSum1+num[i][j];
               }
               if(i+j==m-1){
                   kSum2=kSum2+num[i][j];
               }
            }
            sumArray[k++]=sum;
        }
        sumArray[k++]=kSum1;
        sumArray[k++]=kSum2;
        sort(sumArray,sumArray+k+1,cmp);
        for(int i=0;i<k;i++){
            printf("%d",sumArray[i]);
            if(i<k-1) printf(" ");
        }
    }
    return 0;
}