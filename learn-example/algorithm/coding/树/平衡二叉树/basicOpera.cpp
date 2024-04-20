#include<cstdio>
#include<algorithm>
using namespace std;
struct node{
	int data,height;
	node *lchild,*rchild;
};

node* newNode(int v){
	node* Node = new node;
	Node->data=v;
	Node->height=1;
	Node->lchild=Node->rchild=NULL;
	return Node;
}
int getHeight(node* root){
	if(root==NULL) return 0;
	return root->height;
}
int getBalanceFactor(node* root){
	return getHeight(root->lchild)-getHeight(root->rchild);
}

void updateHeight(node* root){
    if(root->lchild==NULL){
        updateHeight(root->lchild)
    }
    if(root->rchild==NULL){
        updateHeight(root->rchild)
    }
	root->height=max(getHeight(root->lchild),getHeight(root->rchild))+1;
}

//平衡二叉树的基本操作
void search(node* root,int x){
	if(root==NULL){
		printf("null");
		return;
	}
	if(root->data==x){
		printf("success,%d",root->data);
	}else if(x<root->data){
		search(root->lchild,x);	
	}else{
		search(root->rchild,x);
	}
} 
//左旋left rotation 
void L(node* root){
	node* temp=root->rchild;
	root->rchild=temp->lchild;
	temp->lchild=root;
	updateHeight(root);
	updateHeight(temp);
	root=temp;
}
//右旋
void R(node* root){
	node* temp=root->lchild;
	root->lchild=temp->rchild;
	temp->rchild=root;
	updateHeight(root);
	updateHeight(temp);
	root=temp;
}
void insert(node* &root,int v){
	if(root==NULL){
		root=newNode(v);
		return;
	}
	if(v<root->data){
		insert(root->lchild,v);
		updateHeight(root);
		if(getBalanceFactor(root)==2){
			if(getBalanceFactor(root->lchild)==1){//LL
				R(root);
			}else if(getBalanceFactor(root->lchild)==-1){//LR
				L(root->lchild);
				R(root);
			}
		} 
	}else{
		insert(root->rchild,v);
		updateHeight(root);
		if(getBalanceFactor(root)==-2){
			if(getBalanceFactor(root->rchild)==-1){//RR
				L(root);
			}else if(getBalanceFactor(root->rchild)==1){//RL
				R(root->rchild);
				L(root);
			}
		}
	}
} 

//AVL树的建立
node* Create(int array[],int n){
	node* root=NULL;
	for(int i=0;i<n;i++){
		insert(root,array[i]);
	}
	return root;
} 

int main(){
	return 0;
}