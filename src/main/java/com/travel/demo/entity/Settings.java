package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "settings")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Settings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "setting_id")
	private Integer settingId;

	@Column(name = "company_name", length = 255)
	private String companyName;

	@Column(name = "company_tax", length = 255)
	private String companyTax;

	@Column(name = "company_address", columnDefinition = "text")
	private String companyAddress;

	@Column(name = "phone", length = 15)
	private String phone;

	@Column(name = "company_email", length = 255)
	private String companyEmail;

	@Column(name = "url_logo", columnDefinition = "text")
	private String urlLogo;
}
