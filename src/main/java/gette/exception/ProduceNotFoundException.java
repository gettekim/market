package gette.exception;

public class ProduceNotFoundException extends RuntimeException{
    public ProduceNotFoundException(){
        super("유효하지 않은 농산물 입니다.");
    }
}
