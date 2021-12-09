/*Repair the Wall
 *
 * 题目描述

Long time ago , Kitty lived in a small village. The air was fresh and the scenery was very beautiful. The only thing that troubled her is the typhoon.
When the typhoon came, everything is terrible. It kept blowing and raining for a long time. And what made the situation worse was that all of Kitty's walls were made of wood.
One day, Kitty found that there was a crack in the wall. The shape of the crack is
a rectangle with the size of 1×L (in inch). Luckly Kitty got N blocks and a saw(锯子) from her neighbors.
The shape of the blocks were rectangle too, and the width of all blocks were 1 inch. So, with the help of saw,
 Kitty could cut down some of the blocks(of course she could use it directly without cutting) and put them in the crack, and the wall may be repaired perfectly, without any gap.
Now, Kitty knew the size of each blocks, and wanted to use as fewer as possible of the blocks to repair the wall, could you help her ?


输入
The problem contains many test cases, please process to the end of file( EOF ).
Each test case contains two lines.
In the first line, there are two integers L(0<L<1000000000) and N(0<=N<600) which
mentioned above.
In the second line, there are N positive integers. The ith integer Ai(0<Ai<1000000000 ) means that the ith block has the size of 1×Ai (in inch).

输出
For each test case , print an integer which represents the minimal number of blocks are needed.
If Kitty could not repair the wall, just print "impossible" instead.

样例输入
2 2
12 11
14 3
27 11 4
109 5
38 15 6 21 32
5 3
1 1 1

样例输出
1
1
5
impossible
 *
 *
 * */

 /*
  * 钻块从大到小排序   求补强最少钻块  每次选最大的
  *
  * 选择第一块，补进去，计算出剩余洞大小 并计算出钻块剩余尺寸  重复操作
  *
  * */

#include<cstdio>
#include<algorithm>
using namespace std;

bool cmp(int a,int b){
    return a>b;
}

int main(){
    int L,N;
    while(scanf("%d%d",&L,&N)!=EOF){
        int A[N];
        for(int i=0;i<N;i++){
            scanf("%d",&A[i]);
        }
        sort(A,A+N,cmp);
        int curB = 0;//当前钻
        bool isRepair=false;

        while(!isRepair){
            if(L<=0){
                printf("%d\n",curB);
                break;
            }
            if(curB>N-1){
                printf("imposible\n");
                break;
            }
            L-=A[curB++];

        }
   }

    return 0;
}

