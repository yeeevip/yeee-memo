/*
题目描述

        会下国际象棋的人都很清楚：皇后可以在横、竖、斜线上不限步数地吃掉其他棋子。如何将8个皇后放在棋盘上（有8 * 8个方格），使它们谁也不能被吃掉！这就是著名的八皇后问题。
对于某个满足要求的8皇后的摆放方法，定义一个皇后串a与之对应，即a=b1b2...b8，其中bi为相应摆法中第i行皇后所处的列数。已经知道8皇后问题一共有92组解（即92个不同的皇后串）。
给出一个数b，要求输出第b个串。串的比较是这样的：皇后串x置于皇后串y之前，当且仅当将x视为整数时比y小。


输入

        第1行是测试数据的组数n，后面跟着n行输入。每组测试数据占1行，包括一个正整数b(1 <= b <= 92)


输出

        输出有n行，每行输出对应2一个输入。输出应是一个正整数，是对应于b的皇后串。


样例输入
3
6
4
25
样例输出
25713864
17582463
36824175

 */



/*解题思路一

        因为要求出92种不同摆放方法中的任意一种，所以我们不妨把92种不同的摆放方法一次性求出来，存放在一个数组里。
为求解这道题我们需要有一个矩阵仿真棋盘，每次试放一个棋子时只能放在尚未被控制的格子上，一旦放置了一个新棋子，就在它所能控制的所有位置上设置标记，
如此下去把八个棋子放好。当完成一种摆放时，就要尝试下一种。若要按照字典序将可行的摆放方法记录下来，就要按照一定的顺序进行尝试。
也就是将第一个棋子按照从小到大的顺序尝试；对于第一个棋子的每一个位置，将第二个棋子从可行的位置从小到大的顺序尝试；在第一第二个棋子固定的情况下，将第三个棋子从可行的位置从小到大的顺序尝试；依次类推。
首先，我们有一个8*8的矩阵仿真棋盘标识当前已经摆放好的棋子所控制的区域。用一个有92行每行8个元素的二维数组记录可行的摆放方法。
用一个递归程序来实现尝试摆放的过程。基本思想是假设我们将第一个棋子摆好，并设置了它所控制的区域，则这个问题变成了一个7皇后问题，
用与8皇后同样的方法可以获得问题的解。那我们就把重心放在如何摆放一个皇后棋子上，

 摆放的基本步骤是：从第1到第8个位置，顺序地尝试将棋子放置在每一个未被控制的位置上，
设置该棋子所控制的格子，将问题变为更小规模的问题向下递归，需要注意的是每次尝试一个新的未被控制的位置前，要将上一次尝试的位置所控制的格子复原。*/
/*
#include<cstdio>
#include<cmath>
using namespace std;
int queenPlaces[92][8];//存放92种皇后旗子的摆放方法
int count = 0;
int board[8][8];
void putQueen(int ithQueen){
    int i,k,r;
    if(ithQueen==8){
        count++;
        return;
    }
    for(i=0;i<8;i++){
        if(board[i][ithQueen]==-1){
            //摆放皇后
            board[i][ithQueen]=ithQueen;
            //将其后所有的摆放方法的ith歌皇后都摆在i+1的位置上
            //在i增加以后，后面的第ith个皇后摆放方法后覆盖此时的设置
            for(k=count;k<92;k++){
                queenPlaces[k][ithQueen]=i+1;
            }
            //设置控制范围
            for(k=0;k<8;k++){
                for(r=0;r<8;r++){
                    if(board[k][r]==-1&&  (k == i || r == ithQueen || abs(k - i) == abs(r - ithQueen))){
                        board[k][r]=ithQueen;
                    }
                }
            }
            //向下级递归
            putQueen(ithQueen+1);
            //回溯，撤销控制范围
            for(k=0;k<8;k++){
                for(r=0;r<8;r++){
                    if(board[k][r]==ithQueen) board[k][r]=-1;
                }
            }
        }
    }
}
int main(){
    int n,i,j;
    for(i=0;i<8;i++){
        for(j=0;j<8;j++){
            board[i][j] = -1;
        }
        for(j=0;j<92;j++){
            queenPlaces[j][i]=0;
        }
    }
    putQueen(0);//从第0个棋子开始摆放，运行结果是将queenPlaces生成好
    scanf("%d",&n);
    for(i=0;i<n;i++){
        int ith;
        scanf("%d",&ith);
        for(j=0;j<8;j++){
            printf("%d",queenPlaces[ith-1][j]);
        }
        printf("\n");
    }

    return 0;
}*/

