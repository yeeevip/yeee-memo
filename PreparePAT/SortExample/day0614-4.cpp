/*
题目描述
Excel可以对一组纪录按任意指定列排序。现请你编写程序实现类似功能。
对每个测试用例，首先输出1行“Case i:”，其中 i 是测试用例的编号（从1开始）。随后在 N 行中输出按要求排序后的结果，
 即：当 C=1 时，按学号递增排序；当 C=2时，按姓名的非递减字典序排序；当 C=3 时，按成绩的非递减排序。当若干学生具有相同姓名或者相同成绩时，则按他们的学号递增排序。
输入
测试输入包含若干测试用例。每个测试用例的第1行包含两个整数 N (N<=100000) 和 C，其中 N 是纪录的条数，C 是指定排序的列号。
 以下有N行，每行包含一条学生纪录。每条学生纪录由学号（6位数字，同组测试中没有重复的学号）、姓名（不超过8位且不包含空格的字符串）、
 成绩（闭区间[0, 100]内的整数）组成，每个项目间用1个空格隔开。当读到 N=0 时，全部输入结束，相应的结果不要输出。

输出
对每个测试用例，首先输出1行“Case i:”，其中 i 是测试用例的编号（从1开始）。随后在 N 行中输出按要求排序后的结果，
 即：当 C=1 时，按学号递增排序；当 C=2时，按姓名的非递减字典序排序；当 C=3 时，按成绩的非递减排序。当若干学生具有相同姓名或者相同成绩时，则按他们的学号递增排序。

样例输入
4 1
000001 Zhao 75
000004 Qian 88
000003 Li 64
000002 Sun 90
4 2
000005 Zhao 95
000011 Zhao 75
000007 Qian 68
000006 Sun 85
4 3
000002 Qian 88
000015 Li 95
000012 Zhao 70
000009 Sun 95
0 3
样例输出
Case 1:
000001 Zhao 75
000002 Sun 90
000003 Li 64
000004 Qian 88
Case 2:
000007 Qian 68
000006 Sun 85
000005 Zhao 95
000011 Zhao 75
Case 3:
000012 Zhao 70
000002 Qian 88
000009 Sun 95
000015 Li 95

*/

#include <cstdio>
#include<cstring>
#include<algorithm>
using namespace std;
struct Student{
    char id[7];
    char name[8];
    int score;
}stu[100010];
bool cmpId(Student s1,Student s2){
    return strcmp(s1.id,s2.id)<0;
}
bool cmpName(Student s1,Student s2){
    return strcmp(s1.name,s2.name)<0;
}
bool cmpScore(Student s1,Student s2){
    if(s1.score!=s2.score) return s1.score<s2.score;
    else return strcmp(s1.id,s2.id)<0;
}
int main(){
    int n,col;
    while(scanf("%d%d",&n,&col)){
        if(n==0) break;
        for(int i=0;i<n;i++){
            scanf("%s %s %d",stu[i].id,stu[i].name,&stu[i].score);
        }
        if(col==1){
            sort(stu,stu+n,cmpId);
        }else if(col==2){
            sort(stu,stu+n,cmpName);
        }else{
            sort(stu,stu+n,cmpScore);
        }
        for(int i=0;i<n;i++){
            printf("%s %s %d",stu[i].id,stu[i].name,stu[i].score);
            if(i<n-1) printf("\n");
        }
    }
    return 0;
}




