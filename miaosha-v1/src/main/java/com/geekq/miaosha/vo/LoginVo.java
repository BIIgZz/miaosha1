package com.geekq.miaosha.vo;

import com.geekq.miaosha.validator.MobileCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {
    @NotNull
    @MobileCheck
    private String username;

    @NotNull
    @Length(min = 32)
    private String password;

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
