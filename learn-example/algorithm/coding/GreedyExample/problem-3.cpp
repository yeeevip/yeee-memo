/*\
 * To Fill or Not to Fill
 *
 * 题目描述
With highways available, driving a car from Hangzhou to any other city is easy. But since the tank capacity of a car is limited,
 we have to find gas stations on the way from time to time. Different gas station may give different price. You are asked to carefully design the cheapest route to go.

输入
Each input file contains one test case. For each case, the first line contains 4 positive numbers: Cmax (<= 100),
 the maximum capacity of the tank; D (<=30000), the distance between Hangzhou and the destination city; Davg (<=20),
 the average distance per unit gas that the car can run; and N (<= 500), the total number of gas stations.
 Then N lines follow, each contains a pair of non-negative numbers: Pi, the unit gas price, and Di (<=D),
 the distance between this station and Hangzhou, for i=1,...N. All the numbers in a line are separated by a space.

输出
For each test case, print the cheapest price in a line, accurate up to 2 decimal places. It is assumed that the tank is empty at the beginning.
 If it is impossible to reach the destination, print "The maximum travel distance = X" where X is the maximum possible distance the car can run, accurate up to 2 decimal places.

样例输入
59 525 19 2
3.00 314
3.00 0
样例输出
82.89
*/


/*
分析：https://www.cnblogs.com/XBWer/p/3866486.html
      很明显，这道题的思路是贪心，也就是要用尽可能少的钱，要去尽可能远的地方。
      那么，像这种贪心的题目，怎么去思考呢？
      首先，今天听到戴龙翱（ZJU大牛）提到的一点，对于贪心的题目，测试样例，必须自己去体会一遍，这样，很有可能会给我们带来不少启发。
      那么，我们就一起来过一遍测试样例吧：

Sample Input 1:
50 1300 12 8
6.00 1250
7.00 600
7.00 150
7.10 0
7.20 200
7.50 400
7.30 1000
6.85 300

Sample Output 1:
749.17

先画个图再看
1：起点，肯定是要加油的，不然跑都跑不起来，那么，问题来了——加多少？
让我们先来观察一下，油箱加满之后，最远能跑600；也就是说，如果我现在在起点把油箱加满的话，[0,600]都是我可以达到的路程范围；
好了，那么，我们只需要看这个范围内的如何做出贪心策略；
起点处的油价是7.1，如果之后遇到的第一个加油站A油价小于7.1，那么，在A之后不管是什么情况，我们都应该加油（至于要加多少，还不能确定），
因为至少在当前状态下，这样做是最“贪婪”的。
2：通过1的分析，我们选择了加油站B。而且值得强调的是，我们在起点A加的油跑到B时是正好用完的状态。
这时，我们站在了B点，B点最远能到750(150+600），我们又如何根据贪心算法来做出贪婪的决策呢？
B处的油价是7，显然，750之前的加油站有很多，油价有比7小的，比7大的，也有等于7的。那么，贪婪的你，一定不会傻到去选择一个油价贵的（如C、E）
因为我完全可以到达比7小的加油站再加油，为什么要去比7大的加油站加油呢？
so，我们选择了D（油价6.85），而且，D的油价比当前的便宜，所以我们加油只要够从B——>D就好，加多了就不符合我贪婪的本性了！
3：到了D之后，可以说是比较开心的，因为在他[300,300+600]的范围内这价是最便宜的，此时不加更待何时！？因为是最便宜的，所以，为了贪，必须加满！
加满了之后，最远可以到900（300+600），那么，在900之前，我们会遇到E、F，且F油价较E要便宜一些，因此，为了到达目的地，我们不得不到F加油。
4：和之前的情况有所不同的是，这次，我们到目的地的时候，还是有油剩余的（600-300<600），而且剩余的油够跑300（即可以跑到900）。
那么，我们在F加多少的油呢？
站在F的位置，我们开始思考。距离400有一个加油站G，可是油价要7.3，坑爹呢！这么贵！
可是，就算F加满了，我们也只能跑到1200（600+600），所以，没办法，为了到达目的地，我们不得不到G加，但是，这里要注意，因为G比F的油价要贵，
所以，为了贪，我们会在F把油加满，（在能够达到目的地的前提下，尽可能在贵的地方少加点油，在便宜的地方多加点油——贪）；
5：到了G之后，计算了此时邮箱还剩下的油狗刨200，也就是说，我们在贵的的地方G只需要加50（1250-1000-200），能到H即可，因为H的油价是最便宜（没有之一），
在[1000,1000+600]的范围内，是最便宜的，so，就这样走到了H
6：走到了H之后，就不用多想了，H之后也没有加油站了，而且加满能够到目的地I的油量就够了。

经过了以上分析之后，要开始对以上的各个情况进行抽象，即把遇到的情况分类（要包括所有的情况），并且，基于贪心的思想去考虑不同的情况下，做出何种决策
处在当前加油站（起点加油站）的情况下
情况1：600米范围内，有目的地——计算恰好能到目的地的油量
情况2：600米范围内没有加油站，无论油价多贵——加满——能跑多远算多远
情况3：600米范围内有加油站：
                            a:有比当前加油站的油价更便宜的加油站——加到恰好能到那个最近的油站的油量
                            （注：1、如果有多个便宜的，还是要先选取最近的那个，而不是最便宜的那个；2、可能还有油剩余）
                            b:没有比当前加油站的油价更便宜的加油站——加满，然后在600范围内找到最便宜的加油站加油

再来看第二组数据：
Sample Input 2:
50 1300 12 2
7.10 0
7.00 600

Sample Output 2:
The maximum travel distance = 1200.00

分析过程：
1：600的范围内（包括600），有加油站，而且比当前的油价要便宜，因此，属于情况3—a，故，我们加到恰好能到，这里比较特殊的是，只有加满才恰好能到，
注意，这里不能归为情况2，因为情况2的结果对应着一定无法到达目的地，所以，当前的状态还无法判断出能不能到达目的地；
2：600范围内，没有加油站，这里属于情况2，能跑多远跑多远，因为已经无法到达目的地了，只能尽可能地跑更远

经过以上的分析，就可以开始尝试地写代码了
特殊的情况优化：
    1：起点没有加油站
    2：起点即终点

主要的几个关键点，或者说是行驶的过程中需要记录些什么信息：
    1：到达当前加油站的油量——因为，你要计算还需要加多少油，所以，总共需要的油量—现有的油量=在当前加油站要加的油量
 */

