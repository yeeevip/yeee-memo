const int MAXV=1000;
const int INF = 100000000;

int n,G[MAXV][MAXV];
bool vis[MAXV]={false};

void DFS(int u,int depth){
	vis[u]=true;
	for(int v=0;v<n;v++){
		if(vis[v]==false&&G[u][v]!=INF){
			DFS(v,depth+1);
		}
	}
}


vector<int> Adj[MAXV];

void DFS(int u,int depth){
	vis[u]=true;
	for(int i=0;i<Adj[u].size();i++){
		int v=Adj[u][i];
		if(vis[v]==false){
			DFS(v,depth+1);
		}
	}
}

void DFSTrave(){
	for(int u=0;u<n;u++){
		if(vis[u]==false){
			DFS(u,1);
		}
	}
}





//广度优先搜索

bool inq[MAXV]={false};

void BFS(int u){
	queue<int> q;
	q.push(u);
	inq[u]=true;
	while(!q.empty()){
		int u=q.top();
		q.pop();
		for(int v=0;v<n;v++){
			if(inq[v]==false&&G[u][v]!=INF){
				q.push(v);
				inq[v]=true;
			}
		}
	}
}


vector<int> Adj[MAXV];
void BFS(int u){
	queue<int> q;
	q.push(u);
	inq[u]=true;
	while(!q.empty()){
		for(int i=0;i<Adj[u].size();i++){
			int v=Adj[u][i];
			if(inq[v]==false){
				q.push(v);
				inq[v]=true;
			}
		}
	}
}


void BFSTravel(){
	for(int u=0;u<n;u++){
		if(inq[u]==false){
			BFS(u);
		}
	}
}



#include<cstdio>
#include<queue>
#include<vector>
using namespace std;
const int maxn=10010;
const int INF=1000000000;
int n;
bool inq[maxn]={false};
int G[maxn][maxn];

void BFS(int u){
	queue<int> q;
	q.push(u);
	inq[u]=true;
	while(!q.empty()){
		int u=q.front();
		q.pop();
		for(int v=0;v<n;v++){
			if(inq[v]==false&&G[u][v]!=INF){
				q.push(v);
				inq[v]=true;
			}
		}
	}
}

vector<int> Adj[maxn];

void BFS2(int u){
	queue<int> q;
	q.push(u);
	inq[u]=true;
	while(!q.empty()){
		int u=q.front();
		for(int i=0;i<Adj[u].size();i++){
			int v=Adj[u][i];
			if(inq[v]==false){
				q.push(v);
				inq[v]=true;
			}
		}
	}
}


struct node{
	int v;
	int layer;
};

vector<node> Adj2[maxn];

void BFS3(int s){
	queue<node> q;
	node start;
	start.v=s;
	start.layer=0;
	q.push(start);
	inq[start.v]=true;
	while(!q.empty()){
		node t=q.front();
		int u=t.v;
		q.pop();
		for(int i=0;i<Adj2[u].size();i++){
			node next=Adj2[u][i];
			next.layer=t.layer+1;
			if(inq[next.v]==false){
				q.push(next);
				inq[next.v]=true;
			}
		}
	}
}


void BFSTrave(){
	for(int u=0;u<n;u++){
		if(inq[u]==false){
			BFS(u);
		}
	}
}

int main(){
	return 0;
}

