package com.oauth.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(schema="OAUTH", name="USER")
@SequenceGenerator(
	name="USER_ID_SEQ_GENERATOR",
	schema="OAUTH",
	sequenceName="USER_ID_SEQ",
	initialValue=1,
	allocationSize=1
)
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_ID_SEQ_GENERATOR")
	@Column(name="id")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="email")
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	@Builder
	public User(String name, String email, Role role) {
		this.name = name;
		this.email = email;
		this.role = role;
	}
	
	public User update(String name) {
        this.name = name;

        return this;
    }
	
	public String getRoleKey() {
		return this.role.getKey();
	}
}

