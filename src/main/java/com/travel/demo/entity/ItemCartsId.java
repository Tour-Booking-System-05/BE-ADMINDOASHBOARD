package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ItemCartsId implements Serializable {
  @Column(name = "cart_id")
  private Integer cartId;

  @Column(name = "item_id")
  private Integer itemId;
}
