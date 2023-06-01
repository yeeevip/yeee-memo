/*
 *
 *题目描述
暑假到了，小明终于可以开心的看电视了。但是小明喜欢的节目太多了，他希望尽量多的看到完整的节目。
现在他把他喜欢的电视节目的转播时间表给你，你能帮他合理安排吗？
输入
输入包含多组测试数据。每组输入的第一行是一个整数n（n<=100），表示小明喜欢的节目的总数。
接下来n行，每行输入两个整数si和ei（1<=i<=n），表示第i个节目的开始和结束时间，为了简化问题，每个时间都用一个正整数表示。
当n=0时，输入结束。
输出
对于每组输入，输出能完整看到的电视节目的个数。
样例输入
12
1 3
3 4
0 7
3 8
15 19
15 20
10 15
8 18
6 12
5 10
4 14
2 9
0
样例输出
5
 *
 *
 * */


 /*
  * 分析：首先进行结构体排序，这道题的关键就在于结构体排序时所选用的排列方法，如果以电视节目的开始时间排序的话，
  * 那么可能出现电视节目放的早结束的晚这种情况，这显然不是最优解，所以我们对电视节目的结束时间进行排序，这样向下遍历，
  * 因为是按结束顺序排列的，向下遍历的时候只会提前到达目标位置。这样的结果就是最优的解。


  * */


#include<cstdio>
#include<algorithm>
 using namespace std;
 const int maxn=30;
 struct Channel{
     int b;
     int e;
 }channel[maxn];

 bool cmp(Channel a,Channel b){
     return a.e<b.e;
 }

 int main(){
     int n,count=1;
     scanf("%d",&n);
     for(int i=0;i<n;i++){
         scanf("%d%d",&channel[i].b,&channel[i].e);
     }
     sort(channel,channel+n,cmp);

     int record = channel[0].e;
     for(int j = 0;j<n;j++){
         if(record<=channel[j].b){
             count++;
             record=channel[j].e;
         }
     }
     printf("%d",count);
     return 0;
 }