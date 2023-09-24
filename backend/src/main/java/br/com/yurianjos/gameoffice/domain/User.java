package br.com.yurianjos.gameoffice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Table(name = "users")
@Entity(name = "users")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<RentedGame> rentedGames;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = Roles.USER.name();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (Roles.ADMIN.name().equals(this.role)) {
            return List.of(new SimpleGrantedAuthority(Roles.ADMIN.getRole()), new SimpleGrantedAuthority(Roles.USER.getRole()));
        } else {
            return List.of(new SimpleGrantedAuthority(Roles.USER.getRole()));
        }
    }

    @Override
    public String getUsername() {
        return username;
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