
package wgu.edu.BrinaBright.Security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wgu.edu.BrinaBright.Entities.User;

import java.util.Collection;
import java.util.List;

@Getter
@Data



    public class UserPrincipal implements UserDetails {

        private final Long id;
        private final String email;
        private final String password;
        private final Collection<? extends GrantedAuthority> authorities;

        public UserPrincipal(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
            this.id = id;
            this.email = email;
            this.password = password;
            this.authorities = authorities;
        }

        public static UserPrincipal create(User user) {
            return new UserPrincipal(
                    user.getId(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    List.of(() -> "ROLE_" + user.getRole().name())
            );
        }

    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
