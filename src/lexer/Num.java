package lexer;

public class Num extends myToken {
	public final int value;
	public Num(int v){
		super(Tag.NUM);
		value=v;
	}
	public int getValue() {
		return value;
	}
	
}
