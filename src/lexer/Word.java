package lexer;
public class Word extends myToken{
	public final String lexeme;
	public Word(int t,String s){
		super(t);
		lexeme=new String(s);
	}
}
