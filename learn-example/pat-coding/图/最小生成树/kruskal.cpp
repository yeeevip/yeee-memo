#include<cstdio>
#include<algorithm>
using namespace std;
const int maxn=100;
int n,m;
struct edge{
	int u,v,cost;
}E[maxn];

bool cmp(edge a,edge b){
	return a.cost<b.cost;
}

int father[maxn];
int findF(int x){
	int a=x;
	while(x!=father[x]){
		x=father[x];
	}
	while(a!=father[a]){
		int z=a;
		a=father[a];
		father[z]=x;
	}
	return x;
}

int kruskal(int n,int m){
	int ans=0,num_edge=0;
	for(int i=1;i<=n;i++){
		father[i]=i;
	}
	sort(E,E+m,cmp);
	for(int i=0;i<m;i++){
		int faU=findF(E[i].u);
		int faV=findF(E[i].v);
		if(faU!=faV){
			father[faU]=faV;
			ans+=E[i].cost;
			num_edge++;
			if(num_edge==n-1) break;
		}
	}
	if(num_edge!=n-1) return -1;
	else return ans;
}

int main(){
	scanf("%d %d",&n,&m);
	for(int i=0;i<m;i++){
		scanf("%d %d %d",&E[i].u,&E[i].v,&E[i].cost);
	}
	printf("%d",kruskal(n,m));
	return 0;
}