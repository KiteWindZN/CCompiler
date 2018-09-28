package lexer;

public class Decimal extends myToken{
	float value=0;
	Decimal(float value2){
		super(Tag.Decimal);
		value=value2;
	}
}
