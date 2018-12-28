/*
 * 语法分析器的数据结构，包含语法的左值和右值
 * */
package grammar;

import java.util.List;

public class GrammarStruct {
	String left;
	List<String> right;
	
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public List<String> getRight() {
		return right;
	}
	public void setRight(List<String> right) {
		this.right = right;
	}
	
}
