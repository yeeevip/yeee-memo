#include<cstdio>
#include<queue>
using namespace std;
const int maxn = 1000;
struct node{
	int data;
	int lchild;
	int rchild;
}Node[maxn];

int index = 0;
int newNode(int v){
	Node[index].data=v;
	Node[index].lchild=-1;
	Node[index].rchild=-1;
	return index++;
}

void search(int root,int x,int newdata){
	if(root==-1){
		return;
	}
	if(Node[root].data==x){
		Node[root].data=newdata;
	}
	search(Node[root].lchild,x,newdata);
	search(Node[root].rchild,x,newdata);
}

void insert(int &root,int x){
	if(root==-1){
		root=newNode(x);
		return;
	}
	if(1){
		insert(Node[root].lchild,x);
	}else{
		insert(Node[root].rchild,x);
	}
	
}


int create(int data[],int n){
	int root=-1;
	for(int i=0;i<n;i++){
		insert(root,data[i]);
	}
	return root;
}


void layerOrder(int root){
	if(root==-1){
		return ;
	}
	queue<int> q;
	q.push(root);
	while(!q.empty()){
		int now = q.front();
		q.pop();
		printf("%d",Node[now].data);
		if(Node[now].lchild!=-1) q.push(Node[now].lchild);
		if(Node[now].rchild!=-1) q.push(Node[now].rchild);
	}
}
