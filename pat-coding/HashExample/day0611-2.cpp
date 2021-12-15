/*

题目描述
先输入一组数，然后输入其分组，按照分组统计出现次数并输出，参见样例。

输入
输入第一行表示样例数m，对于每个样例，第一行为数的个数n，接下来两行分别有n个数，第一行有n个数，第二行的n个数分别对应上一行每个数的分组，n不超过100。

输出
输出m行，格式参见样例，按从小到大排。

样例输入
1
7
3 2 3 8 8 2 3
1 2 3 2 1 3 1
样例输出
1={2=0,3=2,8=1}
2={2=1,3=0,8=1}
3={2=1,3=1,8=0}
*/
/*
#include<iostream>
#include <cstdio>
#include<vector>
#include<map>
using namespace std;
void display(map<int,int> m){
  map<int,int>::iterator it;
  for(it=m.begin();it!=m.end();it++){
    if(it==m.begin())
      printf("%d=%d",it->first,it->second);
     else
      printf(" %d=%d",it->first,it->second);
  }
}
int main(){
  int n;
  vector<int> arr1,arr2;
  map<int,map<int,int>> mmap;

 scanf("%d",&n);
 while (n--){
   int m,temp;
   scanf("%d",&m);
   for(int i=0;i<m;i++){
     scanf("%d",&temp);
     arr1.push_back(temp);
   }
   for(int i=0;i<m;i++){
     scanf("%d",&temp);
     arr2.push_back(temp);
   }
 }

  vector<int>::iterator it1,it2;
  for(it2=arr2.begin();it2!=arr2.end();it2++){
    for(it1=arr1.begin();it1!=arr1.end();it1++){
      mmap[*it2][*it1]=0;
    }
  }
  for(it1=arr1.begin(),it2=arr2.begin();it1!=arr1.end();it1++,it2++)
    (mmap[*it2][*it1])++;

  map<int,map<int,int>>::iterator mit;
    for(mit=mmap.begin();mit!=mmap.end();++mit){
      cout<<mit->first<<"={";
      display(mit->second);
      cout<<"}"<<endl;
    }

  return 0;
}*/

/*

#include <iostream>
#include <algorithm>

using namespace std;
const int MaxN = 102;

int main() {
  //利用链表散列进行统计，这里用二维数组模拟
  int m, n;
  while (cin >> m) {
    while (m--) {
      int Table[MaxN][MaxN] = {0}, classFlag[MaxN] = {false}, Class[MaxN], ClaN = 0, Num[MaxN], tmp, NumUi[MaxN], N = 0;
      cin >> n;
      for (int i = 0; i < n; ++i)
        cin >> Num[i];
      for (int i = 0; i < n; ++i) {
        cin >> tmp;
        if (!classFlag[tmp]) {
          classFlag[tmp] = true;
          Class[ClaN++] = tmp;
        }
        int aa=Table[tmp][MaxN - 1]++;
        printf("temp=%d---%d\n",tmp,aa);
        Table[tmp][aa] = Num[i];

      }
      sort(Class, Class + ClaN);
      sort(Num, Num + n);
      for (int i = 0; i < n; ++i) {
        if (N == 0 || Num[i] != NumUi[N - 1]) {
          NumUi[N++] = Num[i];
        }
      }

      for (int k = 0; k < ClaN; ++k) {
        printf("%d={", Class[k]);
        for (int h = 0; h < N; ++h) {
          int c = 0, j = 0;

          int aaaa=Table[Class[k]][MaxN - 1];
          printf("\nClass[k]=%d，MaxN - 1=%d,aaaa=%d\n\n",Class[k],MaxN - 1,aaaa);

          for (; j <aaaa ; ++j) {
            if (Table[Class[k]][j] == NumUi[h])
              ++c;
          }
          printf("%d=%d", NumUi[h], c);
          if (h < N - 1)
            printf(",");
        }
        printf("}\n");
      }

    }
  }


  return 0;
}

*/

/*


#include<stdio.h>
#include<map>
#include<set>
#include<vector>
using namespace std;
int main()
{
    int m;
    scanf("%d",&m);
    while(m--)
    {
        int n,an[105]={0},ans[105][105]={0};
        set<int> s,g;
        map<int,int> mp,group;
        scanf("%d",&n);
        for(int i=1;i<=n;i++)
        {
            scanf("%d",&an[i]);
            if(mp[an[i]])
                continue;
            else
            {
                mp[an[i]]=i;
                s.insert(an[i]);
            }
        }
        for(int i=1;i<=n;i++)
        {
            int a;
            scanf("%d",&a);
            if(!group[a])
            {
                g.insert(a);
                group[a]=i;
            }
            ans[group[a]][mp[an[i]]]++;
        }
        set<int>::iterator it;
        for(it=g.begin();it!=g.end();it++)
        {
            printf("%d={",*it);
            set<int>::iterator in;
            for(in=s.begin();in!=s.end();in++)
            {
                if(in!=s.begin())
                    putchar(',');
                printf("%d=%d",*in,ans[group[*it]][mp[*in]]);
            }
            printf("}\n");
        }
    }
    return 0;
}
*/
/*
#include <cstdio>
#include <set>
using namespace std;
int main(){
  int n;
  while(scanf("%d",&n)!=EOF){
    while (n--){
      int m,num[110],group[110],TableHash[200][1010]={0},row=1;
      set<int> uniNum,uniGroup;
      scanf("%d",&m);
      for(int i=0;i<m;i++){
        scanf("%d",&num[i]);
        uniNum.insert(num[i]);
      }
      for(int i=0;i<m;i++){
        scanf("%d",&group[i]);
        uniGroup.insert(group[i]);
      }
      for(int i=0;i<m;i++){
        TableHash[group[i]][num[i]]++;
      }
      set<int>::iterator it1,it2;
      for(it1=uniGroup.begin();it1!=uniGroup.end();it1++){
        printf("%d={",*it1);
        int c=1;
        for(it2=uniNum.begin();it2!=uniNum.end();it2++){
          printf("%d=%d",*it2,TableHash[*it1][*it2]);
          if(c++<uniNum.size()) printf(",");
        }
        printf("}");
        if(row++<uniGroup.size()) printf("\n");

      }
    }
  }
  return 0;
}*/

#include <cstdio>
#include <map>
#include <set>
#include <vector>
using namespace std;
int main(){
  int n;
  while (scanf("%d",&n)){
    while(n--){
      int m,temp,c=1;
      set<int> g;
      scanf("%d",&m);
      vector<int> num,group;
      map<int,map<int,int>> mmap;
      for(int i=0;i<m;i++){
        scanf("%d",&temp);
        num.push_back(temp);
      }
      for(int i=0;i<m;i++){
        scanf("%d",&temp);
        group.push_back(temp);
        g.insert(temp);
      }

      vector<int>::iterator it1,it2;
      for(it1=group.begin();it1!=group.end();it1++){
        for(it2=num.begin();it2!=num.end();it2++){
          mmap[*it1][*it2]=0;
        }
      }
      for(it1=group.begin(),it2=num.begin();it1!=group.end(),it2!=num.end();it1++,it2++){
        mmap[*it1][*it2]++;
      }
      map<int,map<int,int>>::iterator mit;
      for(mit=mmap.begin();mit!=mmap.end();mit++){
        printf("%d={",mit->first);
        map<int,int>::iterator git;
        for(git=mit->second.begin();git!=mit->second.end();git++){
          if(git!=mit->second.begin()) printf(",");
          printf("%d=%d",git->first,git->second);
        }
        printf("}");
        if((c++)<g.size()) printf("\n");
      }
    }
  }
  return 0;
}