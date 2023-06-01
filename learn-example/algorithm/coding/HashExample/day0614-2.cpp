/*
题目描述
        Given two strings S1 and S2, S = S1 - S2 is defined to be the remaining string after taking all the characters
        in S2 from S1. Your task is simply to calculate S1 - S2for any given strings. However, it might not be that simple to do it fast.
输入
        Each input file contains one test case. Each case consists of two lines which gives S1 and S2, respectively.
        The string lengths of both strings are no more than 104. It is guaranteed that all the characters are visible
        ASCII codes and white space, and a new line character signals the end of a string.
输出
        For each test case, print S1 - S2 in one line.
样例输入
They are students.
aeiou
样例输出
Thy r stdnts.

 */

#include<cstdio>
#include <cstring>
using namespace std;
int charToIntHas(char c){
    if(c>='a'&&c<='z') return c-'a';
    else return c-'A'+26;//大写字母
}
int main(){
    char str[110],str2[110];
    bool charHash[80]={false};
    int len1,len2;
  //  scanf("%s",str);
    //scanf("%s",str2);
    gets(str);
    gets(str2);
    len1=strlen(str);
    len2=strlen(str2);
    for(int i=0;i<len2;i++)
        charHash[charToIntHas(str2[i])]=true;
    for(int i=0;i<len1;i++){
        if(!charHash[charToIntHas(str[i])])
            printf("%c",str[i]);
    }
}