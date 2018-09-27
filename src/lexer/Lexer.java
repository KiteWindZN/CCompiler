package lexer;

import java.util.HashMap;
import java.util.Map;

public class Lexer {

	private Map<String,myToken> words=new HashMap<String,myToken>();
	public void reserve(Word t){
		words.put(t.lexeme, t);
	}
	public Lexer(){
		reserve(new Word(Tag.TRUE,"true"));
		reserve(new Word(Tag.FALSE,"false"));
		reserve(new Word(Tag.INT,"int"));
		reserve(new Word(Tag.FLOAT,"float"));
		reserve(new Word(Tag.DOUBLE,"double"));
		reserve(new Word(Tag.LONG,"long"));
		reserve(new Word(Tag.STRING,"string"));
		reserve(new Word(Tag.CHAR,"char"));
		reserve(new Word(Tag.IF,"if"));
		reserve(new Word(Tag.ELSE,"else"));
		reserve(new Word(Tag.FOR,"for"));
		reserve(new Word(Tag.DO,"do"));
		reserve(new Word(Tag.WHILE,"while"));
		reserve(new Word(Tag.CONTINUE,"continue"));
		reserve(new Word(Tag.BREAK,"break"));
		reserve(new Word(Tag.SWITCH,"switch"));
		reserve(new Word(Tag.CASE,"case"));
		reserve(new Word(Tag.DEFAULT,"default"));
		reserve(new Word(Tag.RETURN,"return"));
		reserve(new Word(Tag.MAIN,"main"));
		reserve(new Word(Tag.VOID,"void"));
		reserve(new Word(Tag.DEFINE,"define"));
		reserve(new Word(Tag.SIZEOF,"sizeof"));
		reserve(new Word(Tag.TYPEOF,"typeof"));
		reserve(new Word(Tag.INCLUDE,"include"));
	}
	
	//双缓存读取的实现
	//
	//双引号和单引号里面的内容如何识别
	//注释如何识别
	//符号表怎么创建
}
