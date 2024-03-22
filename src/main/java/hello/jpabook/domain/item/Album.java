package hello.jpabook.domain.item;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("A")//구분 인자 자식은 Value해야한다
public class Album extends Item{
    private String artist;
    private String etc;
}
