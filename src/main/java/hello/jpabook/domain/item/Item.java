package hello.jpabook.domain.item;

import hello.jpabook.domain.Category;
import hello.jpabook.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity@Getter@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)//신글 테이블
@DiscriminatorColumn(name = "dtype")//부모 클레스는 Column
public class Item {//상속 받는 하위 클레스들을 가지고 하나의 테이블을 생성
    @Id@GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
    @ManyToMany(mappedBy = "items")
    private List<Category> categories=new ArrayList<>();

    //비즈니스 로직

    /**
     * stock 증가
     * @param stockQuantity
     */
    public void addStock(int stockQuantity){
        this.stockQuantity+=stockQuantity;
    }

    /**
     * stock 감소
     * @param stockQuantity
     */
    public void removeStock(int stockQuantity){
        int restStock = this.stockQuantity - stockQuantity;
        if(restStock<0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity=restStock;
    }
}
