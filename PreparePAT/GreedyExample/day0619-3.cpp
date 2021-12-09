/*
1.硬币找零
难度评级：★

　　假设有几种硬币，如1、3、5，并且数量无限。请找出能够组成某个数目的找零所使用最少的硬币数。


 */

 /*
  * 贪心算法
  * 按照贪心算法的思想，需要不断地使用面值最大的硬币。如果要找零的值小于最大的硬币值，则尝试第二大的硬币，依次类推。
  *
  * 虽然贪心算法不是对所有问题都能得到整体的最优解，但是实际应用中的许多问题都可以使用贪心算法得到最优解。
  * 有时即使使用贪心算法不能得到问题的最优解，但最终结果也是较优的解
  * */
/*

#include<iostream>
 using namespace std;
 const int ONE=1,TWO=2,FIVE=5,TEN=10;
 int main(){
     int money;
     int reOne=0,reTwo=0,reFive=0,reTen=0;
     cin>>money;
     //尝试每一种硬币
     while(money>=TEN){
         reTen++;
         money-=TEN;
     }
     while(money>=FIVE){
         reFive++;
         money-=FIVE;
     }
     while(money>=TWO){
         reTwo++;
         money-=TWO;
     }
     while(money>=ONE){
         reOne++;
         money-=ONE;
     }
     cout<<reTen<<endl<<reFive<<endl<<reTwo<<endl<<reOne;
     return 0;
 }

*/





/*
 * 动态规划
 *
 * 对于最少硬币找零问题状态转移方程：
        f (j) = min{  f(j - coin[i]) ＋ 1,  i = 0......N  ｝
可以理解为：j为需要找零多少元，有N种硬币，对于每种硬币，我们可以依次假设f(i)中至少包含一个coin[j] (j=0, 1......N) ，
 然后得到所需的最少硬币是f(j- coin[i]) ＋ 1，最后再从这N次假设中选出最小的就是f(i)。

有人可能会有疑问，为什么只是假设存在一块硬币coin[j]，存在k块硬币难道不用考虑吗？假如f(i)真的包含多个coin[j]，
 我们只取一个coin[j]，那么剩下的几个coin[j]的最优组合肯定已经包含在 f(i - coin[j]) 里面了，我们根本不用关心它们。
*/

#include<iostream>
using namespace std;
/*
 * money 需要找零的钱
 * coin 可用的硬币
 * n 硬币种类
 *
 * */
void FindMin(int money,int *coin,int n){
    int *f = new int[money+1]();//存储1.....money找零最少需要的硬币个数
    int *coinValue = new int[money+1]();//最后加入的硬币，方便后面输出是那几个硬币
    for(int i=1;i<=n;i++){
        int minNum=i;//表示i个1元硬币正好找零i元，这是一种找零的组合，并不是所有组合里最少的，下面寻找最小值
        int curMoney = 0;//这次找零，在原来的基础上需要的硬币
        for(int j=0;j<n;j++){
            if(i>=coin[j]){//找零的钱大于这个硬币的面值
                if(f[i-coin[j]]+1<=minNum){
                    //在更新时，需要判断i-coin[j]是否能找的开，如果找不开就不需要更新
                    if(i-coin[j]==0||coinValue[i-coin[j]]!=0){
                        minNum=f[i-coin[j]]+1;//更新
                        curMoney=coin[j];//更新
                    }
                }
            }
        }
        f[i]=minNum;
        coinValue[i]=curMoney;
    }
    //输出结果
    if(coinValue[money]==0){
        cout<<"找不开零钱"<<endl;
    }else{
        cout<<"需要最少的硬币个数为："<<f[money]<<endl;
        cout<<"硬币分别为："<<endl;
        while(money>0){
            cout<<coinValue[money]<<"，";
            money-=coinValue[money];
        }
    }
    delete []f;
    delete []coinValue;
}
int main(){
    int Money = 599;
    int coin[]={1,2,5,9,10};
    FindMin(Money,coin,5);
    return 0;
}
