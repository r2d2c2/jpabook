package hello.jpabook.exception;

public class NotEnoughStockException extends RuntimeException{
    //상속 받은 기능을 제정의 컨트롤+o
    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }


}
