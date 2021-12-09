1006 Sign In and Sign Out (25)（25 分）

At the beginning of every day, the first person who signs in the computer room will unlock the door, and the last one who signs out will lock the door. Given the records of signing in's and out's, you are supposed to find the ones who have unlocked and locked the door on that day.
Input Specification:
Each input file contains one test case. Each case contains the records for one day. The case starts with a positive integer M, which is the total number of records, followed by M lines, each in the format:
ID_number Sign_in_time Sign_out_time
where times are given in the format HH:MM:SS, and ID number is a string with no more than 15 characters.
Output Specification:
For each test case, output in one line the ID numbers of the persons who have unlocked and locked the door on that day. The two ID numbers must be separated by one space.
Note: It is guaranteed that the records are consistent. That is, the sign in time must be earlier than the sign out time for each person, and there are no two persons sign in or out at the same moment.
Sample Input:
3
CS301111 15:30:28 17:00:10
SC3021234 08:00:00 11:25:25
CS301133 21:45:00 21:58:40
Sample Output:
SC3021234 CS301133


#include<cstdio>
#include<algorithm>
using namespace std;
const int maxn=30;
struct record{
	char ID[16];
	int h1,m1,s1,h2,m2,s2;
} Rec[maxn];
bool cmp1(record a,record b){
	if(a.h1!=b.h1){
		return a.h1<b.h1;
	}else if(a.m1!=b.m1){
		return a.m1<b.m1;
	}else {
		return a.s1<b.s1;
	}
}
bool cmp2(record a,record b){
	if(a.h2!=b.h2){
		return a.h2>b.h2;
	}else if(a.m2!=b.m2){
		return a.m2>b.m2;
	}else{
		return a.s2>b.s2;
	}
}
int main(){
	int n;
	scanf("%d",&n);
	for(int i=0;i<n;i++){
		scanf("%s %d:%d:%d %d:%d:%d",Rec[i].ID,&Rec[i].h1,&Rec[i].m1,&Rec[i].s1,&Rec[i].h2,&Rec[i].m2,&Rec[i].s2);
	}
	sort(Rec,Rec+n,cmp1);
	printf("%s ",Rec[0].ID);
	sort(Rec,Rec+n,cmp2);
	printf("%s",Rec[0].ID);

	return 0;
}