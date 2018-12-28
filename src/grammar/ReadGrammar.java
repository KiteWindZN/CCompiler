/*
 * 从文件里读取文法表达式，封装成链表的形式，供分析器调用
 * 同时记录文法中的所有非终结符
 * */
package grammar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReadGrammar {
    Set<String> nonT=new HashSet<String>(); //非终结符
    List<GrammarStruct> grammarSet=new ArrayList<GrammarStruct>();
    
	public void readFromFile(String fileName) throws IOException{
		
		BufferedReader bf=new BufferedReader(new FileReader(fileName));
		String line=bf.readLine();
		while(line!=null){
			GrammarStruct g=new GrammarStruct();
			String[] strs=line.split("->");
			g.left=strs[0];
			List<String> right=new ArrayList<String>();
			String[] rightStrs=strs[1].split("|");
			for(int i=0;i<rightStrs.length;i++){
				right.add(rightStrs[i]);
			}
			g.right=right;
			grammarSet.add(g);
			line=bf.readLine();
			nonT.add(strs[0]);//所有的非终结符都出现在文法的左侧
		}
		bf.close();
		
	}
}
