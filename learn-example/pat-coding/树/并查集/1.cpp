#include<cstdio>
const int maxn=110;
int father[maxn];

void init(int n){
	for(int i=1;i<=n;i++){
		father[i]=i;
	}
}

int findFather(int x){
	int  a=x;
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
void Union(int a,int b){
	int faA=findFather(a);
	int faB=findFather(b);
	if(faA!=faB){
		father[faA]=faB;
	}
}

int main(){
	int n,m,a,b;
	scanf("%d %d",&n,&m);
	init(n);
	for(int i=0;i<m;i++){
		scanf("%d %d",&a,&b);
		Union(a,b);
	}
	int c=0;
	for(int i=1;i<=n;i++){
		if(father[i]==i){
			c++;
		}
	}
	printf("%d\n",c);

	return 0;
}