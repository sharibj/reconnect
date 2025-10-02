package adapter.primary.http.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TenantRepository tenantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Tenant tenant = tenantRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .id(tenant.getId())
                .username(tenant.getUsername())
                .password(tenant.getPassword())
                .email(tenant.getEmail())
                .enabled(tenant.isEnabled())
                .accountNonExpired(tenant.isAccountNonExpired())
                .accountNonLocked(tenant.isAccountNonLocked())
                .credentialsNonExpired(tenant.isCredentialsNonExpired())
                .build();
    }
}
