#include<cstdio>
#include<queue>
using namespace std;

//代表小顶堆的优先队列
priority_queue<long long,vector<long long>,greater<long long> > q;

int main(){
	int n;
	long long temp,x,y,ans=0;
	scanf("%d",&n);
	for(int i=0;i<n;i++){
		scanf("%lld",&temp);
		q.push(temp);
	}
	while(q.size()>1){//只要优先队列中至少有两个元素
		x=q.top();
		q.pop();
		y=q.top();
		q.pop();
		q.push(x+y);
		ans+=x+y;
	}
	printf("%lld\n",ans);
	return 0;
}