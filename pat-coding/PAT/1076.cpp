1076 Forwards on Weibo (30)（30 分）

Weibo is known as the Chinese version of Twitter. One user on Weibo may have many followers, and may follow many other users as well. Hence a social network is formed with followers relations. When a user makes a post on Weibo, all his/her followers can view and forward his/her post, which can then be forwarded again by their followers. Now given a social network, you are supposed to calculate the maximum potential amount of forwards for any specific user, assuming that only L levels of indirect followers are counted.
Input Specification:
Each input file contains one test case. For each case, the first line contains 2 positive integers: N (<=1000), the number of users; and L (<=6), the number of levels of indirect followers that are counted. Hence it is assumed that all the users are numbered from 1 to N. Then N lines follow, each in the format:
M[i] user_list[i]
where M[i] (<=100) is the total number of people that user[i] follows; and user_list[i] is a list of the M[i] users that are followed by user[i]. It is guaranteed that no one can follow oneself. All the numbers are separated by a space.
Then finally a positive K is given, followed by K UserID's for query.
Output Specification:
For each UserID, you are supposed to print in one line the maximum potential amount of forwards this user can triger, assuming that everyone who can view the initial post will forward it once, and that only L levels of indirect followers are counted.
Sample Input:
7 3
3 2 3 4
0
2 5 6
2 3 1
2 3 4
1 4
1 5
2 2 6
Sample Output:
4
5


#include<cstdio>
#include<queue>
#include<vector>
#include<cstring>
using namespace std;
const int maxn = 1010;
struct node{
	int v;
	int l;
};
bool inq[maxn]={false};
vector<node> Adj[maxn];
int N,L;

int BFS(int s){
	int ans=0;
	queue<node> q;
	node start;
	start.v=s;
	start.l=0;
	q.push(start);
	inq[start.v]=true;
	while(!q.empty()){
		node t=q.front();
		q.pop();
		int u=t.v;
		for(int i=0;i<Adj[u].size();i++){
			node next=Adj[u][i];
			next.l=t.l+1;
			if(inq[next.v]==false&&next.l<=L){
				q.push(next);
				ans++;
				inq[next.v]=true;
			}
		}
	}
	return ans;
}

void Trave(int u){
	memset(inq,false,sizeof(inq));
	int count = BFS(u);
	printf("%d\n",count);
}

int main(){
	int m,follow;
	scanf("%d %d",&N,&L);
	for(int i=1;i<=N;i++){
		scanf("%d",&m);
		for(int j=0;j<m;j++){
			scanf("%d",&follow);
			node s;
			s.v=i;
			Adj[follow].push_back(s);
		}
	}
	int k,u;
	scanf("%d",&k);
	for(int i=0;i<k;i++){
		scanf("%d",&u);
		Trave(u);
	}
	return 0;
}
