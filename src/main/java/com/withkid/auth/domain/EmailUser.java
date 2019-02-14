package com.withkid.auth.domain;

import com.withkid.auth.utills.ShaUtill;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.security.NoSuchAlgorithmException;

@Entity
@DiscriminatorValue("E")
@Getter
@NoArgsConstructor
public class EmailUser extends User{

    @Column
    private String password;

    public EmailUser(Long id, String email, String password) {
        super(id, email);
        this.password = password;
    }

    public void passwordToHash() throws NoSuchAlgorithmException {
        this.password = ShaUtill.sha256(this.password);
    }
}
