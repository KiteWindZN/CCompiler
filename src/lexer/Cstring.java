package lexer;

public class Cstring extends myToken{
	String value="";
	Cstring(String val){
		super(Tag.CSTRING);
		value=val;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
