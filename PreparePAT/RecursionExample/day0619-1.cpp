/*
题目描述

        有一个神奇的口袋，总的容积是40，用这个口袋可以变出一些物品，这些物品的总体积必须是40。John现在有n个想要得到的物品，
        每个物品的体积分别是a1，a2……an。John可以从这些物品中选择一些，如果选出的物体的总体积是40，那么利用这个神奇的口袋，
        John就可以得到这些物品。现在的问题是，John有多少种不同的选择物品的方式。


输入

        输入的第一行是正整数n (1 <= n <= 20)，表示不同的物品的数目。接下来的n行，每行有一个1到40之间的正整数，分别给出a1，a2……an的值。


输出

        输出不同的选择物品的方式的数目。


样例输入
2
12
28
3
21
10
5
样例输出
1
0
 */

 //递归解法
#include<cstdio>
const int maxn=22;
int num[maxn];
int ways(int n,int w){
    if(w==0) return 1;
    if(n<=0) return 0;
    return ways(n-1,w)+ways(n-1,w-num[n-1]);
}
int main(){
    int n;
    while(scanf("%d",&n)!=EOF){
        for(int i=0;i<n;i++){
            scanf("%d",&num[i]);
        }
        printf("%d",ways(n,40));
    }
    return 0;
}

//动态解法
/*#include<cstdio>
#define N 100
int main(){
    int dp[N][50],n,a[N];  //dp[i][j]表示从前i种物品里凑出体积j的方法数
    while(scanf("%d",&n)!=EOF){
        for(int i=1;i<=n;i++){
            scanf("%d",&a[i]);
            dp[i][0]=1;
        }
        dp[0][0]=1;
        for(int i=1;i<=n;i++){
            for(int j=1;j<=40;j++){
                dp[i][j]=dp[i-1][j];
                if(j>a[i])
                    dp[i][j]+=dp[i-1][j-a[i]];
            }
        }
        printf("%d",dp[n][40]);
    }
    return 0;
}*/

//我为人人”型递推解法
//此问题仅在询问容积40是否可达，40是个很小的 数，可以考虑对值域空间-即对容积的可达性进行动态 规划。
//定义一维数组 int sum[41];依次放入物品，计算每次放入物品可达的容积， 并在相应空间设置记录，最后判断sum[40] 是否可达 ，到达了几次。





//最长上升子序列的“我为人人”做法：


#include <sctdio>
const int maxn = 20;
int weigh[maxn];
int solve(int n,int weigh){
    if(weigh==0) return 1;
    if(n<=0) return 0;
    return solve(n-1,weigh-weigh[n])+solve(n-1,weigh);
}
int main(){
    int n;
    scanf("%d",&n);
    for(int i = 1;i<=n;i++){
        scanf("%d",&weigh[i]);
    }
    printf("%d",solve(n,40));
    return 0;
}