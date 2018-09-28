package lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

	private Map<String, Word> words = new HashMap<String, Word>();
	List<Character> seperatorList = new ArrayList<Character>();
	int batchSize = 4096;
	byte[] cacheZone1;
	byte[] cacheZone2;
	int cacheNum = 0;
	String fileName = "program.c";
	ReadFile readFile;
	int row = 0;
	int col = 0;
	int index = 0;

	public void reserve(Word t) {
		words.put(t.lexeme, t);
	}

	public Lexer() throws IOException {
		reserve(new Word(Tag.TRUE, "true"));
		reserve(new Word(Tag.FALSE, "false"));
		reserve(new Word(Tag.INT, "int"));
		reserve(new Word(Tag.FLOAT, "float"));
		reserve(new Word(Tag.DOUBLE, "double"));
		reserve(new Word(Tag.LONG, "long"));
		reserve(new Word(Tag.STRING, "string"));
		reserve(new Word(Tag.CHAR, "char"));
		reserve(new Word(Tag.IF, "if"));
		reserve(new Word(Tag.ELSE, "else"));
		reserve(new Word(Tag.FOR, "for"));
		reserve(new Word(Tag.DO, "do"));
		reserve(new Word(Tag.WHILE, "while"));
		reserve(new Word(Tag.CONTINUE, "continue"));
		reserve(new Word(Tag.BREAK, "break"));
		reserve(new Word(Tag.SWITCH, "switch"));
		reserve(new Word(Tag.CASE, "case"));
		reserve(new Word(Tag.DEFAULT, "default"));
		reserve(new Word(Tag.RETURN, "return"));
		reserve(new Word(Tag.MAIN, "main"));
		reserve(new Word(Tag.VOID, "void"));
		reserve(new Word(Tag.DEFINE, "define"));
		reserve(new Word(Tag.SIZEOF, "sizeof"));
		reserve(new Word(Tag.TYPEOF, "typeof"));
		reserve(new Word(Tag.INCLUDE, "include"));
		reserve(new Word(Tag.STATIC, "static"));
		reserve(new Word(Tag.FINAL, "final"));
		reserve(new Word(Tag.CONST, "const"));
		
		seperatorList.add(',');
		seperatorList.add(';');
		seperatorList.add('=');
		seperatorList.add('<');
		seperatorList.add('>');
		seperatorList.add('!');
		seperatorList.add('&');
		seperatorList.add('|');
		seperatorList.add('(');
		seperatorList.add(')');
		seperatorList.add('[');
		seperatorList.add(']');
		seperatorList.add('{');
		seperatorList.add('}');
		seperatorList.add('+');
		seperatorList.add('-');
		seperatorList.add('*');
		seperatorList.add('/');
		seperatorList.add('%');

		readFile = new ReadFile(fileName, batchSize);
	}

	// 双缓存读取的实现
	public void loadCache() throws IOException {
		if (cacheNum == 0 || cacheNum == 2) {
			cacheZone1 = readFile.getNextBatch();
			cacheNum = 2;
		} else {
			cacheZone2 = readFile.getNextBatch();
			cacheNum = 1;
		}
		index = 0;
	}

	public myToken get_nextToken() throws IOException{
		
		
		byte peek=getNextChar();
		while((char)peek==' '||(char)peek=='\t'||(char)peek=='\n'){
			index++;
			col++;
			if((char)peek=='\n'){
				row++;
				col=0;
			}
			peek=getNextChar();
		}
		int startCol=col;
		if(peek>='0'&&peek<='9'){//Num
			int pointNum=0;
			float value=0;
			float decimal=0.1f;
			while(seperatorList.indexOf(peek)!=-1){
				if(peek==0)
				    return null;
				if(peek=='\n'){
					row++;
					col=0;
				}
				if(peek>='0'&&peek<='9'||(peek=='.')){
					if(peek=='.'){
						pointNum++;
						col++;
						peek=getNextChar();
						continue;
					}
				    if(pointNum==0)
					    value=value*10+(peek-'0');
				    if(pointNum==1){
				    	    value=value+(peek-'0')*decimal;
				    	    decimal*=0.1;
				    }
				    index++;
					col++;
					peek=getNextChar();
				}
				
			}
			if(pointNum==0){
				Num num=new Num((int)value);
				return num;
			}else if(pointNum==1){
				Decimal deObj=new Decimal(value);
				return deObj;
			}else{//error Num
				String errorMsg="wrong Number";
				processError(row,startCol,errorMsg);
				myToken nextToken=new myToken(1);
				return nextToken;
			}
		}else if((peek>='A'&&peek<='Z')||(peek>='a'&&peek<='z')||peek=='_'){//ID or keyWord
			String value="";
			int flag=1;
			while(seperatorList.indexOf(peek)!=-1){
				if(peek==0)
					return null;
				if(peek=='\n'){
					row++;
					col=0;
				}
				if((peek>='A'&&peek<='Z')||(peek>='a'&&peek<='z')||peek=='_'){
					;
				}else flag=0;
				value+=peek;
				index++;
				col++;
				peek=getNextChar();
			}
			
			if(flag==1){
				Word word=words.get(value);
				if(word!=null)
					return  word;
				word=new Word(Tag.ID,value);
				return word;
			}else{
				String errorMsg=value+ " : wrong keyWord or Symbol";
				processError(row,startCol,errorMsg);
				myToken nextToken=new myToken(1);
				return nextToken;
			}
		}else {
			myToken tokenObj;
			Word myWord;
			switch(peek){
			case '+':
				peek=getNextChar();
				if(peek=='+'){
					col++;
					myWord=new Word(Tag.INCREASE,"++");
					return myWord;
				}else{
					tokenObj=new myToken(peek);
					index--;
					return tokenObj;
				}
				
			case '-':
				peek=getNextChar();
				if(peek=='-'){
					col++;
					myWord=new Word(Tag.DECREASE,"--");
					return myWord;
				}else{
					tokenObj=new myToken(peek);
					index--;
					return tokenObj;
				}
			case '*':
				tokenObj=new myToken(peek);
				return tokenObj;
			case '/'://除法或者注释的开头
				peek=getNextChar();
				if(peek=='/'){
					while(peek!='\n'){
						peek=getNextChar();
					}
					tokenObj=new myToken(0);
					return tokenObj;
				}else if(peek=='*'){
					peek=getNextChar();
					byte nextC=getNextChar();
					while(peek!=0&&(peek!='*'||nextC!='/')){
						peek=nextC;
						nextC=getNextChar();
					}
					tokenObj=new myToken(0);
					return tokenObj;
				}else{
					tokenObj=new myToken(peek);
					index--;
					return tokenObj;
				}
			case '%':
				tokenObj=new myToken(peek);
				return tokenObj;
				
			case '(':
				tokenObj=new myToken(peek);
				return tokenObj;
			case ')':
				tokenObj=new myToken(peek);
				return tokenObj;
			case '[':
				tokenObj=new myToken(peek);
				return tokenObj;
			case ']':
				tokenObj=new myToken(peek);
				return tokenObj;
			case '{':
				tokenObj=new myToken(peek);
				return tokenObj;
			case '}':
				tokenObj=new myToken(peek);
				return tokenObj;
			case '&':
				peek=getNextChar();
				if(peek=='&'){
					col++;
					myWord=new Word(Tag.AND,"&&");
					return myWord;
				}else{
					tokenObj=new myToken(peek);
					index--;
					return tokenObj;
				}
			case '|':
				peek=getNextChar();
				if(peek=='|'){
					col++;
					myWord=new Word(Tag.OR,"||");
					return myWord;
				}else{
					tokenObj=new myToken(peek);
					index--;
					return tokenObj;
				}
			case '!':
				peek=getNextChar();
				if(peek=='='){
					col++;
					myWord=new Word(Tag.NOTEQUAL,"!=");
					return myWord;
				}else{
					tokenObj=new myToken(peek);
					index--;
					return tokenObj;
				}
			case '=':
				peek=getNextChar();
				if(peek=='='){
					col++;
					myWord=new Word(Tag.EQUAL,"==");
					return myWord;
				}else{
					tokenObj=new myToken(peek);
					index--;
					return tokenObj;
				}
			case '#':
				tokenObj=new myToken(peek);
				return tokenObj;
			case '<':
				peek=getNextChar();
				if(peek=='='){
					col++;
					myWord=new Word(Tag.NOTBIGGER,"<=");
					return myWord;
				}else{
					tokenObj=new myToken(peek);
					index--;
					return tokenObj;
				}
				
			case '>':
				peek=getNextChar();
				if(peek=='='){
					col++;
					myWord=new Word(Tag.NOTSMALLER,">=");
					return myWord;
				}else{
					tokenObj=new myToken(peek);
					index--;
					return tokenObj;
				}
				
			case '\"':
				String value="";
				peek=getNextChar();
				while(peek!=0&&peek!='\"'){
					value+=peek;
				}
				
				Cstring cstring=new Cstring(value);
				return cstring;
			case '\'':
				peek=getNextChar();
				if(peek=='\\')
					peek=getNextChar();
				byte nextChar=getNextChar();
				if(nextChar=='\''){
					Cchar cchar=new Cchar((char)peek);
					return cchar;
				}else {
					while(nextChar!='\'')
						nextChar=getNextChar();
					String errorMsg="in '' is not a valid character!";
					processError(row,startCol,errorMsg);
					tokenObj=new myToken(0);
					return tokenObj;
				}
			case ',':
				tokenObj=new myToken(peek);
				return tokenObj;
			case ';':
				tokenObj=new myToken(peek);
				return tokenObj;
			default:
				String errorMsg=peek+" : this character is not valid";
				processError(row,col,errorMsg);
				break;
			}
		}
		
		
		myToken nextToken=new myToken(0);
		return nextToken;
	}
	
	public byte getNextChar() throws IOException{
		byte peek=0;
		if(index==4096||peek==0){//the end of cacheZone
			if(index==4096){
				loadCache();
			}else{//end of File
				return peek;
			}
		}
		
		if(cacheNum==2){
			peek=cacheZone1[index];
		}else{
			peek=cacheZone2[index];
		}
		index++;
		return peek;
	}
	//
	// 双引号和单引号里面的内容如何识别,已实现√
	// 注释如何识别,已实现√
	// 符号表怎么创建
	
	//回退至上一个字符
	 public void rebackPreChar(){
		 if(index==0){
		 }else{
			 index--;
		 }
	 }
	//error
	public void processError(int rowNum,int colNum,String errorMsg){
		System.out.println("rowNum: "+rowNum+" , ColNum: "+colNum+" : "+errorMsg);
	}
}
