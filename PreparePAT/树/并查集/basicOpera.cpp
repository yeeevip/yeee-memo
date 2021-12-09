#include<cstdio>
using namespace std;
const int N=100;

int father[N];

void init(){
	for(int i=1;i<=N;i++){
		father[i]=i;
	}
}

int findFather(int x){
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

void Union(int a,int b){
	int faA=findFather(a);
	int faB=findFather(b);
	if(faA!=faB){
		father[faA]=faB;
	}
}

int main(){
	return 0;
}