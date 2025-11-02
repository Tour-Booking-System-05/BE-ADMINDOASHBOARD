package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCarts {
    @EmbeddedId
    private ItemCartsId id;

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private Carts cart;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Items item;
}
