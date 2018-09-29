package lexer;

public class Cchar extends myToken{
	char value='0';
	Cchar(char val){
		super(Tag.CCHAR);
		value=val;
	}
	public char getValue() {
		return value;
	}
	public void setValue(char value) {
		this.value = value;
	}
	
	
}