/*

解题思路二

        上面的方法用一个二维数组来记录棋盘被已经放置的棋子的控制情况，每次有新的棋子放置时用了枚举法来判断它控制的范围。
还可以用三个一维数组来分别记录每一列，每个45度的斜线和每个135度的斜线上是否已经被已放置的棋子控制，这样每次有新的棋子放置时，
不必再搜索它的控制范围，可以直接通过三个一维数组判断它是否与已经放置的棋子冲突，在不冲突的情况下，也可以通过分别设置三个一维数组的相应的值，
来记录新棋子的控制范围。
*/
/*
#include<cstdio>
int record[92][9],mark[9],count=0;//record记录全部解，mark记录当前解；
bool range[9],line1[17],line2[17];//分别记录列方向，45度，135度方向上被控制的情况
void tryToPut(int i){
    if(i>8){//如果最后一个皇后放置完毕，将当前解复制到全部解中
        for(int k=1;k<9;k++){
            record[count][k]=mark[k];
        }
        count++;
    }
    for(int j=1;j<=8;j++){//逐一尝试将当前皇后放置在不同列上
        if(range[j]&&line1[i+j]&&line2[i-j+9]){//如果与前面的不冲突,则把当前皇后放置在当前位置
            mark[i]=j;
            range[j]=line1[i+j]=line2[i-j+9]=false;
            tryToPut(i+1);
            range[j]=line1[i+j]=line2[i-j+9]=true;
        }
    }
}

int main(){
    int i,testtimes,num;
    scanf("%d",&testtimes);
    for(i=0;i<=8;i++){
        range[i]=true;
    }
    for(i=0;i<17;i++){
        line1[i]=line2[i]=true;
    }
    tryToPut(1);
    while(testtimes--){
        scanf("%d",&num);
        for(i=1;i<=8;i++){
            printf("%d",record[num-1][i]);
        }
        printf("\n");
    }
}*/



/*

解题思路三

        这个题目也可以不用仿真棋盘来模拟已放置棋子的控制区域，而只用一个有8个元素的数组记录已经摆放的棋子摆在什么位置，
当要放置一个新的棋子时，只需要判断它与已经放置的棋子之间是否冲突就行了。

*/

#include<cstdio>
int ans[92][8],n,b,i,j,num,hang[8];
void queen(int i){
    int j,k;
    if(i==0){//一组新的解产生了
        for(j=0;j<8;j++){
            ans[num][j]=hang[j]+1;
        }
        num++;
        return;
    }
    for(j=0;j<8;j++){//将当前皇后i逐一尝试放置在不同的列
        for(k=0;k<i;k++) {//逐一判定i与前面的皇后是否冲突
            if (hang[k] == j || (k - i) == (hang[k] - j) || (i - k) == (hang[k] - j)) {
                break;
            }
        }
         if(k==i){//放置i，尝试i+1个皇后
            hang[i]=j;
            queen(i+1);
        }
    }
}
int main(){
    num=0;
    queen(0);
    scanf("%d",&n);
    for(i=0;i<n;i++){
        scanf("%d",&b);
        for(j=0;j<8;j++){
            printf("%d",ans[b-1][j]);
        }
        printf("\n");
    }
    return 0;
}