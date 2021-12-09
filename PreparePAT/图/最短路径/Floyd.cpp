#include<cstdio>
#include<algorithm>
using namespace std;
const int maxn=200;
const int INF=1000000000;
int n,m;
int d[maxn][maxn];//d[i][j]表示顶点i和j最短距离

void Floyd(){
	for(int k=0;k<n;k++){
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(d[i][k]!=INF&&d[k][j]!=INF&&d[i][k]+d[k][j]<d[i][j]){
					d[i][j]=d[i][k]+d[k][j];
				}
			}
		}
	}
}

int main(){
	fill(d[0],d[0]+maxn*maxn,INF);
	scanf("%d %d",&n,&m);
	for(int i=0;i<n;i++) d[i][i]=0;
	int u,v,w;
	for(int i=0;i<m;i++){
		scanf("%d %d %d",&u,&v,&w);
		d[u][v]=w;
	}
	Floyd();
	for(int i=0;i<n;i++){
		for(int j=0;j<n;j++){
			printf("%d ",d[i][j]);
		}
		printf("\n");
	}
	return 0;
}


/*
Floyd最小环算法
		问题描述： 给你一张无向图，定义环为从i出发到达j然后从j返回i并且所有点都只经过一次（最少为3个点），求所有环当中经过路径最小的环



		算法描述： 首先容易想到的是暴力来枚举环，当删除其中一条边ij后再跑一边从i到j的最短路，然后加上边ij的值就是含有边ij的最小环的值，这样最坏的                    时间复杂度可以达到O(n^4)，显然复杂度有点大。

                   所以我们考虑降低时间复杂度，我们来分析下floyd的实现过程，当枚举顶点k之前我们已经求得了顶点为1  -  k-1 的最短                     

                   路，所以我们可以在跟新k之前枚举k之前的i和j的组合，我们可以知道dis[i][j]没有经过k点,所以我们就可以知道

                   如果dis[i][j]+mp[i][k]+mp[k][j] != inf(mp[i][j]为没有跟新得边值) 时就存在一条经过ijk的最小环,所以我们要求的是所有环当                   

                   中最小的哪一个！



HDU1599：

find the mincost route
Time Limit: 1000/2000 MS (Java/Others)    Memory Limit: 32768/32768 K (Java/Others)
Total Submission(s): 4556    Accepted Submission(s): 1838


Problem Description
杭州有N个景区，景区之间有一些双向的路来连接，现在8600想找一条旅游路线，这个路线从A点出发并且最后回到A点，假设经过的路线为V1,V2,....VK,V1,那么必须满足K>2,就是说至除了出发点以外至少要经过2个其他不同的景区，而且不能重复经过同一个景区。现在8600需要你帮他找一条这样的路线，并且花费越少越好。
 

Input
		第一行是2个整数N和M（N <= 100, M <= 1000)，代表景区的个数和道路的条数。
接下来的M行里，每行包括3个整数a,b,c.代表a和b之间有一条通路，并且需要花费c元(c <= 100)。
 

Output
		对于每个测试实例，如果能找到这样一条路线的话，输出花费的最小值。如果找不到的话，输出"It's impossible.".
 

Sample Input
3 3 1 2 1 2 3 1 1 3 1 3 3 1 2 1 1 2 3 2 3 1
 

Sample Output
3 It's impossible.*/

#include<algorithm>
#include<iostream>
#include<cstdio>

using namespace std;

const int maxn = 105;
const int inf = 1e8;

class Floyd{
public:

	int n,m;
	int dis[maxn][maxn],mp[maxn][maxn];

	int init(){
		if(scanf("%d%d",&n,&m)!=2)return -1;
		for(int i=1;i<=n;i++)for(int j=1;j<=n;j++)mp[i][j]=dis[i][j]=inf;
		for(int i=0;i<m;i++){
			int u,v,w;scanf("%d%d%d",&u,&v,&w);
			mp[u][v]=mp[v][u]=dis[u][v]=dis[v][u]=min(w,mp[u][v]);
		}
	}

	void floyd(){
		int MinCost = inf;
		for(int k=1;k<=n;k++){

			//因为路径i到j的情况只有经过k和不经过k，而要求从一个点至少经过两个节点返回原点，
			// k每次更新都会使dis[i][j]得到更新，而只有在更新了一次k之后才可以找min，
			// min即是在dis[i][i]最短的情况下的求至少经过两个点又回到该点的最小距离，
			// 所以i和j的值都应该小于k，i的值从1到k-1，而j的值却跟i的值相关，即i!=j，因为当i=j时，dis[i][j]不是无穷大，
			// 而是从i->j->i的值，这就会出现自环，这里我定义自环为经过一个节点就返回原节点的节点，比如像1->2->1这样min的值会不准确，
			// 这不是经过了两个节点,所以下面第一个两层循环可以有三种写法，具体看代码


			//当要扩充第k个节点时，前k-1个节点已经用过，并且是用于更新最短路径dis[i][j]这就是第二个两层for循环，
			// 所以在更新k之前已经有一条最短路径从i到达j，此时再来寻找另外一个从i到j的路径，map[j][k]+map[k][i]，
			// 如果有的话则一定形成了从i回到i的环，比如 1->2值为1，2->3值为2,3->4值为3,4->1值为4，
			// 则第一次存在从1到3的最短路，再寻找时找到了1到4,4到3的路径，则形成了环，而且是最小的，
			// 注意第一个循环中加上的值是map[j][k]和map[k][i]的值，map的是值都是初始值，不会变化，而dis在不断更新
			for(int i=1;i<k;i++)
				for(int j=i+1;j<k;j++)
					MinCost = min(MinCost,dis[i][j]+mp[i][k]+mp[k][j]);//跟新k点之前枚举ij求经过ijk的最小环
			for(int i=1;i<=n;i++)
				for(int j=1;j<=n;j++)
					dis[i][j]=min(dis[i][j],dis[i][k]+dis[k][j]);      //跟新k点
		}
		if(MinCost==inf)puts("It's impossible.");
		else printf("%d\n",MinCost);
	}

}fd;

int main()
{
	while(~fd.init())fd.floyd();
	return 0;
}