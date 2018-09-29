package lexer;

public class Decimal extends myToken{
	float value=0;
	Decimal(float value2){
		super(Tag.Decimal);
		value=value2;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	
}
