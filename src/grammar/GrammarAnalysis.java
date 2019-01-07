/*
 * 语法分析器的核心代码，
 * 包括消除左递归，求First，Follow集合
 * 构造分析表等模块。
 * 错误处理暂时先不考虑，等待后续添加
 */
package grammar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import lexer.Cchar;
import lexer.Cstring;
import lexer.Decimal;
import lexer.Lexer;
import lexer.Num;
import lexer.Word;
import lexer.myToken;

public class GrammarAnalysis {

	Map<MtableKey, GrammarSingle> mTable = new HashMap<MtableKey, GrammarSingle>();
	Map<String, List<String>> firstSet = new HashMap<String, List<String>>();
	Map<String, List<String>> followSet = new HashMap<String, List<String>>();
	Map<String, GrammarStruct> grammarMap = new HashMap<String, GrammarStruct>();
	Stack<String> stack;
	Set<String> nonT = new HashSet<String>();// 非终结符集合
	// Set<String> T=new HashSet<String>();//终结符集合
	Lexer lexer = new Lexer("program.c", 4096);

	GrammarAnalysis(String fileName) throws IOException {
		ReadGrammar readGrammar = new ReadGrammar();
		readGrammar.readFromFile(fileName);
		grammarMap = readGrammar.grammarMap;
		nonT = readGrammar.nonT;

		for (String v : nonT) {
			firstSet.put(v, new ArrayList<String>());
			followSet.put(v, new ArrayList<String>());
		}
	}

	// 消除左递归
	public void avoidLeftRecursion() {
		List<String> v = new ArrayList<String>();
		for (String str : nonT) {
			v.add(str);
		}
		for (int i = 0; i < v.size(); i++) {
			String Vi = v.get(i);
			GrammarStruct gsi = grammarMap.get(Vi);
			List<String> gRight = gsi.right;
			// 消除Vi中的间接左递归
			for (int j = 0; j < i; j++) {
				String Vj = v.get(j);
				for (int h = 0; h < gRight.size(); h++) {
					String tmpS = gRight.get(h);
					String[] strs1 = tmpS.split(" ");
					for (int k = 0; k < strs1.length; k++) {
						if (strs1[k].equals(Vi)) {
							gRight.remove(h);
							i--;
							// add Vj
							GrammarStruct gsj = grammarMap.get(Vj);
							List<String> gRightJ = gsj.right;
							for (int m = 0; m < gRightJ.size(); m++) {
								String tmpS1 = tmpS;
								gRight.add(tmpS1.replace(Vj, gRightJ.get(m)));
							}
						}
					}
				}
			}

			// 消除Vi中的直接左递归
			List<String> listHas = new ArrayList<String>();
			List<String> listNotHas = new ArrayList<String>();
			int flag = 0;
			for (int j = 0; j < gRight.size(); j++) {
				String str = gRight.get(j);
				if (str.trim().startsWith(Vi)) {
					flag = 1;
					listHas.add(str.trim().substring(Vi.length()));
				} else {
					listNotHas.add(str);
				}
			}
			if (flag == 0)
				continue;
			// 修改带有左递归的非终结符的文法
			gRight.clear();
			String Vii = Vi + "1";
			for (int j = 0; j < listNotHas.size(); j++) {
				gRight.add(listNotHas.get(j).trim() + " " + Vii);
			}
			gsi.right = gRight;
			grammarMap.put(Vi, gsi);
			// 添加新增的非终结符gsii
			GrammarStruct gsii = new GrammarStruct();
			gsii.left = Vii;
			List<String> gsiiRight = new ArrayList<String>();
			for (int j = 0; j < listHas.size(); j++) {
				gsiiRight.add(listHas.get(j).trim() + " " + Vii);
			}
			gsii.right = gsiiRight;
			grammarMap.put(Vii, gsii);
			// 将新产生的非终结符加入非终结符集合nonT
			nonT.add(Vii);
		}
	}

