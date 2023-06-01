1004 Counting Leaves (30)（30 分）

A family hierarchy is usually presented by a pedigree tree. Your job is to count those family members who have no child.
Input
Each input file contains one test case. Each case starts with a line containing 0 < N < 100, the number of nodes in a tree, and M (< N), the number of non-leaf nodes. Then M lines follow, each in the format:
ID K ID[1] ID[2] ... ID[K]
where ID is a two-digit number representing a given non-leaf node, K is the number of its children, followed by a sequence of two-digit ID's of its children. For the sake of simplicity, let us fix the root ID to be 01.
Output
For each test case, you are supposed to count those family members who have no child for every seniority level starting from the root. The numbers must be printed in a line, separated by a space, and there must be no extra space at the end of each line.
The sample case represents a tree with only 2 nodes, where 01 is the root and 02 is its only child. Hence on the root 01 level, there is 0 leaf node; and on the next level, there is 1 leaf node. Then we should output "0 1" in a line.
Sample Input
2 1
01 1 02
Sample Output
0 1

#include<cstdio>
#include<vector>
#include<algorithm>
using namespace std;
const int maxn=110;
struct node{
	int level;
	vector<int> child;
}tree[maxn];

int n,m,numLevel[maxn]={0};
int maxLe=0;
void DFS(int root,int level){
	if(root>n){
		return ;
	}
	if(tree[root].child.size()==0){
		numLevel[tree[root].level]++;
	}
	maxLe=max(maxLe,level);
	for(int i=0;i<tree[root].child.size();i++){
		int child=tree[root].child[i];
		tree[child].level=tree[root].level+1;
		DFS(tree[root].child[i],level+1);
	}

}

int main(){
	scanf("%d %d",&n,&m);
	int id,k;
	for(int i=0;i<m;i++){
		scanf("%d %d",&id,&k);
		int c;
		for(int i=0;i<k;i++){
			scanf("%d",&c);
			tree[id].child.push_back(c);
		}
	}
	tree[1].level=1;
	DFS(1,1);
	for(int i=1;i<=maxLe;i++){
		printf("%d",numLevel[i]);
		if(i<maxLe){
			printf(" ");
		}
	}
	return 0;
}