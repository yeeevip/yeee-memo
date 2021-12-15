/*
题目描述
名名的妈妈从外地出差回来，带了一盒好吃又精美的巧克力给名名（盒内共有 N 块巧克力，20 > N >0）。
妈妈告诉名名每天可以吃一块或者两块巧克力。
假设名名每天都吃巧克力，问名名共有多少种不同的吃完巧克力的方案。
例如：
如果N=1，则名名第1天就吃掉它，共有1种方案；
如果N=2，则名名可以第1天吃1块，第2天吃1块，也可以第1天吃2块，共有2种方案；
如果N=3，则名名第1天可以吃1块，剩2块，也可以第1天吃2块剩1块，所以名名共有2+1=3种方案；
如果N=4，则名名可以第1天吃1块，剩3块，也可以第1天吃2块，剩2块，共有3+2=5种方案。
现在给定N，请你写程序求出名名吃巧克力的方案数目。

输入
输入只有1行，即整数N。

输出
可能有多组测试数据，对于每组数据，
输出只有1行，即名名吃巧克力的方案数。

样例输入
1
2
4

样例输出
1
2
5
*/
/*
#include<cstdio>
int solve(int n){
	if(n==1) return 1;
	if(n==2) return 2;
	return solve(n-1)+solve(n-2);
}
int main(){
	int n;
	while(scanf("%d",&n)!=EOF){
		printf("%d\n",solve(n));
	}
	return 0;
}*/
/*

题目描述
编写一个求斐波那契数列的递归函数，输入n 值，使用该递归函数，输出如下图形（参见样例）。

输入
输入第一行为样例数m，接下来有m行每行一个整数n，n不超过10。


输出
对应每个样例输出要求的图形(参见样例格式).


样例输入
1
6
样例输出
          0
        0 1 1
      0 1 1 2 3
    0 1 1 2 3 5 8
  0 1 1 2 3 5 8 13 21
0 1 1 2 3 5 8 13 21 34 55
 */

#include<cstdio>
int solve(int n,int* resA){
	int res;
	if(n==0){
		res=0;
	}else if(n==1){
		res=1;
	}else if(n==2){
		res=1;
	}else{
		res=solve(n-1,resA)+solve(n-2,resA);
	}
	resA[n]=res;
	return res;
}
int main(){
	int m,n,k;
    int resA[100];
    resA[0]=0;
	while(scanf("%d",&m)!=EOF){ scanf("%d",&n);
		solve(2*n-2,resA);
		k=2*(n-1);
		for(int i=1;i<=n;i++){
		    for(int s=0;s<k;s++){
		        printf(" ");
		    }
		    for(int j=0;j<=2*i-2;j++){
		        printf("%d",resA[j]);
		        if(j!=2*i-2) printf(" ");
		    }
		    k-=2;
			printf("\n");
		}
	}
	return 0;
}