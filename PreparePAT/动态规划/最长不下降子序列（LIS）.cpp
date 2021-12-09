#include<cstdio>
#include<algorithm>
using namespace std;
const int N=100;
int A[N],dp[N];
int main(){
	int n;
	scanf("%d",&n);
	for(int i=1;i<=n;i++){
		scanf("%d",&A[i]);
	}
	int ans=-1;//记录最大的dp[i]
	for(int i=1;i<=n;i++){//按顺序计算出dp[i]的值
		dp[i]=1;//边界初始条件（先假定每个元素自成一个子序列）
		for(int j=1;j<i;j++){
			if(A[i]>=A[j]&&(dp[j]+1>dp[i])){
				dp[i]=dp[j]+1;//状态转移方程，用以更新dp[i]
			}
		}
		ans=max(ans,dp[i]);
	}
	printf("%d\n",ans);

	return 0;
}