/*
#include<cstdio>
#include<algorithm>
#include<iostream>
using namespace std;

const int maxn = 502;

struct Gasstation{
    double pos;
    double price;
}gasstation[maxn];

bool cmp(Gasstation a,Gasstation b){
    if(a.pos!=b.pos){
        return a.pos<b.pos;
    }else{
        return a.price<b.pos;
    }
}

int main(){
    double Cmax,D,Davg;
    int N;
    scanf("%lf%lf%lf%d",&Cmax,&D,&Davg,&N);
    for(int i=0;i<N;i++){
        scanf("%lf%lf",&gasstation[i].price,&gasstation[i].pos);
    }
    sort(gasstation,gasstation+N,cmp);
    if(D==0){
        printf("0.00\n");
        return 0;
    }

    if(gasstation[0].pos!=0){
        printf("The maximum travel distance = 0.00\n");
        return 0;
    }

    int curstnum = 0;//当前所处的油站编号
    double curgas=0;//当前油量
    double curcost=0;//当前的花费
    bool flag = false;//是否到达目的地
    double maxrundis = Cmax*Davg;//邮箱加满最远能走的距离
    while(!flag){
        bool tag = false;//最大距离内是否有加油站
        bool ifcheaper = false;//是否有便宜的
        double cheaperstprice = 10000;//找出最便宜的
        int cheapernum ;//没有最便宜的情况下找出更便宜的
        for(int i = curstnum+1;i<N;i++){
            if(gasstation[i].pos-gasstation[curstnum].pos<=maxrundis){//范围内
                tag = true;
                if(gasstation[i].price<gasstation[curstnum].price){//情况3-a   //且有更便宜的
                    ifcheaper = true ;
                    double dist = gasstation[i].pos-gasstation[curstnum].pos;
                    double needgas = dist/Davg-curgas;
                    curgas = 0;
                    curcost+=needgas*gasstation[curstnum].price;
                    curstnum=i;
                    break;
                }
                if(gasstation[i].price<cheaperstprice){
                    cheaperstprice = gasstation[i].price;
                    cheapernum = i;
                }
            }else{
                break;
            }
        }
        if(!ifcheaper&&(maxrundis>=(D-gasstation[curstnum].pos))){//说明可以达到目的地了，情况1
            flag = true;
            double dist = D -gasstation[curstnum].pos;
            double needgas = dist/Davg-curgas;
            curcost+=needgas*gasstation[curstnum].price;
            printf("%.2lf\n",curcost);
        }
        if(tag&&!ifcheaper){//情况3-b
            double needgas = Cmax - curgas;
            curcost+=needgas*gasstation[curstnum].price;
            double dist = gasstation[cheapernum].pos-gasstation[curstnum].pos;
            curgas = Cmax-dist/Davg;
            curstnum = cheapernum;
        }else if(!tag){
            printf("The maxinum travel distance = %.2lf\n",gasstation[curstnum].pos+maxrundis);
            return 0;
        }
    }
    return 0;
}*/