	// 提取左公因子
	public void extractLeftCommonFactor() {

		// step1. sort
		for (String V : nonT) {
			GrammarStruct gsv = grammarMap.get(V);
			List<String> gsvRight = gsv.right;
			sortList(gsvRight);
		}

		// step2. extract
		for (String V : nonT) {
			// longest common sub prefix
			GrammarStruct gsv = grammarMap.get(V);
			List<String> gsvRight = gsv.right;
			List<String> prefixList=new ArrayList<String>();
			List<String> lastList=new ArrayList<String>();
			int len=gsvRight.size();
			if(len==1)
				continue;
			while(len>1){
				int start=longestCommonPrefix(gsvRight, len, prefixList,lastList);
				if(start>0){//has common prefix
					String prefixS=prefixList.get(0);
					for(int i=1;i<prefixList.size();i++){
						prefixS+=" "+prefixList.get(i);
					}
					String newV=V+"1";
					
				}
				len--;
			}
			// change grammarMap.get(V)

			// add new v to nonT

			// add new GrammarStruct to grammarMap
		}
	}

	// fast sort
	public void sortList(List<String> list) {

	}

	public int longestCommonPrefix(List<String> list, int end,List<String> resList,List<String> lastList) {
		int start = 0;
		int len = Integer.MAX_VALUE;
		
		List<List<String>> lists = new ArrayList<List<String>>();
		for (int i = 0; i < end; i++) {
			String str = list.get(i);
			String[] strs = str.trim().split(" ");
			if (len > strs.length)
				len = strs.length;
			List<String> tmpL = new ArrayList<String>();
			for (String s : strs) {
				tmpL.add(s);
			}
			lists.add(tmpL);
		}

		int flag = 0;
		while (start < len && flag == 0) {
			String str = lists.get(0).get(start);
			for (int i = 1; i < lists.size(); i++) {
				if (lists.get(i).get(start).equals(str)) {
					continue;
				} else {
					flag = 1;
					break;
				}
			}
			if (flag == 0){
				resList.add(str);
				start++;
			}
		}
		return start;
	}

	// 求first集合
	public void calculateFirstSet() {
		int flag = 1;
		while (flag == 1) {
			flag = 0;
			for (String V : nonT) {
				GrammarStruct gs = grammarMap.get(V);
				List<String> gsRight = gs.right;
				int startLength = firstSet.get(V).size();

				for (int i = 0; i < gsRight.size(); i++) {
					String str = gsRight.get(i);
					String[] strs = str.split(" ");
					if (!nonT.contains(strs[0])) {
						firstSet.get(V).add(strs[0]);
						continue;
					} else {
						int flag1 = 0;
						for (int j = 0; j < strs.length; j++) {

							String tmpS = strs[j];
							if (!nonT.contains(tmpS)) {
								firstSet.get(V).add(tmpS);// 之前的非终结符都含有空值@
								flag1 = 1;
								break;
							}
							// @代表空值
							if (firstSet.get(tmpS).contains("@")) {
								for (String s : firstSet.get(tmpS)) {
									if (s.equals("@"))
										continue;
									firstSet.get(V).add(s);
								}
								continue;
							} else {
								flag1 = 1;
								break;
							}
						}
						if (flag1 == 0) {
							firstSet.get(V).add("@");
						}
					}
				}
				if (startLength < firstSet.get(V).size()) {
					flag = 1;
				}
			}
		}
	}

	// 求follow集合
	public void calculateFollowSet() {
		followSet.get("S").add("#");// S为起始符号
		for (String V : nonT) {
			GrammarStruct gsv = grammarMap.get(V);
			List<String> gsvRight = gsv.right;
			for (int i = 0; i < gsvRight.size(); i++) {
				String tmpRight = gsvRight.get(i);
				String[] strs = tmpRight.split(" ");
				int j = 0;
				for (; j < strs.length - 1; j++) {
					int flag1 = 0;
					String str1 = strs[j];
					int h = j + 1;
					if (!nonT.contains(str1))
						continue;
					while (flag1 == 0 && h < strs.length) {
						String str2 = strs[h];
						h++;
						if (nonT.contains(str2)) {
							if (!firstSet.get(str2).contains("@")) {
								followSet.get(str1).addAll(firstSet.get(str2));
								flag1 = 1;
							} else {
								for (String str3 : firstSet.get(str2)) {
									if (str3.equals("@")) {// 除去空值
										continue;
									}
									followSet.get(str1).add(str3);
								}
							}
						} else {
							followSet.get(str1).add(str2);
							flag1 = 1;
						}
					}
					if (flag1 == 0) {// 非终结符号str1后面全是非终结符，且他们的first集里面都含有空值@
						followSet.get(str1).addAll(followSet.get(V));
					}
				}
				if (nonT.contains(strs[j]))// 一条文法最后一个符号为非终结符
					followSet.get(strs[j]).addAll(followSet.get(V));
			}
		}
	}

