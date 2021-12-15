/*Input Specification:
Each input file contains one test case. For each case, the first line contains a positive number N (<=100),
the number of test locations. Then N ranklists follow, each starts with a line containing a positive integer K (<=300),
the number of testees, and then K lines containing the registration number (a 13-digit number) and the total score of each testee.
All the numbers in a line are separated by a space.

Output Specification:
For each test case, first print in one line the total number of testees. Then print the final ranklist in the following format:
registration_number final_rank location_number local_rank
The locations are numbered from 1 to N. The output must be sorted in nondecreasing order of the final ranks.
 The testees with the same score must have the same rank, and the output must be sorted in nondecreasing order of their registration numbers.

 Sample Input:
 2
 5
 1234567890001 95
 1234567890005 100
 1234567890003 95
 1234567890002 77
 1234567890004 85
 4
 1234567890013 65
 1234567890011 25
 1234567890014 100
 1234567890012 85
 Sample Output:
 9
 1234567890005 1 1 1
 1234567890014 1 2 1
 1234567890001 3 1 2
 1234567890003 3 1 2
 1234567890004 5 1 4
 1234567890012 5 2 2
 1234567890002 7 1 5
 1234567890013 8 2 3
 1234567890011 9 2 4*/


#include<cstdio>
#include<cstring>
#include<algorithm>
using namespace std;
struct Student{
    char registration_num[13];
    int score;
    int location;
    int loc_rank;

}stu[30010];

bool cmp(Student a,Student b){
    if(a.score!=b.score){
        return a.score>b.score;
    }else{
        return strcmp(a.registration_num,b.registration_num)<0;
    }
}

int main(){
    int num,n,m;
    scanf("%d",&n);
    for(int i=1;i<=n;i++){
        scanf("%d",&m);
        for(int j=0;j<m;j++){
            scanf("%s %d",stu[num].registration_num,&stu[num].score);
            stu[num].location=i;
            num++;
        }
        sort(stu+num-m,stu+num,cmp);
        stu[num-m].loc_rank=1;
        for(int i=num-m+1;i<num;i++){
            if(stu[i].score==stu[i-1].score){
                stu[i].loc_rank=stu[i-1].loc_rank;
            }else{
                stu[i].loc_rank=i-(num-m)+1;
            }
        }
    }

    sort(stu,stu+num,cmp);
    int r = 1;
    for(int i=0;i<num;i++){
        printf("%s ",stu[i].registration_num);
        if(i!=0&&stu[i].score!=stu[i-1].score){
            r=i+1;
        }
        printf("%d %d %d\n",r,stu[i].location,stu[i].loc_rank);
    }

    return 0;

}


