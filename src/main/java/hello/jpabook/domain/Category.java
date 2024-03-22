package hello.jpabook.domain;

import hello.jpabook.domain.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity@Getter@Setter
public class Category {
    @Id@GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private  String name;
    @ManyToMany
    @JoinTable(name = "category_item",
    joinColumns = @JoinColumn(name = "category_id"),
    inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items=new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    @OneToMany(mappedBy = "parent")
    private List<Category> child=new ArrayList<>();

    //연관관계 편의 메소드 양방향 객체를 편하게 연결
    public void addChildCategory(Category category){
        this.child.add(category);
        category.setParent(this);
    }
}
