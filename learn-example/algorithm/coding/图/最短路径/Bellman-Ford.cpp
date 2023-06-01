#include<cstdio>
#include<vector>
#include<algorithm>
using namespace std;
const int maxn=1000;
const int INF=1000000000;
bool vis[maxn]={false};

struct node{
	int v,dis;
};
vector<node> Adj[maxn];

int d[maxn];
int n;
bool Bellman(int s){
	fill(d,d+maxn,INF);
	d[s]=0;
	for(int i=0;i<n-1;i++){
		for(int u=0;u<n;u++){
			for(int j=0;j<Adj[u].size();j++){
				int v=Adj[u][j].v;
				int dis=Adj[u][j].dis;
				if(d[u]+dis<d[v]){
					d[v]=d[u]+dis;
				}
			}
		}
	}
	for(int u=0;u<n;u++){
		for(int j=0;j<Adj[u].size();j++){
			int v=Adj[u][j].v;
			int dis=Adj[u][v].dis;
			if(d[u]+dis<d[v]){
				return false;
			}
		}
	}
	return true;
}



int main(){
	return 0;
}