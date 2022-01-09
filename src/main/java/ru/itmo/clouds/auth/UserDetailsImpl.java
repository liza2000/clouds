package ru.itmo.clouds.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itmo.clouds.repository.EUser;

import java.util.Collection;
import java.util.Collections;

@Data
public class UserDetailsImpl implements UserDetails {

    Long id;
    private  String login;
    @JsonIgnore
    private String  password;
    private final Collection<GrantedAuthority> authorities = Collections.emptySet();

   public Collection<GrantedAuthority>  getAuthorities() {
        return authorities;
    }
    

    UserDetailsImpl(Long id, String login, String password){
        this.id = id;
        this.login = login;
        this.password = password;
    }

      public static UserDetailsImpl build(EUser eUser) {
            return new UserDetailsImpl(
                eUser.getId(),
                eUser.getLogin(),
                eUser.getPassword());
        }


   public String getPassword() {
       return password;
    }

   public String getUsername() {
       return  login;
    }

   public boolean isAccountNonExpired() {
       return true;
    }

    public boolean isAccountNonLocked() {
       return true;
    }

    public boolean isCredentialsNonExpired() {
       return true;
    }

    public boolean isEnabled() {
       return true;
    }
}