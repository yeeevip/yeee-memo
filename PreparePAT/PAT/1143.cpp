1143 Lowest Common Ancestor（30 分）

The lowest common ancestor (LCA) of two nodes U and V in a tree is the deepest node that has both U and V as descendants.
A binary search tree (BST) is recursively defined as a binary tree which has the following properties:
The left subtree of a node contains only nodes with keys less than the node's key.
The right subtree of a node contains only nodes with keys greater than or equal to the node's key.
Both the left and right subtrees must also be binary search trees.
Given any two nodes in a BST, you are supposed to find their LCA.
Input Specification:

Each input file contains one test case. For each case, the first line gives two positive integers: M (≤ 1,000), the number of pairs of nodes to be tested; and N (≤ 10,000), the number of keys in the BST, respectively. In the second line, N distinct integers are given as the preorder traversal sequence of the BST. Then M lines follow, each contains a pair of integer keys U and V. All the keys are in the range of int.
Output Specification:

For each given pair of U and V, print in a line LCA of U and V is A. if the LCA is found and A is the key. But if A is one of U and V, print X is an ancestor of Y. where X is A and Y is the other node. If U or V is not found in the BST, print in a line ERROR: U is not found. or ERROR: V is not found. or ERROR: U and V are not found..
Sample Input:

6 8
6 3 1 2 5 4 8 7
2 5
8 7
1 9
12 -3
0 8
99 99
Sample Output:

LCA of 2 and 5 is 3.
8 is an ancestor of 7.
ERROR: 9 is not found.
ERROR: 12 and -3 are not found.
ERROR: 0 is not found.
ERROR: 99 and 99 are not found.


#include<cstdio>
#include<vector>
#include<algorithm>
using namespace std;
const int maxn=10010;
struct node{
	int key;
	node* lchild;
	node* rchild;
};
int n,m;
int pre[maxn],inOrder[maxn];

node* createTree(int preL,int preR,int inL,int inR){
	if(preL>preR){
		return NULL;
	}
	node* root=new node;
	root->key=pre[preL];
	int k;
	for(k=inL;k<=inR;k++){
		if(inOrder[k]==pre[preL]){
			break;
		}
	}
	int numLeft=k-inL;

	root->lchild=createTree(preL+1,preL+numLeft,inL,k-1);
	root->rchild=createTree(preL+numLeft+1,preR,k+1,inR);

	return root;
}

int search(node* root,int x,int an){
	if(root==NULL){
		return -1;
	}
	if(x==root->key){
		return an;
	}
	if(x<root->key){
		search(root->lchild,x,root->key);
	}else if(x>root->key){
		search(root->rchild,x,root->key);
	}
}

int main(){
	scanf("%d %d",&m,&n);
	for(int i=0;i<n;i++){
		scanf("%d",&pre[i]);
		inOrder[i]=pre[i];
	}
	sort(inOrder,inOrder+n);

	node* L=createTree(0,n-1,0,n-1);

	for(int i=0;i<m;i++){
		int u,v;
		scanf("%d %d",&u,&v);
		int an1=search(L,u,L->key);
		int an2=search(L,v,L->key);
		if(an1==-1&&an2!=-1){
			printf("ERROR: %d is not found.",u);
		}else if(an1!=-1&&an2==-1){
			printf("ERROR: %d is not found.",v);
		}else if(an1==-1&&an2==-1){
			printf("ERROR: %d and %d are not found.",u,v);
		}else if(an1!=-1&&an1==v){
			printf("%d is an ancestor of %d.",v,u);
		}else if(an2!=-1&&an2==u){
			printf("%d is an ancestor of %d.",u,v);
		}else if(an1!=-1&&an2!=-1&&an1>an2){
			printf("LCA of %d and %d is %d.",u,v,an1);
		}else if(an1!=-1&&an2!=-1&&an1<an2){
			printf("LCA of %d and %d is %d.",u,v,an2);
		}
		if(i<m-1){
			printf("\n");
		}
	}
	return 0;
}