/*
题目描述
输入一个字符串，长度小于等于200，然后将输出按字符顺序升序排序后的字符串。

输入
测试数据有多组，输入字符串。

输出
 对于每组输入,输出处理后的结果。

样例输入
tianqin
样例输出
aiinnqt
提示
注意输入的字符串中可能有空格。
 */

#include<cstdio>
#include <cstring>
#include<algorithm>
using namespace std;
const int maxn=210;
int main(){
    char s[maxn];
    int len = -1;
    gets(s);
    len = strlen(s);
    sort(s,s+len);
    puts(s);
    return 0;
}