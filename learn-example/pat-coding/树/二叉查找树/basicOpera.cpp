//二叉查找树BST
#include<cstdio>

struct node{
	int data;
	node* lchild;
	node* rchild;
};

void search(node* root,int x){
	if(root==NULL) return;
	if(root->data==x){
		printf("%d",root->data);
	}else if(x<root->data){
		search(root->lchild,x);
	}else{
		search(root->rchild,x);
	}

}

void insert(node* &root,int x){
	if(root==NULL){
		node* newN = new node;
		newN->data=x;
		newN->lchild=newN->rchild=NULL;
		root=newN;
	}else if(root->data==x){
		return;
	}else if(root->data>x){
		search(root->lchild,x);
	}else{
		search(root->rchild,x);
	}
}


node* create(int array[],int n){
	node* root=NULL;
	for(int i=0;i<n;i++){
		insert(root,array[i]);
	}
}

node* findPre(node* root){
	while(root->rchild!=NULL){
		root=root->rchild;
	}
	return root;
}

node* findNext(node* root){
	while(root->lchild!=NULL){
		root=root->lchild;
	}
	return root;
}


void deleteNode(node* &root,int x){
	if(root==NULL) return;
	if(root->data==x){
		if(root->lchild==NULL&&root->rchild==NULL){
			root=NULL;
		}else if(root->lchild!=NULL){
			node* pre =findPre(root->lchild);
			root->data=pre->data;
			deleteNode(pre,pre->data);
		}else{
			node* next=findNext(root->rchild);
			root->data=next->data;
			deleteNode(next,next->data);
		}
	}else if(x<root->data){
		deleteNode(root->lchild,x);
	}else{
		deleteNode(root->rchild,x);
	}
}


int main(){
	return 0 ;
}
