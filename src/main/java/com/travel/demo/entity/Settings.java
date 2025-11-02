package com.travel.demo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "setting_id")
	private Integer settingId;

	@Column(name = "company_name")
	private String companyName;

	@Column(name = "company_tax")
	private String companyTax;

	@Column(name = "company_address", columnDefinition = "TEXT")
	private String companyAddress;

	private String phone;

	@Column(name = "url_website_user")
	private String urlWebsiteUser;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private Accounts account;
}