	// 产生LL1语法分析表
	public void createMTable() {
		for (String V : nonT) {
			GrammarStruct gsv = grammarMap.get(V);
			List<String> gsvRight = gsv.right;

			for (String right : gsvRight) {
				String[] strs = right.split(" ");
				String str = strs[0];
				if (nonT.contains(str)) {
					for (String s : firstSet.get(str)) {
						GrammarSingle gs = new GrammarSingle();
						gs.left = V;
						gs.right = right;
						MtableKey mk = new MtableKey();
						mk.V = V;
						mk.T = s;
						mTable.put(mk, gs);
					}
					if (firstSet.get(str).contains("@")) {
						for (String s : followSet.get(str)) {
							GrammarSingle gs = new GrammarSingle();
							gs.left = V;
							gs.right = right;
							MtableKey mk = new MtableKey();
							mk.V = V;
							mk.T = s;
							mTable.put(mk, gs);
						}
					}
				} else {
					GrammarSingle gs = new GrammarSingle();
					gs.left = V;
					gs.right = right;
					MtableKey mk = new MtableKey();
					mk.V = V;
					mk.T = str;
					mTable.put(mk, gs);
				}
			}
		}
	}

	// 分析具体的程序
	public void analysisProgram() throws IOException {
		stack.push("#");
		stack.push("S");// start sign
		myToken token;
		token = lexer.get_nextToken();
		while (token != null) {
			String V = stack.pop();
			String T = "";
			MtableKey mk = new MtableKey();

			int tag = token.tag;
			if (tag > 0 && tag < 257) {// char
				System.out.println("< " + token.tag + " , " + (char) token.tag + " >");
				T = "" + (char) token.tag;
			} else if (tag < 310) {
				Word word = (Word) token;
				System.out.println("< " + word.tag + " , " + word.lexeme + " >");
				T = word.lexeme;
			} else {// constant
				if (tag == 310) {
					Num num = (Num) (token);// int
					// System.out.println("< "+num.tag+" , "+num.value+" >");
					T = "Num";
				} else if (tag == 311) {// float and double
					Decimal decimal = (Decimal) token;
					// System.out.println("< "+decimal.tag+" ,
					// "+decimal.getValue()+" >");
					T = "Num";
				} else if (tag == 312) {// String
					Cstring cstring = (Cstring) token;
					// System.out.println("< "+cstring.tag+" ,
					// "+cstring.getValue()+" >");
					T = "String";
				} else if (tag == 313) {// char
					Cchar cchar = (Cchar) token;
					// System.out.println("< "+cchar.tag+" ,
					// "+cchar.getValue()+" >");
					T = "Char";
				}
			}

			if (T.equals("#")) {
				if (stack.size() == 0) {
					if (V.equals("#")) {
						System.out.println("GrammarAnalysis SUCCESS");
						return;
					}
				}
			}

			mk.V = V;
			mk.T = T;
			if (mTable.containsKey(mk)) {
				GrammarSingle gs = mTable.get(mk);
				String gsRight = gs.right;
				String[] strs = gsRight.split(" ");
				if (strs.length == 1 || strs[0].equals("@"))
					continue;
				for (int i = strs.length - 1; i >= 0; i--) {
					stack.push(strs[i].trim());
				}
			} else {
				System.out.println("there must be something wrong in grammar, and the location is : row: "
						+ lexer.getRow() + " ,column: " + lexer.getCol());
				panicProcess();// error process wait to code
			}
		}
	}

	// error process
	public void panicProcess() {

	}

	public Map<MtableKey, GrammarSingle> getmTable() {
		return mTable;
	}

	public void setmTable(Map<MtableKey, GrammarSingle> mTable) {
		this.mTable = mTable;
	}

	public Map<String, List<String>> getFirstSet() {
		return firstSet;
	}

	public void setFirstSet(Map<String, List<String>> firstSet) {
		this.firstSet = firstSet;
	}

	public Map<String, List<String>> getFollowSet() {
		return followSet;
	}

	public void setFollowSet(Map<String, List<String>> followSet) {
		this.followSet = followSet;
	}

	public Map<String, GrammarStruct> getGrammarMap() {
		return grammarMap;
	}

	public void setGrammarMap(Map<String, GrammarStruct> grammarMap) {
		this.grammarMap = grammarMap;
	}

	public Stack<String> getStack() {
		return stack;
	}

	public void setStack(Stack<String> stack) {
		this.stack = stack;
	}

}
