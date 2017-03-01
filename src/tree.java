import java.util.*;

class node
{
	char ch;
	String code = "";
	int freq = 0;
	node l = null, r = null, p = null;
	
}

public class tree
{
	node root = new node();
	node NYT = root;
	
	void AddNewNode (char ch)
	{
		NYT.l = new node();
		NYT.r = new node();
		NYT.freq = 1;
		NYT.r.freq = 1;
		NYT.l.p = NYT;
		NYT.r.p = NYT;
		NYT.l.code = NYT.code+'0';
		NYT.r.code = NYT.code+'1';
		NYT.r.ch = ch;
		
		if(NYT!=root) 
			check(NYT.p);
		
		NYT = NYT.l;
	}
	
	int getChar (node cur, String code)
	{
		if(cur==null)
			return 0;
		
		if(cur == NYT && code.equals(cur.code))
			return -1;
		
		if(code.equals(cur.code))
			return (int)cur.ch;
		
		return getChar(cur.l,code)+getChar(cur.r,code);
	}
	
	String getCode (node cur, char ch)
	{
		if(cur==null || cur==NYT)
			return "";
		
		if(cur.l==null && cur.r==null && cur.ch==ch)
			return cur.code;
		
		return getCode(cur.l,ch)+getCode(cur.r,ch);
		
	}
	
	void UpdateExistNode (node cur , String code)
	{
		if(code=="")
		{
			check(cur);
			return ;
		}
		
		if(code.charAt(0)=='0')
			cur=cur.l;
		else
			cur=cur.r;
		
		UpdateExistNode (cur,code.length()>1 ? code.substring(1) : "");
	}
		
	void check(node cur)
	{		
		CheckSwap(cur);
		cur.freq++;

		if(cur == root)
			return;
		
		check(cur.p);
	}
	
	void CheckSwap (node cur)
	{
		LinkedList  <node> q = new LinkedList <node>();
		
		q.add(root);
		
		while (q.peek()!=cur)
		{
			if(q.peek()!=root && q.peek()!=cur.p && q.peek().freq<=cur.freq)
			{
				swap(q.peek(),cur);
				CalCode(root,"");
				return ;
			}
			
			else
			{
				if(q.peek().r != null)
					q.add(q.peek().r);
				
				if(q.peek().l != null)
					q.add(q.peek().l);
				
				q.poll();
			}
		}		
	}
	
	void swap (node x, node y)
	{		
		if (x.p.r == x)
		{			
			if (y.p.r == y)
				y.p.r = x;
			else
				y.p.l = x;
			
			x.p.r = y;
		}
			
		
		else
		{			
			if (y.p.r == y)
				y.p.r = x;
			else
				y.p.l = x;
			
			x.p.l = y;
		}
				
		node tmp = x.p;
		x.p = y.p;
		y.p = tmp;
	}
	
	void CalCode (node cur, String code)
	{
		if(cur==null)
			return;
		
		cur.code=code;
		CalCode(cur.l,code+'0');
		CalCode(cur.r,code+'1');
	}	
}
