package earlybird.earlybird.security.authentication.oauth2.user;

import earlybird.earlybird.user.dto.UserAccountInfoDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class OAuth2UserDetails implements UserDetails {

    private final UserAccountInfoDTO userAccountInfoDTO;
    private final List<GrantedAuthority> roles;

    public UserAccountInfoDTO getUserAccountInfoDTO() {
        return userAccountInfoDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return userAccountInfoDTO.getAccountId();
    }

    @Override
    public String getUsername() {
        return userAccountInfoDTO.getAccountId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
