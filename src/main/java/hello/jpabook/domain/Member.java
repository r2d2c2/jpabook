package hello.jpabook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity@Setter@Getter
public class Member {
    @Id@GeneratedValue
    @Column(name = "member_id")
    private Long id;
    @NotEmpty
    private String name;
    @Embedded
    private Address address;
    @JsonIgnore
    @OneToMany(mappedBy = "member")//조회만
    private List<Order> orders=new ArrayList<>();
}
