#include<cstdio>
#include<algorithm>
#include<vector>
using namespace std;
const int maxn=1000;
const int INF=1000000000;

int n,m,s,G[maxn][maxn],weight[maxn];
int d[maxn],c[maxn],w[maxn],num[maxn];
int pre[maxn],cost[maxn][maxn];
bool vis[maxn]={false};

void Dijkstra(int s){
	fill(d,d+maxn,INF);
	fill(c,c+maxn,INF);
	fill(w,w+maxn,0);
	fill(num,num+maxn,0);
	for(int i=0;i<n;i++) pre[i]=i;
	d[s]=0;
	c[s]=0;
	w[s]=weight[s];
	num[s]=1;
	for(int i=0;i<n;i++){
		int u=-1,MIN=INF;
		for(int j=0;j<n;i++){
			if(vis[j]==false&&d[j]<MIN){
				u=j;
				MIN=d[j];
			}
		}
		if(u==-1) return;
		vis[u]=true;
		for(int v=0;v<n;v++){
			if(vis[v]==false&&G[u][v]!=INF&&d[u]+G[u][v]<d[v]){
				d[v]=d[u]+G[u][v];
				pre[v]=u;//记录v的前驱节点是u
			}

			//第二标尺

			//新增边权
			if(vis[v]==false&&G[u][v]!=INF){
				if(d[u]+G[u][v]<d[v]){
					d[v]=d[u]+G[u][v];
					c[v]=c[u]+cost[u][v];
				}else if(d[u]+G[u][v]==d[v]&&c[u]+cost[u][v]<c[v]){
					c[v]=c[u]+cost[u][v];
				}
			}

			//新增点权
			if(vis[v]==false&&G[u][v]!=INF){
				if(d[u]+G[u][v]<d[v]){
					d[v]=d[u]+G[u][v];
					w[v]=w[u]+weight[v];
				}else if(d[u]+G[u][v]==d[v]&&w[u]+weight[v]>w[v]){
					w[v]=w[u]+weight[v];
				}
			}


			//求最短路径条数
			if(vis[v]==false&&G[u][v]!=INF){
				if(d[u]+G[u][v]<d[v]){
					d[v]=d[u]+G[u][v];
					num[v]=num[u];
				}else if(d[u]+G[u][v]==d[v]){
					num[v]+=num[u];
				}
			}

		}
	}
}

void DFS(int s,int v){//s为起点编号，v为当前访问顶点编号
	if(v==s){
		printf("%d\n",s);
		return;
	}
	DFS(s,pre[v]);
	printf("%d\n",v);
}


struct node{
	int v;
	int dis;
};
vector<node> Adj[maxn];
void Dijkstra2(int s){
	fill(d,d+maxn,INF);
	d[s]=0;
	for(int i=0;i<n;i++){
		int u=-1,MIN=INF;
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
			if(vis[v]==false&&d[u]+Adj[u][j].dis<d[v]){
				d[v]=d[u]+Adj[u][j].dis;
			}
		}
	}
}


int main(){
	int u,v,w;
	fill(G[0],G[0]+maxn*maxn,INF);
	scanf("%d %d %d",&n,&m,&s);
	for(int i=0;i<m;i++){
		scanf("%d %d %d",&u,&v,&w);
		G[u][v]=w;
	}
	Dijkstra(s);
	for(int i=0;i<n;i++){
		printf("%d\n",d[i]);
	}
	return 0;
}


#include<cstdio>
#include<vector>
#include<algorithm>
using namespace std;
const int maxn=1000;
const int INF=1000000000;
int G[maxn][maxn];
int n,d[maxn],weight[maxn];
bool vis[maxn]={false};

vector<int> pre[maxn];

void Dijkstra(int s){
	fill(d,d+maxn,INF);
	d[s]=0;
	for(int i=0;i<n;i++){
		int u=-1,MIN=INF;
		for(int j=0;j<n;j++){
			if(vis[j]==false&&d[j]<MIN){
				u=j;
				MIN=d[j];
			}
		}
		if(u==-1)return;
		vis[u]=true;
		for(int v=0;v<n;v++){
			if(vis[v]==false&&G[u][v]!=INF){
				if(d[u]+G[u][v]<d[v]){
					d[v]=d[u]+G[u][v];
					pre[v].clear();
					pre[v].push_back(u);
				}else if(d[u]+G[u][v]==d[v]){
					pre[v].push_back(u);
				}
			}
		}
	}
}


int optvalue,st;
vector<int> path,tempPath;//最优路径、临时路径
void DFS(int v){//v当前访问节点
	if(v==st){
		tempPath.push_back(v);//将起点加入临时路径最后面
		int value;//临时路径的第二标尺的值
		if(value>optvalue){
			optvalue=value;
			path=tempPath;
		}
		tempPath.pop_back();
		return;
	}
	tempPath.push_back(v);
	for(int i=0;i<pre[v].size();i++){
		DFS(pre[v][i]);
	}
	tempPath.pop_back();
}

//最优路径的边权之和    点权之和
int ansD=0,ansW=0;
void calculate(){
	for(int i=tempPath.size()-1;i>0;i--){
		int u=tempPath[i],v=tempPath[i-1];
		ansD+=G[u][v];
	}
	for(int i=tempPath.size()-1;i>=0;i--){
		ansW+=weight[i];
	}
}

int main(){
	return 0;
}