1005 Spell It Right (20)（20 分）

Given a non-negative integer N, your task is to compute the sum of all the digits of N, and output every digit of the sum in English.
Input Specification:
Each input file contains one test case. Each case occupies one line which contains an N (<= 10^100^).
Output Specification:
For each test case, output in one line the digits of the sum in English words. There must be one space between two consecutive words, but no extra space at the end of a line.
Sample Input:
12345
Sample Output:
one five

#include<cstdio>
#include<cstdlib>
#include<iostream>
#include<string>
#include<vector>
using namespace std;
struct bign{
	int d[100000];
	int len;
};
vector<int> w;
int main(){
	string n;
	string word[10]={"","one","two","three","four","five","six","seven","eight","nine"};
	cin>>n;
	int sum=0;
	for(int i=0;i<n.size();i++){
		sum+=(n[i]-48);
	}
	while(sum>0){
		w.push_back(sum%10);
		sum/=10;
	}
	for(int i=w.size()-1;i>=0;i--){
		cout<<word[w[i]];
		if(i>0){
			printf(" ");
		}
	}
	return 0;
}