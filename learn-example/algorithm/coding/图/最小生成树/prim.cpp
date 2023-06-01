#include<cstdio>
#include<algorithm>
#include<vector>
using namespace std;
const int maxn=1000;
const int INF=1000000000;
int G[maxn][maxn];
bool vis[maxn]={false};
int n,d[maxn];

int prim(){
	fill(d,d+maxn,INF);
	d[0]=0;
	int ans=0;
	for(int i=0;i<n;i++){
		int u=-1,MIN=INF;
		for(int j=0;j<n;j++){
			if(vis[j]==false&&d[j]<MIN){
				u=j;
				MIN=d[j];
			}
		}
		if(u==-1) return -1;
		vis[u]=true;
		ans+=d[u];
		for(int v=0;v<n;v++){
			if(vis[v]==false&&G[u][v]!=INF&&G[u][v]<d[v]){
				d[v]=G[u][v];
			}
		}
	}
	return ans;
}

struct node{
	int v,dis;
};

vector<node> Adj[maxn];

int prim2(){
	fill(d,d+maxn,INF);
	d[0]=0;
	int ans=0;
	for(int i=0;i<n;i++){
		int u=-1,MIN=INF;
		for(int j=0;j<n;j++){
			if(vis[j]==false&&d[j]<MIN){
				u=j;
				MIN=d[j];
			}
		}
		if(u==-1)return -1;
		vis[u]=true;
		ans+=d[u];
		for(int j=0;j<Adj[u].size();j++){
			int v=Adj[u][i].v;
			int dis=Adj[u][i].dis;
			if(vis[v]==false&&dis<d[v]){
				d[v]=dis;
			}
		}
	}
	return ans;
}


int main(){
	return 0;
}


#include<cstdio>
#include<algorithm>
#include<vector>
using namespace std;
const int maxn=1000;
const int INF=1000000000;
struct node{
	int v,dis;
	node(int _v,int _dis):v(_v),dis(_dis){}
};
int n,m,d[maxn];
bool vis[maxn];

vector<node> Adj[maxn];

int prim(){
	fill(d,d+maxn,INF);
	fill(vis,vis+maxn,false);
	d[0]=0;
	int ans=0;
	for(int i=0;i<n;i++){
		int u=-1,MIN=INF;
		for(int j=0;j<n;j++){
			if(vis[j]==false&&d[j]<MIN){
				u=j;
				MIN=d[j];
			}
		}
		if(u==-1) return -1;
		vis[u]=true;
		ans+=d[u];
		for(int j=0;j<Adj[u].size();j++){
			int v=Adj[u][j].v;
			int dis =Adj[u][j].dis;
			if(vis[v]==false&&dis<d[v]){
				d[v]=dis;
			}
		}
	}
	return ans;
}

int main(){
	scanf("%d %d",&n,&m);
	int u,v,w;
	for(int i=0;i<m;i++){
		scanf("%d %d %d",&u,&v,&w);
		Adj[u].push_back(node(v,w));
		Adj[v].push_back(node(u,w));
	}
	printf("%d\n",prim());
	return 0;
}
