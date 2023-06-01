#include<cstdio>
#include<stack>
#include<queue>
#include<vector>
#include<cstring>
#include<algorithm>
using namespace std;
const int maxn=1000;
struct node{
	int v,w;
};
vector<node> G[maxn];
int inDegree[maxn],n;

int ve[maxn],vl[maxn];

stack<int> topOrder;

bool topologicalSort(){
	queue<int> q;
	int num=0;
	for(int i=0;i<n;i++){
		if(inDegree[i]==0){
			q.push(i);
		}
	}
	while(!q.empty()){
		int u=q.front();
		q.pop();
		topOrder.push(u);
		for(int i=0;i<G[u].size();i++){
			int v=G[u][i].v;
			inDegree[v]--;
			if(inDegree[v]==0){
				q.push(v);
			}

			//用ve[u]来更新所有的后继节点v
			if(ve[u]+G[u][i].w>ve[v]){
				ve[v]=ve[u]+G[u][i].w;
			}
		}
		num++;
	}
	if(num==n){
		return true;
	}else return false;
}

//关键路径，不是有向无环图返回-1，否则返回关键路径长度
int CriticalPath(){
	memset(ve,0,sizeof(ve));
	if(topologicalSort==false){
		return -1;//不是有向无环图
	}

	//fill(vl,vl+n,ve[n-1]);//vl	数组初始化，初始值为会点的ve值

	//如果事先不知道汇点编号，取ve数组的最大值
	int maxLength=0;
	for(int i=0;i<n;i++){
		if(ve[i]>maxLength){
			maxLength=ve[i];
		}
	}
	fill(vl,vl+n,maxLength);

	//直接使用topOrder出栈即为逆拓扑排列，求解vl数组
	while(!topOrder.empty()){
		int u=topOrder.top();
		topOrder.pop();
		for(int i=0;i<G[u].size();i++){
			int v=G[u][i].v;
			//用u的所有后继结点v的vl值来更新vl[u]
			if(vl[v]-G[u][i].w<vl[u]){
				vl[u]=vl[v]-G[u][v].w;
			}
		}
	}

	//遍历邻接表的所有边，计算活动的最早开始时间e和最迟开始时间l
	for(int u=0;u<n;u++){
		for(int i=0;i<G[u].size();i++){
			int v=G[u][i].v,w=G[u][i].w;
			//活动的最早开始时间e和最迟开始时间l
			int e=ve[u],l=vl[v]-w;
			//如果e==l，说明活动u->v是关键活动
			if(e==l){
				printf("%d->%d\n",u,v);
			}
		}
	}
	return ve[n-1];//返回关键路径长度
}

int main(){
	return 0;
}
