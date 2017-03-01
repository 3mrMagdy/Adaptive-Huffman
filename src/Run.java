import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Run 
{
	public static void main (String arg[])
	{
		new Run().Gui(); 
	}
	
	void Gui()
	{
		JFrame frm = new JFrame ("LZW");
		frm.setSize(500,300);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel pnl = new JPanel();
		frm.add(pnl);
		
		pnl.setLayout(null);
		
		JLabel l = new JLabel("File path");
		l.setBounds(211, 51, 71, 31);
		pnl.add(l);
		
		JTextField tf = new JTextField();
		tf.setBounds(99, 91, 277, 33);
		pnl.add(tf);
		
		JButton b1 = new JButton("Compress");
		b1.setBounds(33, 177, 111, 51);
		pnl.add(b1);
		
		JButton b2 = new JButton("Decompress");
		b2.setBounds(333, 177, 111, 51);
		pnl.add(b2);
		
		b1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Comp(tf.getText());
				JOptionPane.showMessageDialog(null, "Done!!");
			}
		});
		
		b2.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent arg0)
			{
				Decomp(tf.getText());
				JOptionPane.showMessageDialog(null, "Done!!");
			}
		});

		frm.setVisible(true);
		
	}
	
	void Comp (String path)
	{
		String doc = new Run().ReadFromFile(path);
		tree AdptHuffman = new tree();
		String shortCode[] = genCode();
		String comdoc="", code;
		Map <Character,Boolean> is = new HashMap <Character,Boolean> ();
		
		for(int i=0 ; i<doc.length() ; i++)
		{
			if (is.containsKey(doc.charAt(i)))
			{
				code = AdptHuffman.getCode(AdptHuffman.root, doc.charAt(i));
				comdoc += code;
				AdptHuffman.UpdateExistNode(AdptHuffman.root, code);
			}
			
			else
			{
				is.put(doc.charAt(i), true);
				comdoc += AdptHuffman.NYT.code + shortCode[(int)doc.charAt(i)];
				AdptHuffman.AddNewNode(doc.charAt(i));
			}
		}
		
		new Run().writeToFile(comdoc,"comFile.txt");
		
	}
	
	String [] genCode ()
	{
		String shortCode [] = new String [128];
		
		for (int i=0 ; i<128 ; i++)
		{
			shortCode[i] = "";
			
			for(int j=i ; j>0 ; j = (j>>1))
				shortCode[i] = (char)((j&1)+'0') + shortCode[i];
			
			while (shortCode[i].length()!=7)
				shortCode[i] = '0' + shortCode[i];	
		}
		
		return shortCode;
	}
	
	String ReadFromFile (String path)
	{
		String doc="", s;
		
		try
		{
			Scanner in = new Scanner (new File(path));
			
			s=in.nextLine();
			doc+=s;

			while (in.hasNextLine())
			{
				s=in.nextLine();
				doc+= '\n' + s;
			}
			
			in.close();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Invalid path");
		}
		
		return doc;
	}

	void writeToFile (String comdoc, String name)
	{
		File comFile = new File (name);
		
		try
		{
			comFile.createNewFile();
			
			FileWriter writer= new FileWriter(comFile);
			
			writer.write(comdoc);
			
			writer.close();
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Error");
		}
	}

	void Decomp (String path)
	{
		String doc = new Run().ReadFromFile(path);
		tree AdptHuffman = new tree();
		String shortCode[] = genCode();
		String datadoc = "", code = "";
		int check=-1;
		char ch;
		Map <String,Character> is = new HashMap <String,Character> ();
		
		for(int i=0 ; i<128 ; i++)
			is.put(shortCode[i], (char)i);
						
		for(int i=-1 ; i<doc.length() ; i++)
		{
			if(i!=-1)
			{
				code +=doc.charAt(i);
				check = AdptHuffman.getChar(AdptHuffman.root, code);
			}
			if (check == -1)
			{
				code = "";
				
				for(i++ ; !is.containsKey(code) ; code+=doc.charAt(i),i++);
				ch=is.get(code);
				datadoc += ch;
				AdptHuffman.AddNewNode(ch);
				code = "";	i--;				
			}
			
			if(check>0)
			{
				code = "";
				datadoc += (char)check;
				AdptHuffman.UpdateExistNode
				(AdptHuffman.root, AdptHuffman.getCode(AdptHuffman.root, (char)check));
			}
		}
		
		new Run().writeToFile(datadoc,"newData.txt");
	}
}
// E:\WorkSpace\WorkSpace_J\Adptive-Huffman\data.txt
// E:\WorkSpace\WorkSpace_J\Adptive-Huffman\comFile.txt