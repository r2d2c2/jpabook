package hello.jpabook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity@Getter@Setter
public class Delivery {
    @Id@GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)//조회만
    private Order order;
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)//문자 사용을 위해 String필수
    private DeliveryStatus status;
}
