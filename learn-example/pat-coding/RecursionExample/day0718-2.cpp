//
// Created by 13000 on 2018/7/18.
//
/*

这就是有名的八皇后问题。解决这个问题通常需要用递归，而递归对编程能力的要求比较高。因此有不少面试官青睐这个题目，用来考察应聘者的分析复杂问题的能力以及编程的能力。

由于八个皇后的任意两个不能处在同一行，那么这肯定是每一个皇后占据一行。于是我们可以定义一个数组ColumnIndex[8]，数组中第i个数字表示位于第i行的皇后的列号。
先把ColumnIndex的八个数字分别用0-7初始化，接下来我们要做的事情就是对数组ColumnIndex做全排列。由于我们是用不同的数字初始化数组中的数字，因此任意两个皇后肯定不同列。
我们只需要判断得到的每一个排列对应的八个皇后是不是在同一对角斜线上，也就是数组的两个下标i和j，是不是i-j==ColumnIndex[i]-Column[j]或者j-i==ColumnIndex[i]-ColumnIndex[j]。
*/

/*
#include<iostream>
#include<cmath>
#include<algorithm>
using namespace std;
int g_number=0;
void Permutation(int columnIndexs[],int queens,int index);
bool Check(int columnIndex[],int length);
void PrintQueen(int columnIndex[],int length);
void EightQueen(){
    const int queens=8;
    int ColumnIndex[queens];
    for(int i=0;i<queens;++i){
        ColumnIndex[i]=i;
    }
    Permutation(ColumnIndex,queens,0);
}
void swap(int &a,int &b){
    int temp = a;
    a = b;
    b = temp;
}
void Permutation(int columnIndex[],int length, int index){
    if(index==length){
        if(Check(columnIndex,length)){
            ++g_number;
            PrintQueen(columnIndex,length);
        }
    }else{
        for(int i=index;i<length;++i){
            swap(columnIndex[i],columnIndex[index]);
            Permutation(columnIndex,length,index+1);
            swap(columnIndex[i],columnIndex[index]);
        }
    }
}
bool Check(int columnIndex[],int length){
    for(int i=0;i<length;++i){
        for(int j=i+1;j<length;++j){
            if(i-j==columnIndex[i]-columnIndex[j]||i-j==columnIndex[j]-columnIndex[i]){
                return false;
            }
        }
    }
    return true;
}
void PrintQueen(int columnIndex[],int length){
    printf("Solution %d\n",g_number);
    for(int i=0;i<length;i++){
        printf("%d\t",columnIndex[i]);
    }
    printf("\n");
}

int main(){
    EightQueen();
}
*/
#include<cstdio>
int columnIndex[8];
int count=0;
bool check(){
    for(int i=0;i<8;i++){
        for(int j=i+1;j<8;j++){
            if(i-j==columnIndex[i]-columnIndex[j]||i-j==columnIndex[j]-columnIndex[i]){
                return true;
            }
        }
    }
    return false;
}
void Permutation(int index){
    if(index==8){
        if(!check()){
            printf("%d\n",++count);
            for(int i=0;i<8;i++){
                printf("%d ",columnIndex[i]);
            }
            printf("\n");
        }
    }else{
        for(int i=index;i<8;i++){
            int temp = columnIndex[i];
            columnIndex[i]=columnIndex[index];
            columnIndex[index]=temp;
            Permutation(index+1);
            temp = columnIndex[i];
            columnIndex[i]=columnIndex[index];
            columnIndex[index]=temp;
        }
    }
}

int main(){
    for(int i=0;i<8;i++){
        columnIndex[i]=i+1;
    }
    Permutation(0);
}


