#include<cstdio>
#include<algorithm>
using namespace std;
const int maxn=100010;
struct Node{
	int address;
	int data;
	int next;
	bool flag;
}node[maxn];
bool cmp(Node a,Node b){
	if(a.flag==false||b.flag==false) return a.flag>b.flag;
	else return a.data<b.data;
}

int main(){
	for(int i;i<maxn;i++){
		node[i].flag=false;
	}
	int n,sadd;
	scanf("%d%d",&n,&sadd);
	int address;
	for(int i=0;i<n;i++){
		scanf("%d",&address);
		scanf("%d %d",&node[address].data,&node[address].next);
		node[address].address=address;
	}

	int p=sadd,count=0;
	while(p!=-1){
		count++;
		node[p].flag=true;
		p=node[p].next;
	}
	if(count==0){
		printf("0 -1\n");
	}else{
		sort(node,node+maxn,cmp);
		printf("%d %d\n",count,node[0].address);
		for(int i=0;i<count;i++){
			if(i<count-1){
				printf("%05d %d %05d\n",node[i].address,node[i].data,node[i+1].address);
			}else{
				printf("%05d %d -1",node[i].address,node[i].data);
			}
		}
	}

	printf("%d %d");
	for(int i=0;i<n;i++){

	}
	return 0;
}