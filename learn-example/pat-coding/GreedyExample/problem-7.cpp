/*
 * 找零钱
 *
 * 题目描述
小智去超市买东西，买了不超过一百块的东西。收银员想尽量用少的纸币来找钱。
纸币面额分为50 20 10 5 1 五种。请在知道要找多少钱n给小明的情况下，输出纸币数量最少的方案。 1<=n<=99;
输入
有多组数据  1<=n<=99;
输出
对于每种数量不为0的纸币，输出他们的面值*数量，再加起来输出
样例输入
25
32
样例输出
20*1+5*1
20*1+10*1+1*2
 *
 *
 * */


#include<cstdio>
int number[]={50,20,10,5,1};

int main(){
    while(1){
        int money;
        bool first = true;
        scanf("%d",&money);
        for(int i=0;i<5;i++){
            if(money!=0){
                if(money>=number[i]){
                    if(!first){
                        printf("+");
                    }
                    printf("%d*%d",number[i],money/number[i]);
                    money%=number[i];
                    first =false;
                }
            }
        }
    }
    return 0;
}