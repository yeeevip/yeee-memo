//
// Created by 13000 on 2018/7/20.
//

/*
 * （典型递归问题）汉诺塔问题
 * */

#include<cstdio>
void move(char getone,char putone){
    printf("%c-->%c\n",getone,putone);
}
void hanoit(int n,char a,char b, char c){
    if(n==1){
        move(a,c);
    }else{
        hanoit(n-1,a,c,b);
        move(a,c);
        hanoit(n-1,b,a,c);
    }
}

int main(){

    hanoit(3,'A','B','C');
    return 0;
}