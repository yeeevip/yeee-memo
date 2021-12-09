/*1002 A+B for Polynomials (25)（25 分）

This time, you are supposed to find A+B where A and B are two polynomials.

Input

Each input file contains one test case. Each case occupies 2 lines, and each line contains the information of a polynomial: K N1 a~N1~ N2 a~N2~ ... NK a~NK~, where K is the number of nonzero terms in the polynomial, Ni and a~Ni~ (i=1, 2, ..., K) are the exponents and coefficients, respectively. It is given that 1 <= K <= 10，0 <= NK < ... < N2 < N1 <=1000.

Output

For each test case you should output the sum of A and B in one line, with the same format as the input. Notice that there must be NO extra space at the end of each line. Please be accurate to 1 decimal place.

Sample Input

2 1 2.4 0 3.2
2 2 1.5 1 0.5
Sample Output

3 2 1.5 1 2.9 0 3.2*/

#include<cstdio>
#include<cstring>
const int maxn=1010;
double p1[maxn];
double p2[maxn];
bool isZero[maxn];
int count=0;
void rInput(double p[]){
	int n;
	scanf("%d",&n);
		int t;
		double num;
		for(int j=0;j<n;j++){
			scanf("%d%lf",&t,&num);
			if(isZero[t]==true){
				count++;
				isZero[t]=false;
			}
			p[t]=num;
		}
}
int main(){
	memset(isZero,true,sizeof(isZero));
	rInput(p1);
	rInput(p2);
	printf("%d ",count);
	for(int i=maxn-1;i>=0;i--){
		if(isZero[i]==false){
			printf("%d %.1lf",i,p1[i]+p2[i]);
			if(count!=1){
				printf(" ");
				count--;
			}
		}

	}
	return 0;
}