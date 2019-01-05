package test;

import java.io.IOException;

import lexer.Cchar;
import lexer.Cstring;
import lexer.Decimal;
import lexer.Lexer;
import lexer.Num;
import lexer.Word;
import lexer.myToken;

public class TestLexer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Lexer lexer=new Lexer("program.c",4096);
		myToken token;
		token=lexer.get_nextToken();
		while(token!=null){
			int tag=token.tag;
			if(tag==0){
				//error token
			}else if(tag>0&&tag<257){//char
				System.out.println("< "+token.tag+" , "+(char)token.tag+" >");
			}else if(tag<310){
				Word word=(Word)token;
				System.out.println("< "+word.tag+" , "+word.lexeme+" >");
			}else{
				if(tag==310){
					Num num=(Num)(token);
					System.out.println("< "+num.tag+" , "+num.value+" >");
				}else if(tag==311){
					Decimal decimal=(Decimal)token;
					System.out.println("< "+decimal.tag+" , "+decimal.getValue()+" >");
				}else if(tag==312){
					Cstring cstring=(Cstring)token;
					System.out.println("< "+cstring.tag+" , "+cstring.getValue()+" >");
				}else if(tag==313){
					Cchar cchar=(Cchar)token;
					System.out.println("< "+cchar.tag+" , "+cchar.getValue()+" >");
				}
			}
			token=lexer.get_nextToken();
		}
	}

}
