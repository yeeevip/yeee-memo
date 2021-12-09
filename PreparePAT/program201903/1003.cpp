/*
As an emergency rescue team leader of a city, you are given a special map of your country.
 The map shows several scattered cities connected by some roads. Amount of rescue teams in each
 city and the length of each road between any pair of cities are marked on the map.
 When there is an emergency call to you from some other city, your job is to lead your men to the place as quickly as possible,
 and at the mean time, call up as many hands on the way as possible.

Input

        Each input file contains one test case. For each test case,
        the first line contains 4 positive integers: N (<= 500) – the number of cities (and the cities are numbered from 0 to N-1),
        M – the number of roads, C1 and C2 – the cities that you are currently in and that you must save, respectively.
        The next line contains N integers, where the i-th integer is the number of rescue teams in the i-th city.
        Then M lines follow, each describes a road with three integers c1, c2 and L,
        which are the pair of cities connected by a road and the length of that road, respectively.
        It is guaranteed that there exists at least one path from C1 to C2.

Output

        For each test case, print in one line two numbers: the number of different shortest paths between C1 and C2,
        and the maximum amount of rescue teams you can possibly gather.
All the numbers in a line must be separated by exactly one space, and there is no extra space allowed at the end of a line.

Sample Input

5 6 0 2
1 2 1 5 3
0 1 1
0 2 2
0 3 1
1 2 1
2 4 1
3 4 1

Sample Output

2 4*/
/*


#include<cstdio>
#include<vector>
#include<algorithm>
using namespace std;
const int maxn=510;
const int INF=100000000;

int N,M,s,e;

struct node{
    int v;
    int dis;
    node(int _v,int _dis):v(_v),dis(_dis){}
};
bool vis[maxn]={false};
vector<node> Adj[maxn];
int weight[maxn];
int d[maxn],w[maxn],num[maxn];

void Dijkstra(int s){
    fill(d,d+maxn,INF);
    fill(w,w+maxn,0);
    fill(num,num+maxn,0);
    d[s]=0;
    w[s]=weight[s];
    num[s]=1;

    for(int i=0;i<N;i++){
        int u=-1,MIN=INF;
        for(int j=0;j<N;j++){
            if(vis[j]==false&&d[j]<MIN){
                u=j;
                MIN=d[j];
            }
        }
        if(u==-1) return;
        vis[u]=true;
        for(int j=0;j<Adj[u].size();j++){
            node nv=Adj[u][j];
            int v=nv.v;
            if(vis[v]==false){
                if(d[u]+nv.dis<d[v]){
                    d[v]=d[u]+nv.dis;
                    w[v]=w[u]+weight[v];
                    num[v]=num[u];
                }else if(d[u]+nv.dis==d[v]){
                    if(w[u]+weight[v]>w[v]){
                        w[v]=w[u]+weight[v];
                    }
                    num[v]+=num[u];
                }
            }
        }
    }

}


int main(){
    scanf("%d %d %d %d",&N,&M,&s,&e);
    for(int i=0;i<N;i++){
        scanf("%d",&weight[i]);
    }
    int u,v,we;
    for(int i=0;i<M;i++){
        scanf("%d %d %d",&u,&v,&we);
        Adj[u].push_back(node(v,we));
        Adj[v].push_back(node(u,we));
    }
    Dijkstra(s);
    printf("%d %d",num[e],w[e]);
    return 0;
}*/
/*

#include<cstdio>
#include<algorithm>
using namespace std;
const int maxn=500;
const int INF=1000000000;
int n;
int weight[maxn];
int G[maxn][maxn];
bool vis[maxn]={false};
int w[maxn];
int dis[maxn];
int num[maxn];

void Dijkstra(int s){
    fill(dis,dis+maxn,INF);
    fill(w,w+maxn,0);
    fill(num,num+maxn,0);
    num[s]=1;
    dis[s]=0;
    w[s]=weight[s];
    for(int i=0;i<n;i++){
        int u=-1,min=INF;
        for(int j=0;j<n;j++){
            if(vis[j]==false&&dis[j]<min){
                u=j;
                min=dis[j];
            }
        }
        if(u==-1)return;
        vis[u]=true;
        for(int v=0;v<n;v++){
            if(vis[v]==false&&G[u][v]!=INF){
                if(dis[u]+G[u][v]<dis[v]){
                    dis[v]=dis[u]+G[u][v];
                    w[v]=w[u]+weight[v];
                    num[v]=num[u];
                }else if(dis[u]+G[u][v]==dis[v]){
                    num[v]+=num[u];
                    if(w[u]+weight[v]>w[v]){
                        w[v]=w[u]+weight[v];
                    }
                }
            }
        }
    }
}

int main(){
    fill(G[0],G[0]+maxn*maxn,INF);
    int m,C1,C2;
    scanf("%d %d %d %d",&n,&m,&C1,&C2);
    for(int i=0;i<n;i++){
        scanf("%d",&weight[i]);
    }
    for(int i=0;i<m;i++){
        int u,v,c;
        scanf("%d %d %d",&u,&v,&c);
        G[u][v]=c;
        G[v][u]=c;
    }
    Dijkstra(C1);
    printf("%d %d",num[C2],w[C2]);
    return 0;
}
*/

#include<cstdio>
#include<vector>
using namespace std;
const int maxn=500;
const int INF=1000000000;
int n;
int weight[maxn];
bool vis[maxn]={false};
int dis[maxn],w[maxn],num[maxn];
struct node{
    int v;
    int d;
    node(int _v,int _d):v(_v),d(_d){}
};
vector<node> Adj[maxn];
void Dijkstra(int s){
    fill(dis,dis+n,INF);
    fill(num,num+n,0);
    fill(w,w+n,0);
    dis[s]=0;
    num[s]=1;
    w[s]=weight[s];
    for(int i=0;i<n;i++){
        int u=-1,min=INF;
        for(int j=0;j<n;j++){
            if(vis[j]==false&&dis[j]<min){
                u=j;
                min=dis[j];
            }
        }
        if(u==-1)return;
        vis[u]=true;
        for(int j=0;j<Adj[u].size();j++){
            node n = Adj[u][j];
            int v=n.v;
            int d=n.d;
            if(vis[v]==false){
                if(dis[u]+d<dis[v]){
                    dis[v]=d+dis[u];
                    w[v]=weight[v]+w[u];
                    num[v]=num[u];
                }else if(dis[u]+d==dis[v]){
                    num[v]+=num[u];
                    if(w[u]+weight[v]>w[v]){
                       w[v]=weight[v]+w[u];
                    }
                }
            }
        }
    }
}
int main(){
    int m,C1,C2;
    scanf("%d %d %d %d",&n,&m,&C1,&C2);
    for(int i=0;i<n;i++){
        scanf("%d",&weight[i]);
    }
    for(int i=0;i<m;i++){
        int u,v,d;
        scanf("%d %d %d",&u,&v,&d);
        Adj[u].push_back(node(v,d));
        Adj[v].push_back(node(u,d));
    }
    Dijkstra(C1);
    printf("%d %d",num[C2],w[C2]);
    return 0;
}