/*
题目描述
        今天的上机考试虽然有实时的Ranklist，但上面的排名只是根据完成的题数排序，没有考虑每题的分值，
        所以并不是最后的排名。给定录取分数线，请你写程序找出最后通过分数线的考生，并将他们的成绩按降序打印。

输入
        测试输入包含若干场考试的信息。每场考试信息的第1行给出考生人数N ( 0 < N < 1000 )、考题数M ( 0 < M < = 10 )、分数线（正整数）G；
        第2行排序给出第1题至第M题的正整数分值；以下N行，每行给出一名考生的准考证号（长度不超过20的字符串）、该生解决的题目总数m、以及这m道题的题号（题目号由1到M）。
当读入的考生人数为0时，输入结束，该场考试不予处理。

输出
        对每场考试，首先在第1行输出不低于分数线的考生人数n，随后n行按分数从高到低输出上线考生的考号与分数，其间用1空格分隔。若有多名考生分数相同，则按他们考号的升序输出。

样例输入
3 5 32
17 10 12 9 15
CS22003 5 1 2 3 4 5
CS22004 3 5 1 3
CS22002 2 1 5
0
样例输出
3
CS22003 63
CS22004 44
CS22002 32
提示


        这题比较简单，计算好每个人的分数后按题目要求排序即可。*/

#include<cstdio>
#include <iostream>
#include<algorithm>
using namespace std;
struct Student{
    char id[20];
    int score=0, scount;
}stu[1010];
bool cmp(Student a,Student b){
    return a.score>b.score;
}
int main(){
    int n,m,g;
    //while(cin>>n>>m>>g){
    while(scanf("%d%d%d",&n,&m,&g)!=EOF){
        if(n==0) break;
        int score[10],tempS=0,k=0;
        for(int i=1;i<=m;i++){
            scanf("%d",&score[i]);
        }
        for(int i =0;i<n;i++){
            scanf("%s%d",stu[i].id,&stu[i].scount);
            for(int j=0;j<stu[i].scount;j++){
                scanf("%d",&tempS);
                stu[i].score=stu[i].score+score[tempS];
            }
            if(stu[i].score>=g) k++;
        }
        sort(stu,stu+n,cmp);
        printf("%d\n",k);
       for(int i=0;i<k;i++){
           printf("%s %d\n",stu[i].id,stu[i].score);
       }
    }
    return 0;
}