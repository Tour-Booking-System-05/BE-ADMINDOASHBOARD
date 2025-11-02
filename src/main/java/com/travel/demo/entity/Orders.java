package com.travel.demo.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Integer orderId;

	private LocalDateTime date;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Items item;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;

	private Byte status;

	@Column(name = "amount_ticket")
	private Integer amountTicket;

	@Column(name = "delete_at")
	private LocalDateTime deleteAt;
}

