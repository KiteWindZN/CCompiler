package lexer;

public class Cstring extends myToken{
	String value="";
	Cstring(String val){
		super(Tag.CSTRING);
		value=val;
	}
}
