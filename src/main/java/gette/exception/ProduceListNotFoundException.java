package gette.exception;

public class ProduceListNotFoundException extends RuntimeException {
    public ProduceListNotFoundException(){
        super("농산물 목록을 찾을 수 없습니다.");
    }
}
