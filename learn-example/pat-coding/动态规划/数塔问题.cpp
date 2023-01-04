#include<cstdio>
#include<algorithm>
using namespace std;
const int maxn=1000;
int f[maxn][maxn],dp[maxn][maxn];
int main(){
	int n;
	scanf("%d",&n);
	for(int i=1;i<=n;i++){
		for(int j=1;j<=i;j++){
			scanf("%d",&f[i][j]);
		}
	}
	//边界
	for(int j=1;j<=n;j++){
		dp[n][j]=f[n][j];
	}
	//从第n-1层不断往上计算出dp[i][j]
	for(int i=n-1;i>=1;i--){
		for(int j=1;j<=i;j++){
			//状态转移方程
			dp[i][j]=max(dp[i+1][j],dp[i+1][j+1])+f[i][j];
		}
	}
	printf("%d\n",dp[1][1]);
	return 0;
}