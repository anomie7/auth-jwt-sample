package com.withkid.auth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@Entity
@DiscriminatorValue("F")
@Getter
@NoArgsConstructor
public class FacebookUser extends User{

    private String name;
    private String picture;
    private String birthday;

    public FacebookUser(Long id, String email, String name, String picture, String birthday) {
        super(id, email);
        this.name = name;
        this.picture = picture;
        this.birthday = birthday;
    }

    public static String getPictureUrl(org.springframework.social.facebook.api.User profile) {
        Map<String, Object> extraData = profile.getExtraData();
        HashMap picture = (HashMap) extraData.get("picture");
        HashMap data = (HashMap) picture.get("data");
        String url = (String) data.get("url");
        return url;
    }
}
