#include<cstdio>
#include<iostream>
#include<stack>
#include<string>
#include<queue>
#include<map>

using namespace std;

struct node{
	double num;
	char op;
	bool flag;
};

string str;+
stack<node> s;
queue<node> q;//后缀表达式序列
map<char,int>  op;

void Change(){//将中缀表达式转为后缀表达式
	double num;
	node temp;
	for(int i=0;i<str.length();i++){
		if(str[i]>='0'&&str[i]<='9'){
			temp.flag = true;
			temp.num=str[i++]-'0';
			while(i<str.length()&&str[i]>='0'&&str[i]<='9'){
				temp.num=temp.num*10+(str[i]-'0');
				i++;
			}
			q.push(temp);
		}else{//如果是操作符
			temp.flag=false;
			//只要操作符栈的栈顶元素比该操作符优先级高，就把操作符栈顶元素弹出到后缀表达式队列中
			while(!s.empty()&&op[str[i]]<=op[s.top().op]){
				q.push(s.top());
				s.pop();
			}
			temp.op=str[i];
			s.push(temp);
			i++;
		}
	}

	while(!s.empty()){
		q.push(s.top());
		s.pop();
	}

}

double Cal(){
	double temp1,temp2;
	node cur,temp;
	while(!q.empty()){
		cur = q.front();
		printf("%lf",cur.num);
		q.pop();
		if(cur.flag==true){
			s.push(cur);
		}else{
			printf("%c",cur.op);
			temp2=s.top().num;
			s.pop();
			temp1=s.top().num;
			s.pop();
			temp.flag=true;
			if(cur.op=='+'){
				temp.num=temp1+temp2;
			}else if(cur.op=='-'){
				temp.num=temp1-temp2;
			}else if(cur.op=='*'){
				temp.num=temp1*temp2;
			}else{
				temp.num=temp1/temp2;
			}
			s.push(temp);
		}

	}
	printf("\n");
	return s.top().num;
}

int main(){
	op['+']=op['-']=1;
	op['*']=op['/']=2;
	while(getline(cin,str),str!="0"){
		for(string::iterator it = str.end();it!=str.begin();it--){
			if(*it==' '){
				str.erase(it);
			}
		}
		while(!s.empty()){
			s.pop();//初始化栈
		}
		Change();
		printf("%.2f\n",Cal());
	}
	return 0;
}
