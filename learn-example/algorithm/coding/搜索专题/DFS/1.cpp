int n,k,x,maxnSumSqu=-1,A[maxn];
vector<int> temp,ans;
void DFS(int index,int noeK,int sum,int sumSqu){
	if(nowK==k&&sum==x){
		if(sumSqu>maxnSumSqu){
			maxnSumSqu=sumSqu;
			ans=temp;
		}
		return;
	}
	if(index==n||nowK>k||sum>x){
		return;
	}

	temp.push_back(A[index]);
	DFS(index+1,noK+1,sum+A[index],sumSqu+A[index]*A[index]);
	temp.pop_back();
	DFS(index+1,noWK,sum,sumSqu);
}



void DFS(int index,int sumV,int sunC){
	if(index==n)return;
	DFS(index+1,sumV,sumC);
	if(sumW+weight[index]<=V){
		if(sumC+C[index]>ans){
			ans=sumC+C[index];
		}
		DFS(index+1,sumW+weight[index],sumC+C[index]);
	}
}