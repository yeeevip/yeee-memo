#include<cstdio>
#include<vector>
#include<queue>
using namespace std;
const int maxn=100;
struct node{
	int data;
	int layer;
	vector<int> child;
}Node[maxn];

int index=0;
int newNode(int v){
	Node[index].data=v;
	Node[index].child.clear();//清空子节点
	return index++;
}

void preOrder(int root){
	printf("%d ",Node[root].data);
	for(int i=0;i<Node[root].child.size();i++){
		preOrder(Node[root].child[i]);
	}
}

void LayerOrder(int root){
	if(root==-1){
		return;
	}
	queue<int> q;
	q.push(root);
	Node[root].layer=0;
	while(!q.empty()){
		int now=q.front();
		q.pop();
		printf("%d",Node[root].data);
		for(int i=0;i<Node[root].child.size();i++){
			int child=Node[root].child[i];
			Node[child].layer=Node[now].layer+1;
			q.push(child);
		}
	}
}