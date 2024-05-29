package org.design_manager_project.models.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.models.BaseModel;
import org.design_manager_project.models.enums.AuthRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User extends BaseModel implements UserDetails {

    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "avatar")
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 8)
    private AuthRole role;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "user")
    private List<Space> spaces;

    @OneToMany(mappedBy = "user")
    private List<Member> members;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
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