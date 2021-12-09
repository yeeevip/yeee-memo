/*1001 A+B Format (20)（20 分）

Calculate a + b and output the sum in standard format -- that is, the digits must be separated into groups of three by commas (unless there are less than four digits).

Input

Each input file contains one test case. Each case contains a pair of integers a and b where -1000000 <= a, b <= 1000000. The numbers are separated by a space.

Output

For each test case, you should output the sum of a and b in one line. The sum must be written in the standard format.

Sample Input

-1000000 9
Sample Output

-999,991*/




#include<cstdio>
#include<cstdio>
#include<cstring>
#include<cmath>
struct Number{
	char num[10];
	bool isPostion;
} Res;

void convert(int a,Number &tem){
	if(a<0){
		tem.isPostion=false;
	}else{
		tem.isPostion=true;
	}
	sprintf(tem.num,"%d",abs(a));
}

int main(){
	int a,b,c;
	scanf("%d %d",&a,&b);
	c=a+b;
	convert(c,Res);
	if(!Res.isPostion){
		printf("-");
	}
	int len=strlen(Res.num);
	for(int i=0;i<len;i++){
		printf("%c",Res.num[i]);
		if((i+1)%3==0&&i!=len-1){
			printf(",");
		}

	}
	return 0;
}