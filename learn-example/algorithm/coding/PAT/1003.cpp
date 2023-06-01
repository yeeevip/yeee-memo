1003 Emergency (25)（25 分）

As an emergency rescue team leader of a city, you are given a special map of your country. The map shows several scattered cities connected by some roads. Amount of rescue teams in each city and the length of each road between any pair of cities are marked on the map. When there is an emergency call to you from some other city, your job is to lead your men to the place as quickly as possible, and at the mean time, call up as many hands on the way as possible.

Input

Each input file contains one test case. For each test case, the first line contains 4 positive integers: N (<= 500) - the number of cities (and the cities are numbered from 0 to N-1), M - the number of roads, C1 and C2 - the cities that you are currently in and that you must save, respectively. The next line contains N integers, where the i-th integer is the number of rescue teams in the i-th city. Then M lines follow, each describes a road with three integers c1, c2 and L, which are the pair of cities connected by a road and the length of that road, respectively. It is guaranteed that there exists at least one path from C1 to C2.

Output

For each test case, print in one line two numbers: the number of different shortest paths between C1 and C2, and the maximum amount of rescue teams you can possibly gather.\ All the numbers in a line must be separated by exactly one space, and there is no extra space allowed at the end of a line.

Sample Input

5 6 0 2
1 2 1 5 3
0 1 1
0 2 2
0 3 1
1 2 1
2 4 1
3 4 1
Sample Output

2 4



#include<cstdio>
#include<vector>
#include<cstring>
#include<algorithm>
using namespace std;
const int maxv=510;
const int INF=1000000000;
struct Node{
	int v;
	int d;
	Node(int _v,int _d):v(_v),d(_d) {}
};
vector<Node> Adj[maxv];
bool vis[maxv]={false};
int n,weight[maxv];
int d[maxv],w[maxv],num[maxv];

void Dijkstra(int s){
	fill(d,d+maxv,INF);
	memset(num,0,sizeof(num));
	memset(w,0,sizeof(w));
	d[s]=0;
	w[s]=weight[s];
	num[s]=1;
	for(int i=0;i<n;i++){
		int u=-1,MIN;
		for(int j=0;j<n;j++){
			if(vis[j]==false&&d[j]<MIN){
				u=j;
				MIN=d[j];
			}
		}
		if(u==-1) return;
		vis[u]=true;
		for(int j=0;j<Adj[u].size();j++){
			int v=Adj[u][j].v;
			if(vis[v]==false){
				if(d[u]+Adj[u][j].d<d[v]){
					d[v]=d[u]+Adj[u][j].d;
					w[v]=w[u]+weight[v];
					num[v]=num[u];
				}else if(d[u]+Adj[u][j].d==d[v]){
					if(w[u]+weight[v]>w[v]){
						w[v]=w[u]+weight[v];
					}
					num[v]+=num[u];
				}
			}
		}
	}
}

int main(){
	int m,st,ed;
	scanf("%d%d%d%d",&n,&m,&st,&ed);
	for(int i=0;i<n;i++){
		scanf("%d",&weight[i]);
	}
	for(int i=0;i<m;i++){
		int u,v,d;
		scanf("%d%d",&u,&v);
		scanf("%d",&d);
		Adj[u].push_back(Node(v,d));
		Adj[v].push_back(Node(u,d));
	}
	Dijkstra(st);
	printf("%d %d\n",num[ed],w[ed]);
	return 0;
}



#include<cstdio>
#include<cstring>
#include<algorithm>
using namespace std;
const int MAXV = 510;
const int INF = 1000000000;

int n,m,st,ed,G[MAXV][MAXV],weight[MAXV];
int d[MAXV],w[MAXV],num[MAXV];
bool vis[MAXV] ={false};

void Dijkstra(int s){
	fill(d,d+MAXV,INF);
	memset(num,0,sizeof(num));
	memset(w,0,sizeof(w));
	d[s]=0;
	w[s]=weight[s];
	num[s]=1;
	for(int i=0;i<n;i++){
		int u=-1,MIN;
		for(int j=0;j<n;j++){
			if(vis[j]==false&&d[j]<MIN){
				u=j;
				MIN=d[j];
			}
		}
		if(u==-1) return;
		vis[u]=true;
		for(int v=0;v<n;v++){
			if(vis[v]==false&&G[u][v]!=INF){
				if(d[u+G[u][v]<d[v]]){
					d[v]=d[u]+G[u][v];
					w[v]=w[u]+weight[v];
					num[v]=num[u];
				}else if(d[u]+G[u][v]==d[v]){
					if(w[u]+weight[v]>w[v]){
						w[v]=w[u]+weight[v];
					}
					num[v]+=num[u];
				}
			}
		}
	}

}

int main(){
	scanf("%d%d%d%d",&n,&m,&st,&ed);
	for(int i=0;i<n;i++){
		scanf("%d",weight[i]);
	}
	int u,v;
	for(int i=0;i<m;i++){
		scanf("%d%d",&u,&v);
		scanf("%d",&G[u][v]);
		G[v][u]=G[u][v];
	}
	Dijkstra(st);
	printf("%d %d\n",num[ed],w[ed]);
	return 0;
}




#include<cstdio>
#include<vector>
#include<algorithm>
using namespace std;
const int maxn=510;
const int INF=100000000;

int N,M,s,e;

struct node{
	int v;
	int dis;
	node(int _v,int _dis):v(_v),dis(_dis){}
};
bool vis[maxn]={false};
vector<node> Adj[maxn];
int weight[maxn];
int d[maxn],w[maxn],num[maxn];

void Dijkstra(int s){
	fill(d,d+maxn,INF);
	fill(w,w+maxn,0);
	fill(num,num+maxn,0);
	d[s]=0;
	w[s]=weight[s];
	num[s]=1;

	for(int i=0;i<N;i++){
		int u=-1,MIN=INF;
		for(int j=0;j<N;j++){
			if(vis[j]==false&&d[j]<MIN){
				u=j;
				MIN=d[j];
			}
		}
		if(u==-1) return;
		vis[u]=true;
		for(int j=0;j<Adj[u].size();j++){
			node nv=Adj[u][j];
			int v=nv.v;
			if(vis[v]==false){
				if(d[u]+nv.dis<d[v]){
					d[v]=d[u]+nv.dis;
					w[v]=w[u]+weight[v];
					num[v]=num[u];
				}else if(d[u]+nv.dis==d[v]){
					if(w[u]+weight[v]>w[v]){
						w[v]=w[u]+weight[v];
					}
					num[v]+=num[u];
				}
			}
		}
	}

}


int main(){
	scanf("%d %d %d %d",&N,&M,&s,&e);
	for(int i=0;i<N;i++){
		scanf("%d",&weight[i]);
	}
	int u,v,we;
	for(int i=0;i<M;i++){
		scanf("%d %d %d",&u,&v,&we);
		Adj[u].push_back(node(v,we));
		Adj[v].push_back(node(u,we));
	}
	Dijkstra(s);
	printf("%d %d",num[e],w[e]);
	return 0;
}