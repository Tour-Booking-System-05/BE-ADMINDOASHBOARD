package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carts {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_id")
	private Integer cartId;

	@Column(name = "created_at")
	private LocalDate createdAt;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
	private List<ItemCarts> itemCarts;
}
