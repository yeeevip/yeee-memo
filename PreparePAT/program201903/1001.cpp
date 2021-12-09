/*1001 A+B Format （20 分)
Calculate a+b and output the sum in standard format -- that is, the digits must be separated into groups of three
 by commas (unless there are less than four digits).

Input Specification:
Each input file contains one test case. Each case contains a pair of integers a and b where −10
​6
≤a,b≤10
​6
​​ . The numbers are separated by a space.

Output Specification:
For each test case, you should output the sum of a and b in one line. The sum must be written in the standard format.

Sample Input:
-1000000 9
Sample Output:
-999,991
 */
/*
#include <iostream>
using namespace std;
int main() {
    int a, b;
    cin >> a >> b;
    string s = to_string(a + b);
    int len = s.length();
    for (int i = 0; i < len; i++) {
        cout << s[i];
        if (s[i] == '-') continue;
        if ((i + 1) % 3 == len % 3 && i != len - 1) cout << ",";
    }
    return 0;
}*/


#include<cstdio>
#include<cstdlib>
#include<cmath>
#include<cstring>
const int maxSize = 10;
int main(){
    int a,b,result,len=0,count=0,ex=0;
    char numStr[maxSize];
    scanf("%d %d",&a,&b);
    result = a+b;
    if(result<0){
        printf("-");
    }
    sprintf(numStr,"%d",abs(result));
    len = strlen(numStr);
    ex = len%3;
    bool first=true;
    for(int i = 0;i<len;i++){
        if(ex!=0&&count==ex&&first){
            printf(",");
            count=0;
            first=false;
        }
        if(count!=0&&count%3==0){
            printf(",");
        }
        printf("%c",numStr[i]);
        count++;
    }
    return 0;
}


