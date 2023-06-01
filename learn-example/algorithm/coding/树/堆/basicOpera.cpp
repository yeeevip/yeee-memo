#include<cstdio>
#include<algorithm>
using namespace std;
const int maxn=100;
int heap[maxn],n;

void downAdjust(int low,int high){
	int i=low,j=2*i;
	while(j<=high){//存在孩子节点
		if(j+1<=high&&heap[j+1]>heap[j]){
			j=j+1;
		}
		//如果孩子中最大的权值比欲调整大
		if(heap[j]>heap[i]){
			swap(heap[j],heap[i]);
			i=j;
			j==i*2;
		}else{
			break;//孩子的权值均比欲调整节点i小,调整结束
		}
	}
}

void deleteTop(){
	heap[1]=heap[n--];
	downAdjust(1,n);
}


void createHeap(){
	for(int i=n/2;i>=1;i--){
		downAdjust(i,n);
	}
}

void upAdjust(int low,int high){
	int i=high,j=i/2;
	while(j>=low){//父亲在[low，high]范围内
		if(heap[j]<heap[i]){
			swap(heap[j],heap[i]);
			i=j;
			j=i/2;
		}else{
			break;
		}
	}
}

//堆排序
void heapSort(){
	createHeap();
	for(int i=n;i>=1;i--){
		swap(1,i);
		downAdjust(1,i-1
		);
	}
}

void insert(int x){
	heap[++n]=x;
	upAdjust(1,n);
}


int main(){
	return 0;
}