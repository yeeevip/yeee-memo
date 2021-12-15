#include<cstdio>
#include<vector>
#include<queue>
using namespace std;
const int maxn=1000;
vector<int> G[maxn];
int n,m,inDegree[maxn];

bool topologicalSort(){
	int num=0;//记录加入拓扑序列的定点个数
	queue<int> q;
	for(int i;i<n;i++){
		if(inDegree[i]==0){
			q.push(i);
		}
	}

	while(!q.empty()){
		int u=q.front();
		//printf("%d",u);
		q.pop();
		for(int i=0;i<G[u].size();i++){
			int v=G[u][i];
			inDegree[v]--;
			if(inDegree[v]==0){
				q.push(v);
			}
		}
		G[u].clear();
		num++;//加入拓扑序列的定点数加1
	}
	if(num==n){
		return true;
	}else
		return false;
}

int main(){
	return 0;
}