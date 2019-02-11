package com.withkid.auth.domain;

import javax.persistence.*;

import com.withkid.auth.utills.ShaUtill;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;

@Entity
@Getter @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_ID")
	private Long id;
	private String email;
	private String password;

	public void passwordToHash() throws NoSuchAlgorithmException {
		this.password = ShaUtill.sha256(this.password);
	}
}
