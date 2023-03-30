package gette.exception;

public class ProducePriceNotFoundException extends RuntimeException {
    public ProducePriceNotFoundException(){
        super("토큰이 없거나 지정된 이름에 해당하는 정보가 없습니다.");
    }
}
