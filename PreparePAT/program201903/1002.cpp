/*
This time, you are supposed to find A+B where A and B are two polynomials.

Input

        Each input file contains one test case. Each case occupies 2 lines, and each line contains the information
        of a polynomial: K N1 aN1 N2 aN2 … NK aNK, where K is the number of nonzero terms in the polynomial, Ni and aNi (i=1, 2, …, K)
        are the exponents and coefficients, respectively. It is given that 1 <= K <= 10，0 <= NK < … < N2 < N1 <=1000.

Output

        For each test case you should output the sum of A and B in one line, with the same format as the input.
        Notice that there must be NO extra space at the end of each line. Please be accurate to 1 decimal place.

Sample Input

2 1 2.4 0 3.2
2 2 1.5 1 0.5

Sample Output

3 2 1.5 1 2.9 0 3.2
 */
#include<cstdio>
const int maxSize = 1001;
struct{
    double coe;
}A[maxSize],B[maxSize],Result[maxSize];
int hash[maxSize],count=0;

int main(){
    for(int i=0;i<maxSize;i++){
        hash[i]=1;
    }
    for(int i=0;i<2;i++){
        int k;
        scanf("%d",&k);
        for(int j=0;j<k;j++){
            int exp;
            double coe;
            scanf("%d %lf",&exp,&coe);
            count+=hash[exp];
            hash[exp]=0;
            if(i==0){
                A[exp].coe = coe;
            } else if(i==1){
                B[exp].coe = coe;
            }
            Result[exp].coe = A[exp].coe+B[exp].coe;
        }
    }
    printf("%d",count);
    for(int i =maxSize-1;i>=0;i--){
        if(Result[i].coe!=0.0){
            printf(" %d %.1lf",i,Result[i].coe);
        }
    }

    return 0;
}