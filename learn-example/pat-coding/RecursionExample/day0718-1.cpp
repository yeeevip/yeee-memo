//
// Created by 13000 on 2018/7/18.


/*题目：输入一个字符串，打印出该字符串中字符的所有排列。例如输入字符串abc，则输出由字符a、b、c所能排列出来的所有字符串abc、acb、bac、bca、cab和cba。
分析：这是一道很好的考查对递归理解的编程题，因此在过去一年中频繁出现在各大公司的面试、笔试题中。

我们以三个字符abc为例来分析一下求字符串排列的过程。首先我们固定第一个字符a，求后面两个字符bc的排列。当两个字符bc的排列求好之后，
 我们把第一个字符a和后面的b交换，得到bac，接着我们固定第一个字符b，求后面两个字符ac的排列。现在是把c放到第一位置的时候了。
 记住前面我们已经把原先的第一个字符a和后面的b做了交换，为了保证这次c仍然是和原先处在第一位置的a交换，我们在拿c和第一个字符交换之前，
 先要把b和a交换回来。在交换b和a之后，再拿c和处在第一位置的a进行交换，得到cba。我们再次固定第一个字符c，求后面两个字符b、a的排列。

既然我们已经知道怎么求三个字符的排列，那么固定第一个字符之后求后面两个字符的排列，就是典型的递归思路了。

基于前面的分析，我们可以得到如下的参考代码：*/
//

/*#include<cstdio>
void Permutation(char* pStr,char* pBegin);
void Permutation(char* pStr){
    Permutation(pStr,pStr);
}
void Permutation(char* pStr,char* pBegin){
    if(!pStr||!pBegin) return;
    if(*pBegin=='\0')printf("%s\n",pStr);
    else{
        for(char* pCh=pBegin;*pCh!='\0';++pCh){
            char temp = *pCh;
            *pCh=*pBegin;
            *pBegin=temp;
            Permutation(pStr,pBegin+1);
            temp = *pCh;
            *pCh=*pBegin;
            *pBegin=temp;
        }
    }
}
int main(){
    char str[] = "abc";
    Permutation(str);
    return 0;
}*/

/*
#include<iostream>
#include<string>
using namespace std;

void Permutation(string str,int begin);
void Permutation(string str){
    Permutation(str,0);
}
void swap(string &str,int k1,int k2){
    char temp = str[k1];
    str[k1]=str[k2];
    str[k2]=temp;
}
*//**
    * isExist判断j位置的字符是否已经在list[0]~list[j-1]中出现过了
    * list是含重复字符的数组，i是指示当前位置的游标，j是要判断的字符的位置
    *//*
bool isExist(string str,int f,int c){
    for(int i=f;i<c;i++){
        if(str[c]==str[i]) return true;
    }
    return false;
}
void Permutation(string str,int begin){
    if(begin==str.size()-1){
        cout<<str<<endl;
    }else{
        for(int i=begin;i<str.size();i++){
            if(!isExist(str,begin,i)){
                swap(str,begin,i);
                Permutation(str,begin+1);
                swap(str,begin,i);
            }
        }
    }
}
int main(){
    string str;
    cin>>str;
    Permutation(str);
    return 0;
}*/


/*字符串组合

求n个字符中m个字符的组合的时候，可以把这n个字符分成两部分：第一个字符和其余的所有字符。
 如果组合里包含第一个字符，则下一步在剩余的字符里选取m-1个字符；如果组合里不包含第一个字符，
 则下一步在剩余的n-1个字符里选取m个字符，从中可以看出又是个递归的过程。*/
/*

#include<iostream>
#include <vector>
#include<string>
using namespace std;
vector<char> st;//存储组合的容器
void combination(string str,int begin,int num){
    if(num==0){
        for(int i=0;i<st.size();i++){
            cout<<st[i];
        }
        cout<<endl;
        return;
    }
    if(begin>=str.length()){
        return;
    }else{
        //把第一个字符放入组合中，在剩余的选取num-1个字符
        st.push_back(str[begin]);
        combination(str,begin+1,num-1);
        //组合里不包含第一个元素，则下一步在剩余的元素中选取num个元素
       st.pop_back();
        combination(str,begin+1,num);
    }
}

void combination(string str){
    for(int i=1;i<=str.length();i++){
        combination(str,0,i);
    }
}

int main(){
    string str;
    cin>>str;
    combination(str);
}*/
#include <cstdio>
#include<string>
#include<vector>
using namespace std;
vector<char> array;
string str;
void combination(int begin,int num){
     if(num==0){
        for(int i=0;i<array.size();i++){
            printf("%c",array[i]);
        }
        printf("\n");
         return;
    }
    if(begin>=str.size()){
        return;
    }else{

        array.push_back(str[begin]);
        combination(begin+1,num-1);
        array.pop_back();
        combination(begin+1,num);

    }
}
int main(){
    str="abcde";
    combination(0,2);
    return 0;
}

