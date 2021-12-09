1043 Is It a Binary Search Tree (25)（25 分）

A Binary Search Tree (BST) is recursively defined as a binary tree which has the following properties:
The left subtree of a node contains only nodes with keys less than the node's key.
The right subtree of a node contains only nodes with keys greater than or equal to the node's key.
Both the left and right subtrees must also be binary search trees.
If we swap the left and right subtrees of every node, then the resulting tree is called the Mirror Image of a BST.
Now given a sequence of integer keys, you are supposed to tell if it is the preorder traversal sequence of a BST or the mirror image of a BST.
Input Specification:
Each input file contains one test case. For each case, the first line contains a positive integer N (<=1000). Then N integer keys are given in the next line. All the numbers in a line are separated by a space.
Output Specification:
For each test case, first print in a line "YES" if the sequence is the preorder traversal sequence of a BST or the mirror image of a BST, or "NO" if not. Then if the answer is "YES", print in the next line the postorder traversal sequence of that tree. All the numbers in a line must be separated by a space, and there must be no extra space at the end of the line.
Sample Input 1:
7
8 6 5 7 10 8 11
Sample Output 1:
YES
5 7 6 8 11 10 8
Sample Input 2:
7
8 10 11 8 6 7 5
Sample Output 2:
YES
11 8 10 7 5 6 8
Sample Input 3:
7
8 6 8 5 10 9 11
Sample Output 3:
NO



#include<cstdio>
#include<vector>
using namespace std;
const int maxn=1010;
struct node{
	int data;
	node* lchild;
	node* rchild;
};


void insert(node* &root,int x){
	if(root==NULL){
		node* newN = new node;
		newN->data=x;
		newN->lchild=newN->rchild=NULL;
		root = newN;
		return;
	}else if(x<root->data){
		insert(root->lchild,x);
	}else{
		insert(root->rchild,x);
	}
}

void preOrder(node* root,vector<int> &vi){
	if(root==NULL) return;
	vi.push_back(root->data);
	preOrder(root->lchild,vi);
	preOrder(root->rchild,vi);
}
void preOrderM(node* root,vector<int> &vi){
	if(root==NULL) return;
	vi.push_back(root->data);
	preOrderM(root->rchild,vi);
	preOrderM(root->lchild,vi);
}
void postOrder(node* root,vector<int> &vi){
	if(root==NULL) return ;
	postOrder(root->lchild,vi);
	postOrder(root->rchild,vi);
	vi.push_back(root->data);
}
void postOrderM(node* root,vector<int> &vi){
	if(root==NULL)return;
	postOrderM(root->rchild,vi);
	postOrderM(root->lchild,vi);
	vi.push_back(root->data);
}
int n;
vector<int> origin,pre,preM,post,postM;
int main(){
	scanf("%d",&n);
	int data;
	node* root=NULL;
	for(int i=0;i<n;i++){
		scanf("%d",&data);
		origin.push_back(data);
		insert(root,data);
	}
	preOrder(root,pre);
	preOrderM(root,preM);
	if(origin==pre){
		printf("YES\n");
		postOrder(root,post);
		for(int i=0;i<n;i++){
			printf("%d",post[i]);
			if(i<n-1) printf(" ");
		}
	}else if(origin==preM){
		printf("YES\n");
		postOrderM(root,postM);
		for(int i=0;i<n;i++){
			printf("%d",postM[i]);
			if(i<n-1) printf(" ");
		}
	}else{
		printf("NO\n");
	}
	return 0;
}





#include<cstdio>
#include<iostream>
#include<vector>
#include<string>
#include<map>
using namespace std;

using namespace std;

const int maxn=2010;

struct node{
	int v;
	int callTime;
	node(int _v,int _callTime):v(_v),callTime(_callTime){}
};

int n,K,numPerson=0;
int weight[maxn]={0},G[maxn][maxn]={0};

bool vis[maxn]={false};
//vector<node> Adj[maxn];

void DFS(int u,int &numMember,int &totalValue,int &head){
	vis[u]=true;
	numMember++;
	if(weight[head]<weight[u]){
		head=u;
	}
//	for(int i=0;i<Adj[u].size();i++){
//		int v=Adj[u][i].v;
//		tatalValue+=Adj[u][v].callTime;
//		Adj[u][v].callTime=Adj[v][u].callTime=0;
//		if(vis[v]==false){
//			DFS(v,numMember,totalValue,head);
//		}
//	}

	for(int i=0;i<numPerson;i++){
		if(G[u][i]>0){
			totalValue+=G[u][i];
			G[u][i]=G[i][u]=0;
			if(vis[i]==false){
				DFS(i,numMember,totalValue,head);
			}
		}
	}

}

map<string,int> Gang;

map<int,string> intToString;
map<string,int> stringToInt;

void DFSTrave(){
	for(int i=0;i<numPerson;i++){
		int numMember=0,totalValue=0,head=i;
		DFS(i,numMember,totalValue,head);
		if(numMember>2&&totalValue>K){
			Gang[intToString[head]]=numMember;
		}
	}
}

int stringToId(string str){
	if(stringToInt.find(str)!=stringToInt.end()){
		return stringToInt[str];
	}else{
		stringToInt[str]=numPerson;
		intToString[numPerson]=str;
		return numPerson++;
	}
}

int main(){
	int w;
	string str1,str2;
	scanf("%d %d",&n,&K);
	for(int i=0;i<n;i++){
		cin>>str1>>str2>>w;
		int id1=stringToId(str1);
		int id2=stringToId(str2);
		weight[id1]+=w;
		weight[id2]+=w;
//		Adj[id1].push_back(node(id2,w));
//		Adj[id2].push_back(node(id1,w));
		G[id1][id2]+=w;
		G[id2][id1]+=w;
	}
	DFSTrave();
	cout<<Gang.size()<<endl;
	map<string,int>::iterator it;
	for(it=Gang.begin();it!=Gang.end();it++){
		cout<<it->first<<" "<<it->second<<endl;
	}

	return 0;
}

