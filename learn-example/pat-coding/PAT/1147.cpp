In computer science, a heap is a specialized tree-based data structure that satisfies the heap property: if P is a parent node of C, then the key (the value) of P is either greater than or equal to (in a max heap) or less than or equal to (in a min heap) the key of C. A common implementation of a heap is the binary heap, in which the tree is a complete binary tree. (Quoted from Wikipedia at https://en.wikipedia.org/wiki/Heap_(data_structure))

Your job is to tell if a given complete binary tree is a heap.

Input Specification:

Each input file contains one test case. For each case, the first line gives two positive integers: M (<= 100), the number of trees to be tested; and N (1 < N <= 1000), the number of keys in each tree, respectively. Then M lines follow, each contains N distinct integer keys (all in the range of int), which gives the level order traversal sequence of a complete binary tree.

Output Specification:

For each given tree, print in a line "Max Heap" if it is a max heap, or "Min Heap" for a min heap, or "Not Heap" if it is not a heap at all. Then in the next line print the tree's postorder traversal sequence. All the numbers are separated by a space, and there must no extra space at the beginning or the end of the line.

Sample Input:
3 8
98 72 86 60 65 12 23 50
8 38 25 58 52 82 70 60
10 28 15 12 34 9 8 56
Sample Output:
Max Heap
50 60 65 72 12 23 86 98
Min Heap
60 58 52 38 82 70 25 8
Not Heap
56 12 34 28 9 8 15 10


#include<cstdio>
#include<vector>
using namespace std;
const int maxn=1010;

vector<int> Heap[maxn];
int n;

bool MaxHeap(vector<int> heap,int low,int high){
	int i=low,j=2*i;
	while(j<=high){
		if(j+1<=high&&heap[j]>heap[j-1]){
			j=j+1;
		}
		if(heap[i-1]<heap[j-1]){
			return false;
		}else{
			i=j;
			j=2*i;
		}
	}
	return true;
}
bool MinHeap(vector<int> heap,int low,int high){
	int i=low,j=2*i;
	while(j<=high){
		if(j+1<=high&&heap[j]<heap[j-1]){
			j=j+1;
		}
		if(heap[i-1]>heap[j-1]){
			return false;
		}else{
			i=j;
			j=2*i;
		}
	}
	return true;
}

bool isMaxHeap(vector<int> heap){
	bool flag=false;
	for(int i=n/2;i>=1;i--){
		flag = MaxHeap(heap,i,n);
		if(!flag) break;
	}
	return flag;
}
bool isMinHeap(vector<int> heap){
	bool flag=false;
	for(int i=n/2;i>=1;i--){
		flag = MinHeap(heap,i,n);
		if(!flag) break;
	}
	return flag;
}

int cc=0;
void postOrder(int root,vector<int> heap){
	if(root>n){
		return ;
	}
	postOrder(root*2,heap);
	postOrder(root*2+1,heap);
	printf("%d",heap[root-1]);
	cc++;
	if(cc<n){
		printf(" ");
	}
}

int main(){
	int m;
	scanf("%d %d",&m,&n);
	for(int i=0;i<m;i++){
		for(int j=1;j<=n;j++){
			int k;
			scanf("%d",&k);
			Heap[i].push_back(k);
		}
	}
	for(int i=0;i<m;i++){
		if(isMaxHeap(Heap[i])){
			printf("Max Heap\n");
		}else if(isMinHeap(Heap[i])){
			printf("Min Heap\n");
		}else{
			printf("Not Heap\n");
		}
		postOrder(1,Heap[i]);
		cc=0;
		if(i<m-1){
			printf("\n");
		}
	}
	return 0;
}

#include<cstdio>
#include<vector>
using namespace std;
const int maxn=1010;

int heap[maxn];
int n;

bool MaxHeap(int low,int high){
	int i=low,j=2*i;
	while(j<=high){
		if(j+1<=high&&heap[j+1]>heap[j]){
			j=j+1;
		}
		if(heap[i]<heap[j]){
			return false;
		}else{
			i=j;
			j=2*i;
		}
	}
	return true;
}
bool MinHeap(int low,int high){
	int i=low,j=2*i;
	while(j<=high){
		if(j+1<=high&&heap[j+1]<heap[j]){
			j=j+1;
		}
		if(heap[i]>heap[j]){
			return false;
		}else{
			i=j;
			j=2*i;
		}
	}
	return true;
}

bool isMaxHeap(){
	bool flag=false;
	for(int i=n/2;i>=1;i--){
		flag = MaxHeap(i,n);
		if(!flag) break;
	}
	return flag;
}
bool isMinHeap(){
	bool flag=false;
	for(int i=n/2;i>=1;i--){
		flag = MinHeap(i,n);
		if(!flag) break;
	}
	return flag;
}

int cc=0;
void postOrder(int root){
	if(root>n){
		return ;
	}
	postOrder(root*2);
	postOrder(root*2+1);
	printf("%d",heap[root]);
	cc++;
	if(cc<n){
		printf(" ");
	}
}

int main(){
	int m;
	scanf("%d %d",&m,&n);
	for(int i=0;i<m;i++){
		for(int j=1;j<=n;j++){
			int k;
			scanf("%d",&heap[j]);
		}
		if(isMaxHeap()){
			printf("Max Heap\n");
		}else if(isMinHeap()){
			printf("Min Heap\n");
		}else{
			printf("Not Heap\n");
		}
		postOrder(1);
		cc=0;
		if(i<m-1){
			printf("\n");
		}
	}
	return 0;
}
