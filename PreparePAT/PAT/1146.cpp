1146 Topological Order（25 分）

This is a problem given in the Graduate Entrance Exam in 2018: Which of the following is NOT a topological order obtained from the given directed graph? Now you are supposed to write a program to test each of the options.
gre.jpg
Input Specification:

Each input file contains one test case. For each case, the first line gives two positive integers N (≤ 1,000), the number of vertices in the graph, and M (≤ 10,000), the number of directed edges. Then M lines follow, each gives the start and the end vertices of an edge. The vertices are numbered from 1 to N. After the graph, there is another positive integer K (≤ 100). Then K lines of query follow, each gives a permutation of all the vertices. All the numbers in a line are separated by a space.
Output Specification:

Print in a line all the indices of queries which correspond to "NOT a topological order". The indices start from zero. All the numbers are separated by a space, and there must no extra space at the beginning or the end of the line. It is graranteed that there is at least one answer.
Sample Input:

6 8
1 2
1 3
5 2
5 4
2 3
2 6
3 4
6 4
5
1 5 2 3 6 4
5 1 2 6 3 4
5 1 2 3 6 4
5 2 1 6 3 4
1 2 3 4 5 6
Sample Output:

3 4

#include<cstdio>
#include<vector>
#include<queue>
#include<cstring>
using namespace std;
const int maxn=1010;
vector<int> Adj[maxn];
vector<int> order[110];
int inDegree[maxn],inDegreeCop[maxn];

int n,m;

bool topological(vector<int> v){
	for(int i=0;i<v.size();i++){
		int u=v[i];
		if(inDegree[u]==0){
			for(int j=0;j<Adj[u].size();j++){
				int v=Adj[u][j];
				inDegree[v]--;
			}
		}else{
			return false;
		}
	}
	return true;
}

int main(){
	memset(inDegreeCop,0,sizeof(inDegreeCop));
	scanf("%d %d",&n,&m);
	int u,v;
	for(int i=0;i<m;i++){
		scanf("%d %d",&u,&v);
		Adj[u].push_back(v);
		inDegreeCop[v]++;
		inDegree[v]=inDegreeCop[v];
	}
	int k;
	scanf("%d",&k);
	for(int i=0;i<k;i++){
		for(int j=1;j<=n;j++){
			scanf("%d",&v);
			order[i].push_back(v);
		}
	}
	vector<int> pt;
	for(int i=0;i<k;i++){
		if(!topological(order[i])){
			pt.push_back(i);
		}
		for(int i=1;i<=n;i++){
			inDegree[i]=inDegreeCop[i];
		}
	}
	for(int i=0;i<pt.size();i++){
		printf("%d",pt[i]);
		if(i<pt.size()-1){
			printf(" ");
		}
	}
	return 0;
}