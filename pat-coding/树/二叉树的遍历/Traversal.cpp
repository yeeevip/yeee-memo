//二叉树的基本操作

#include<cstdio>
#include<queue>
using namespace std;

struct node{
	int data;
	int layer;
	node* lchild;
	node* rchild;
};

node* root = NULL;

node* newNode(int v){
	node* Node = new node;
	Node->data=v;
	Node->lchild=Node->rchild=NULL;
	return Node;
}

void search(node* root,int x,int newdata){
	if(root==NULL) return;
	if(root->data==x){
		root->data=newdata;
	}
	search(root->lchild,x,newdata);
	search(root->rchild,x,newdata);
}


void insert(node* &root,int x){
	if(root==NULL){
		root->data=x;
		return;
	}
//	if(由二叉树的性质，x应该插在左子树){
//		insert(root->lchild,x);
//	}else{
//		insert(root->rchild,x)
//	}
}

node* Creat(int data[],int n){
	node* root=NULL;
	for(int i=0;i<n;i++){
		insert(root,data[i]);
	}
	return root;
}


void preorder(node* root){
	if(root==NULL){
		return;
	}
	printf("访问根节点，例如输出数据--%d",root->data);
	preorder(root->lchild);
	preorder(root->rchild);
}

void inorder(node* root){
	if(root==NULL){
		return;
	}
	inorder(root->lchild);
	printf("%d",root->data);
	inorder(root->rchild);
}

void postorder(node* root){
	if(root==NULL){
		return;
	}
	postorder(root->lchild);
	postorder(root->rchild);
	printf("%d",root->data);
}


void LayerOrder(node* root){
	if(root==NULL){
		return;
	}
	queue<node*> q;
	q.push(root);

	while(!q.empty()){
		node* top = q.front();
		q.pop();
		printf("%d",top->data);
		if(top->lchild!=NULL) q.push(top->lchild);
		if(top->rchild!=NULL) q.push(top->rchild);
	}

}

void LayerOrderL(node* root){
	if(root==NULL){
		return;
	}
	queue<node*> q;
	root->layer=1;
	q.push(root);
	while(!q.empty()){
		node* now = q.front();
		q.pop();
		printf("%d",now->data);
		if(now->lchild!=NULL){
			now->lchild->layer=now->layer+1;
			q.push(now->lchild);
		}
		if(now->rchild!=NULL){
			now->rchild->layer=now->layer+1;
			q.push(now->rchild);
		}
	}

}


//给定一颗二叉树的中序遍历序列和先序遍历序列，重建这颗二叉树
int in[10],pre[10];
node* createByOrder(int preL,int preR,int inL,int inR){
	if(preL>preR){
		return NULL;
	}
	node* root=new node;
	root->data=pre[preL];
	int k;
	for(k=inL;k<=inR;k++){
		if(pre[preL]==in[k]){
			break;
		}
	}
	int numLeft=k-inL;//左子树的节点个数
	root->lchild=createByOrder(preL+1,preL+numLeft,inL,k-1);
	root->rchild=createByOrder(preL+numLeft+1,preR,k+1,inR);
	return root;
}




int main(){
	return 0;
}