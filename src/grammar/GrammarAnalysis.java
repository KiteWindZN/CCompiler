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

public class GrammarAnalysis {

	Map<String, Map<String, GrammarStruct>> mTable = new HashMap<String, Map<String, GrammarStruct>>();
	Map<String, List<String>> firstSet = new HashMap<String, List<String>>();
	Map<String, List<String>> followSet = new HashMap<String, List<String>>();
	Map<String, GrammarStruct> grammarMap = new HashMap<String, GrammarStruct>();
	Set<String> nonT = new HashSet<String>();

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
			//将新产生的非终结符加入非终结符集合nonT
			nonT.add(Vii);
		}
	}

	// 提取左公因子
	public void extractLeftCommonFactor() {

	}

	// 求first集合
	public void calculateFirstSet() {
		int flag = 1;
		while (flag == 1) {
			flag = 0;
			for (String V : nonT) {
				GrammarStruct gs = grammarMap.get(V);
				List<String> gsRight = gs.right;
				int startLength=firstSet.get(V).size();
				
				for (int i = 0; i < gsRight.size(); i++) {
					String str = gsRight.get(i);
					String[] strs = str.split(" ");
					if (!nonT.contains(strs[0])) {
						firstSet.get(V).add(strs[0]);
					} else {
						int flag1=0;
						for (int j = 0; j < strs.length; j++) {
							
							String tmpS = strs[j];
							if(!nonT.contains(tmpS)){
								flag1=1;
								break;
							}
							//@代表空值
							if(firstSet.get(tmpS).contains("@")){
								for(String s:firstSet.get(tmpS)){
									if(s.equals("@"))
										continue;
									firstSet.get(V).add(s);
								}
								continue;
							}else{
								flag1=1;
								break;
							}
						}
						if(flag1==0){
							firstSet.get(V).add("@");
						}
					}
				}
				if(startLength<firstSet.get(V).size()){
					flag=1;
				}
			}
		}
	}

	// 求follow集合
	public void calculateFollowSet() {

	}
	//产生LL1语法分析表
	public void createMTable(){
		
	}
	//分析具体的程序
	public void analysisProgram(){
		
	}
}
