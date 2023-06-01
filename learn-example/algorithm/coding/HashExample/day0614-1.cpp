/*题目描述
        Being unique is so important to people on Mars that even their lottery is designed in a unique way.
        The rule of winning is simple: one bets on a number chosen from [1, 104]. The first one who bets on a unique number wins.
        For example, if there are 7 people betting on 5 31 5 88 67 88 17, then the second one who bets on 31 wins.
输入
        Each input file contains one test case. Each case contains a line which begins with a positive integer N (<=105)
        and then followed by N bets. The numbers are separated by a space.
输出
        For each test case, print the winning number in a line. If there is no winner, print "None" instead.
样例输入
7 5 31 5 88 67 88 17
5 888 666 666 888 888
样例输出
31
None

 */
#include <cstdio>
const int maxn=102766;
int main(){
    int n;
    while (scanf("%d",&n)!=EOF){
        int bets[110],storeCount[maxn]={0},k=0;
        for(int i=0;i<n;i++){
            scanf("%d",&bets[i]);
            storeCount[bets[i]]++;
        }
        for(int i=0;i<n;i++){
            if(storeCount[bets[i]]==1) {
                printf("%d\n",bets[i]);
                k++;
                break;
            }
        }
        if(k==0) printf("None\n");
    }
    return 0;
}