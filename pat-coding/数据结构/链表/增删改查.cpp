#include<cstdio>

struct node{
	int data;
	node* next;
};
node* Create(int Array[]){
	node *p,*pre,*head;
	head = new node;
	head->next=NULL;
	pre=head;
	for(int i=0;i<5;i++){
		p= new node;
		p->data=Array[i];
		p->next=NULL;
		pre->next=p;
		pre = p;
	}
	return head;
}

int search(node* head,int x){
	int count=0;
	node* p = head->next;
	while(p!=NULL){
		if(p->data==x){
			count++;
		}
		p=p->next;
	}
	return count;
}

void insert(node* head,int pos,int x){
	node* p = head;
	for(int i=0;i<pos-1;i++){
		p=p->next;
	}
	node* q=new node;
	q->data=x;
	q->next=p->next;
	p->next=q;
}

void delete(node* head,int x){
	node* p = head->next;
	node* pre = head;
	while(p!=NULL){
		if(p->data==x){
			pre->next=p->next;
			delete(p);
			p=pre->next;
		}else{
			pre=p;
			p=p->next;
		}
	}
}


int main(){
	int Array[5]={5,3,6,1,2};
	node* L=Create(Array);
	insert(L,3,9);
	L=L->next;
	while(L!=NULL){
		printf("%d",L->data);
		L=L->next;
	}

//	printf("%d\n",search(L,3));

	return 0;
}