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

	Map<String,Map<String,GrammarStruct>> mTable=new HashMap<String,Map<String,GrammarStruct>>();
	Map<String,List<String>> firstSet=new HashMap<String,List<String>>();
	Map<String,List<String>> followSet=new HashMap<String,List<String>>();
	List<GrammarStruct> grammarSet=new ArrayList<GrammarStruct>();
	Set<String> nonT=new HashSet<String>();
	
	GrammarAnalysis(String fileName) throws IOException{
		ReadGrammar readGrammar=new ReadGrammar();
		readGrammar.readFromFile(fileName);
		grammarSet=readGrammar.grammarSet;
		nonT=readGrammar.nonT;
		
		for(String v: nonT){
			firstSet.put(v, new ArrayList<String>());
			followSet.put(v, new ArrayList<String>());
		}
	}
	
	
	public Map<String, Map<String, GrammarStruct>> getmTable() {
		return mTable;
	}
	public void setmTable(Map<String, Map<String, GrammarStruct>> mTable) {
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
	public List<GrammarStruct> getGrammarSet() {
		return grammarSet;
	}
	public void setGrammarSet(List<GrammarStruct> grammarSet) {
		this.grammarSet = grammarSet;
	}
	
}
