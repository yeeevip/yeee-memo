/*
题目描述

        N只小白鼠(1 <= N <= 100)，每只鼠头上戴着一顶有颜色的帽子。现在称出每只白鼠的重量，
        要求按照白鼠重量从大到小的顺序输出它们头上帽子的颜色。帽子的颜色用“red”，“blue”等字符串来表示。不同的小白鼠可以戴相同颜色的帽子。白鼠的重量用整数表示。


输入

多案例输入，每个案例的输入第一行为一个整数N，表示小白鼠的数目。
下面有N行，每行是一只白鼠的信息。第一个为不大于100的正整数，表示白鼠的重量，；第二个为字符串，表示白鼠的帽子颜色，字符串长度不超过10个字符。
注意：白鼠的重量各不相同。

输出

 每个案例按照白鼠的重量从大到小的顺序输出白鼠的帽子颜色。


样例输入
1
79 omi
9
46 lcg
92 cru
37 ceq
54 vhr
17 wus
27 tnv
13 kyr
95 wld
34 qox
 样例输出
omi
wld
cru
vhr
lcg
ceq
qox
tnv
wus
kyr

        */

#include<cstdio>
#include<algorithm>
using namespace std;
const int maxn=101;
struct Rat{
    int weight;
    char hat[8];
}rat[maxn];
bool cmp(Rat a,Rat b){
    return a.weight>b.weight;
}
int main(){
    int n,i;
    while (scanf("%d",&n)!=EOF){
        i=0;
       while(n--){
           scanf("%d %s",&rat[i].weight,rat[i].hat);
           i++;
       }
       sort(rat,rat+i,cmp);
       for(int j=0;j<i;j++){
           printf("%s",rat[j].hat);
           if(j<i-1) printf("\n");
       }
    }
    return 0;
}