/*
 * 贪心：当前最优，子结构最优，自顶向下，迭代，缩小范围
 * 问题的最优子结构性质是该问题可用贪心算法或动态规划算法求解的关键特征。贪心算法的每一次操作都对结果产生直接影响，而动态规划则不是。
 * 贪心算法对每个子问题的解决方案都做出选择，不能回退；动态规划则会根据以前的选择结果对当前进行选择，有回退功能。动态规划主要运用于二维或三维问题，而贪心一般是一维问题
 *
 *
 * 思路：
 * 若没有到达终点，重复以下过程，一直走，不断缩小范围，直到到达
 *      1. 在当前站加满所能走的最大距离（Cmax*Davg）内有加油站
 *          1.1 有比当前站的油价小的第一个加油站，加到能到达这个站的油即可（注：一定是第一个当前小的，关于为什么不妨选择后面最小的比比看）
 *          1.2 没有比当前价格小的，应该在当前站点加满油，找到该范围内除当前油站的最小者，去该加油站。
 *      2. 每次到达一个站点后，刷新油量（遇到1.2情况时会有剩余油量的）
 *      1.2 最大距离内没有加油站，加满，跑最大距离即可
 * 已经到达，结束旅程，输出花费
 *
 *
 * 贪心主要原则是不断局部采取最优策略，不断缩小范围，从而达到整体最优
 *
 *
 * */

#include<cstdio>
#include<algorithm>
using namespace std;
const int maxn = 510;
struct Station{
    double d;
    double price;
};
bool cmp(Station a,Station b){
    return a.d<b.d;
};

int main(){
    Station station[maxn];
    double Cmax,D,Davg;
    int N;
    scanf("%lf%lf%lf%d",&Cmax,&D,&Davg,&N);
    for(int i=0;i<N;i++){
        scanf("%lf%lf",&station[i].price,&station[i].d);
    }
    station[N].price=0;
    station[N].d=D;//为了方便计算，把目的地也加入
    sort(station,station+N+1,cmp);

    bool isReach=false;//是否到达目的地
    double maxD = Cmax*Davg;//加满可以跑的最远距离
    int curStation=0;//当前到达的加油站，初始置为第一个
    double totalCost = 0;//总花费
    double curC=0;//当前油量
      // 极限条件
    if(station[0].d!=0){//出发地没有加油站，不能到达
        printf("The maxinum travel distance = 0.00");
    }
    if(D==0){ //目标地点为0  说明不用走了 现在就在目的地了
        isReach = true;
    }
    while(!isReach){
        double curSmallPrice =1000000;//用来记录后面最小费用加油站的费用
        int curSmallStation = curStation;//最小费用加油站的坐标位置
        bool isGas=false;//范围内是否有加油站
        bool isCheaper=false;//范围内是否有比当前站便宜的

        //用for循坏来寻找从当前加油站最大行驶范围内的加油站情况
        for(int i=curStation+1;i<=N;i++){
            if(station[i].d-station[curStation].d<=maxD){
                isGas=true;
                if(station[i].price<station[curStation].price){
                    curSmallStation=i;
                    isCheaper = true;
                    break;
                }else{//1.1.2
                    if(station[i].price<curSmallPrice){
                            curSmallPrice = station[i].price;
                            curSmallStation=i;//记录范围内最便宜的
                    }
                }
            }else{
                break;
            }
        }
        //当前站后面的加油站统计完毕，计算费用
        if(isGas){
            double d = station[curSmallStation].d-station[curStation].d;//合适费用的加油站与当前加油站的巨鹿
            double needGas;
            if(isCheaper){//有比当前站便宜的
                needGas=d/Davg-curC; //上一次行程中可能会有剩余，所以当前需要加的油减去剩余的油
                curC = 0;//到达站点后，正好用完油
            }else{//没有比当前站点便宜的，在当前站点加满油，开往后面站点最便宜的加油站
                needGas = Cmax-curC;//加满
                curC = Cmax - d/Davg;//到达那个便宜的站点，刷新剩余油量
            }
            totalCost+=needGas*station[curStation].price;//计算此次费用
            curStation=curSmallStation;//刷新当前站点
        }else if(!isReach){//范围内没有加油站了，直接加满跑出最大距离
            printf("The maxinum travel distance = %.2lf\n",station[curStation].d+maxD);
            return 0;
        }
        if(station[curStation].d-station[N].d==0){//当前站与目标站距离为0，说明到站了
            isReach=true;
        }

    }
    //输出费用
    printf("%.2lf\n",totalCost);
    return 0;
}