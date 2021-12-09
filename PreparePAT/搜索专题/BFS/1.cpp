题目描述：
给出一个m*n的矩阵，矩阵中的元素为0或1.称位置（x,y）与其上下左右四个位置是相邻的。如果矩阵中有若干个1相邻，则称这些1构成了一个块。求给定矩阵中的块数。
输入：
0 1 1 1 0 0 1
1 0 1 0 0 0 0
0 0 0 0 1 0 1
0 0 0 1 1 1 0
1 1 1 0 1 1 1
1 1 1 1 0 0 1

输出：4

void DFS(int x,int y){
    inq[x][y]=true;
    for(int i=0;i<4;i++){
        int newX=x+X[i];
        int newY=y+Y[i];
        if(judge(newX,newY)){
            DFS(newX,newY);
        }
    }
}

#include<cstdio>
#include<queue>
using namespace std;
const int maxn = 100;
struct node{
	int x,y;
}Node;

int n,m;//矩阵大小为n*m
int matrix[maxn][maxn];
bool inq[maxn][maxn]={false};
int X[4]={0,0,1,-1};
int Y[4]={1,-1,0,0};//增量数组

bool judge(int x,int y){
	//越界返回
	if(x>=n||x<0||y>=m||y<0)return false;
	if(inq[n][m]==true||matrix[n][m]==0) return false;
	return true;
}

void BFS(int x,int y){
	queue<node> Q;
	Node.x=x,Node.y=y;
	Q.push(Node);
	inq[x][y]=true;
	while(!Q.empty()){
		node top=Q.front();
		Q.pop();
		for(int i=0;i<4;i++){//循环4此，得到四个相邻位置
			int newX=top.x+X[i];
			int newY=top.y+Y[i];
			if(judge(newX,newY)){
				Node.x=newX,Node.y=newY;
				Q.push(Node);
				inq[newX][newY]=true;
			}
		}
	}
}

int main(){
	scanf("%d %d",&n,&m);
	for(int i=0;i<n;i++){
		for(int j=0;j<m;j++){
			scanf("%d",&matrix[i][j]);
		}
	}

	int ans=0;//存放块数
	for(int x=0;x<n;x++){
		for(int y=0;y<m;y++){
			if(matrix[x][y]==1&&inq[x][y]==false){
				ans++;
				BFS(x,y);
			}
		}
	}
	printf("%d\n",ans);
	return 0;
}


给你一个 n 行 m 列的二维迷宫。'S'表示起点，'T' 表示终点，'#' 表示墙壁，'.' 表示平地。你需要从 'S' 出发走到 'T'，每次只能上下左右走动，并且不能走出地图的范围以及不能走到墙壁上。请你计算出走到终点需要走的最少步数。

输入格式

第一行输入 nnn, mmm 表示迷宫大小。(1≤n,m≤100)(1 \leq n,m \leq 100)

接下来输入 nnn 行字符串表示迷宫，每个字符串长度为 mm。（地图保证有且仅有一个终点，一个起始点）

输出格式

输出走到终点的最少步数，如果不能走到终点输出 -1−1，占一行。

#include<cstdio>
#include<cstring>
#include<queue>
using namespace std;
const int maxn =100;
struct node{
	int x,y;
	int step;
}S,T,Node;

int n,m;
char maze[maxn][maxn];

bool inq[maxn][maxn];
int X[4]={0,0,1,-1};
int Y[4]={1,-1,0,0};

bool test(int x,int y){
	if(x>=n||x<0||y>=m||y<0) return false;//边界
	if(maze[x][y]=='*') return false;//墙壁
	if(inq[x][y]==true) return false;
	return true;
}

int BFS(){
	queue<node> q;
	q.push(S);
	while(!q.empty()){
		node top=q.front();
		q.pop();
		if(top.x==S.x&&top.y==S.y){
			return top.step;
		}
		for(int i=0;i<4;i++){
			int newX=top.x+X[i];
			int newY=top.y+Y[i];
			if(test(newX,newY)){
				Node.x=newX,Node.y=newY;
				Node.step=top.step+1;
				q.push(Node);
				inq[newX][newY]=true;
			}
		}
	}
	return -1;
}

int main(){
	scanf("%d %d",&n,&m);
	for(int i=0;i<n;i++){
		getchar();//过滤掉每行后面的换行符
		for(int j=0;j<m;j++){
			maze[i][j]=getchar();
		}
		maze[i][m+1]='\0';
	}
	scanf("%d %d %d %d",&S.x,&S.y,&T.x,&T.y);
	S.step=0;
	printf("%d\n",BFS());
	return 0;
}


#include<cstdio>
#include<queue>
using namespace std;
struct node{
	int data;
}a[10];

int main(){
	queue<node> q;
	for(int i=1;i<=3;i++){
		a[i].data=i;
		q.push(a[i]);
	}
	q.front().data=100;
	printf("%d %d %d\n",a[1].data,a[2].data,a[3].data);

	a[1].data=200;

	printf("%d\n",q.front().data);

	return 0;
}

#include<cstdio>
#include<queue>
using namespace std;
struct node{
	int data;
}a[10];

int main(){
	queue<int> q;
	for(int i=1;i<=3;i++){
		a[i].data=i;
		q.push(i);
	}
	a[q.front()].data=100;
	printf("%d\n",a[1].data);
	return 0;
}