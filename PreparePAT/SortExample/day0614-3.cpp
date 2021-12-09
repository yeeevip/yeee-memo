/*
题目描述
        对输入的n个数进行排序并输出。

输入
        输入的第一行包括一个整数n(1<=n<=100)。 接下来的一行包括n个整数。

输出
        可能有多组测试数据，对于每组数据，将排序后的n个整数输出，每个数后面都有一个空格。
每组测试数据的结果占一行。

样例输入
5
5 4 3 1 2
样例输出
1 2 3 4 5

 */

/*
#include <cstdio>
#include <algorithm>
using namespace std;
const int maxn=110;
int main(){
    int n;
    while (scanf("%d",&n)!=EOF){
        int num[maxn],i=0;
        while (n--){
            scanf("%d",&num[i++]);
        }
        sort(num,num+i);
        for(int j=0;j<i;j++){
            printf("%d",num[j]);
            if(j<i-1){
                printf(" ");
            }
        }
    }
    return 0;
}
*/

#include <cstdio>
#include <set>
using namespace std;
int main(){
    int n,temp;
    while (scanf("%d",&n)){
        set<int> num;
        int k=0;
        while(n--){
            scanf("%d",&temp);
            num.insert(temp);
        }
        set<int>::iterator it;
        for(it=num.begin();it!=num.end();it++){
            printf("%d",*it);
            if((k++)<num.size()-1) printf(" ");
        }
    }
}