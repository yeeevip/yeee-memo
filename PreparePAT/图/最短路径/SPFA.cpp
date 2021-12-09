#include<cstdio>
#include<vector>
#include<cstring>
#include<queue>
#include<algorithm>
using namespace std;
const int maxn=1000;
const int INF=1000000000;
struct node{
	int v,dis;
};
int n,d[maxn],num[maxn];//num记录顶点的入队次数
vector<node> Adj[maxn];
bool inq[maxn];//顶点是否在队列中

bool SPFA(int s){
	memset(inq,false,sizeof(inq));
	memset(num,0,sizeof(num));
	fill(d,d+maxn,INF);

	//源点入队部分
	queue<int> Q;
	Q.push(s);
	inq[s]=true;
	num[s]++;
	d[s]=0;
	while(!Q.empty()){
		int u =Q.front();
		Q.pop();
		inq[u]=false;
		//遍历u所有的邻接边
		for(int i=0;i<Adj[u].size();i++){
			int v=Adj[u][i].v;
			int dis=Adj[u][i].dis;
			//松弛操作
			if(d[u]+dis<d[v]){
				d[v]=d[u]+dis;
				if(!inq[v]){
					Q.push(v);
					inq[v]=true;
					num[v]++;
					if(num[v]>=n){
						return false;//有可达的负环
					}
				}
			}
		}
	}
	return true;//没有可达的负环
}

int main(){
	return 0;
}