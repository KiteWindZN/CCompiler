import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexical {
	List<String> keyWords = new ArrayList<String>();
	List<String> operators = new ArrayList<String>();
	List<String> program = new ArrayList<String>();

	int row = 0;
	int col = 0;

	public void createList(String fileName, List<String> list) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(fileName));
		String line = bf.readLine();
		while (line != null) {
			
			list.add(line);
			line = bf.readLine();
		}
		bf.close();
	}

	public Token next_token() throws IOException{
		if(row>=program.size()||(row==program.size()-1&&col==program.get(row).length()))
			return null;
		Token token=new Token();
		String str=program.get(row);
		while(str.trim().length()==0&&row<program.size()-1){
			row++;
			str=program.get(row);
		}
		
		if(str.trim().length()==0)
			return null;
		
		int len=str.length();
		char ch=str.charAt(col++);
		
		while(ch==' '&&col<len){
			ch=str.charAt(col++);
		}
		int start=col-1;
		StringBuilder value=new StringBuilder();
		int flag=0;
		if(ch>='0'&&ch<='9'){//maybe number
			flag=1;
			int point=0;
			while(operators.indexOf(ch+"")==-1&&col<len){
				value.append(ch);
				if(ch=='.'){
					point++;
				}
				if(!(ch>='0'&&ch<='9')&&ch!='.'){
					flag=3;
				}
				if(ch=='.'&&point>1)
					flag=3;
				ch=str.charAt(col++);
			}
			if(flag==3){//wrong number
				warning(row,start,value.toString());
				col--;
				return token;
			}
		}else if(ch=='_'||(ch>='a'&&ch<='z')||(ch>='A'&&ch<='Z')){
			flag=2;
			while(operators.indexOf(ch+"")==-1&&col<len){
				//
				if((ch=='_'||(ch>='a'&&ch<='z')||(ch>='A'&&ch<='Z')||(ch>='0'&&ch<='9'))&&col<len){
				    value.append(ch);
				    ch=str.charAt(col++);
				}else{
					flag=4;
					value.append(ch);
				    ch=str.charAt(col++);
				}
			}
			if(flag==4){//wrong keyword or symbol
				warning(row,col,value.toString());
				col--;
				return token;
			}
		}
		
		if(flag==1){//right number
			if(value.length()>0){
			    token.setValue(value.toString());
			    token.setTokenNum(keyWords.indexOf("number"));
			    if(col==len&&operators.indexOf(ch+"")==-1){
					row++;
					col=0;
				}else col--;
			    return token;
			}
		}else if(flag==2){//right key word or symbol
			if(value.length()>0){
			    token.setValue(value.toString());
			    int tmp=keyWords.indexOf(value.toString());
			    if(tmp!=-1){
				    token.setTokenNum(tmp);
			    }else token.setTokenNum(keyWords.indexOf("symbol"));
			    if(col==len&&operators.indexOf(ch+"")==-1){
					row++;
					col=0;
				}else col--;
			    return token;
			}
		}
		
		value.delete(0, value.length());
		//判断下一个符号是什么
		int tmp=0;
		switch(ch){
		case ' ':
			break;
		case '#':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '+':
			value.append(ch);
			if(col<len)
				ch=str.charAt(col);
			if(ch=='+'){
				value.append(ch);
				col++;
			}
			
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '-':
			value.append(ch);
			if(col<len)
				ch=str.charAt(col);
			if(ch=='-'){
				value.append(ch);
				col++;
			}
			
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '*':
			value.append(ch);
			if(col<len)
				ch=str.charAt(col);
			if(ch=='*'){
				value.append(ch);
				col++;
			}
			
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '/':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '%':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '\''://考虑如何识别‘X’包含的内容
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '"'://考虑如何识别“XXX”包含的内容
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '=':
			value.append(ch);
			if(col<len)
				ch=str.charAt(col);
			if(ch=='='){
				value.append(ch);
				col++;
			}
			
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '&':
			value.append(ch);
			if(col<len){
				ch=str.charAt(col);
			}else{
				warning(row,start,value.toString());
			}
			if(ch=='&'){
				value.append(ch);
				col++;
				token.setValue(value.toString());
			    tmp=keyWords.indexOf(value.toString());
			    token.setTokenNum(tmp);
			}else{
				warning(row,start,value.toString());
			}
			break;
		case '|':
			value.append(ch);
			if(col<len){
				ch=str.charAt(col);
			}
			if(ch=='|'){
				value.append(ch);
				col++;
				token.setValue(value.toString());
			    tmp=keyWords.indexOf(value.toString());
			    token.setTokenNum(tmp);
			}else{
				warning(row,start,value.toString());
			}
			break;
		case '!':
			value.append(ch);
			if(col<len){
				ch=str.charAt(col);
			}
			if(ch=='='){
				value.append(ch);
				col++;
			}
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case ',':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case ';':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '(':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case ')':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '[':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case ']':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '{':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '}':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '<':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '>':
			value.append(ch);
			token.setValue(value.toString());
		    tmp=keyWords.indexOf(value.toString());
		    token.setTokenNum(tmp);
			break;
		case '@':
			warning(row,start,"@");
			
			break;
		case '$':
			warning(row,start,"$");
			break;
		default:
				break;
		}
		if(col==len){
			row++;
			col=0;
		}
		return token;
	}

	public void warning(int row, int col, String word) throws IOException {
		System.out.println("error: row: "+row+", col: "+col+", "+word+" is not a right number or keyword or symbol.");
		//throw new IOException("error: row: "+row+", col: "+col+", "+word+" is not a right number or keyword or symbol.");
	}

	public void printToken(Token token){
		System.out.println("< "+token.tokenNum+" , "+token.value+" >");
	}
	
	public void printProgram(){
		for(int i=0;i<program.size();i++){
			System.out.println(program.get(i));
		}
		System.out.println();
	}
	public static void main(String[] args) throws IOException{
		Lexical myObj=new Lexical();
		
		myObj.createList("keyWord.txt", myObj.keyWords);
		myObj.createList("program.c", myObj.program);
		myObj.createList("seperator.txt",myObj.operators);
		
		myObj.printProgram();
		
		Token token=new Token();
		token=myObj.next_token();
		while(token!=null){
			if(token.value!=null&&token.value.length()>0)
			    myObj.printToken(token);
			token=myObj.next_token();
		}
		System.out.println();
		System.out.println("Success");
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
}
