/*
问题描述：在下面的数字三角形中寻找一条从顶部到底边的路径，使得路径上所经过的数字之和最大，路径上的每一步都只能往左下或右下走。只需要求出这个最大和即可，不必给出具体路径。

输入数据的要求：三角形的行数大于1小于等于100，数字为0——99.

输入格式：

5 //三角形的行数，下面是三角形

7

3 8

8 1 0

2 7 4 4

4 5 2 6 5

 */

#include<cstdio>
const int maxn=101;
int main(){
    int n,a[maxn][maxn];
    while(scanf("%d",&n)!=EOF){
        for(int i=0;i<n;i++){
            for(int j=0;j<=i;j++){
                scanf("%d",&a[i][j]);
            }
        }
        for(int i=n-2;i>0;i--){
            for(int j=0;j<=i;j++){
                int x=a[i+1][j];
                if(x<a[i+1][j+1]){
                    x=a[i+1][j+1];
                }
                a[i][j]=a[i][j]+x;
            }
        }
        printf("%d\n",a[0][0]);
    }
    return 0;
}


