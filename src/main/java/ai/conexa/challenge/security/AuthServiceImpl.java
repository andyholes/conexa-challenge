package ai.conexa.challenge.security;

import ai.conexa.challenge.exception.InvalidCredentialsException;
import ai.conexa.challenge.exception.UnauthorizedException;
import ai.conexa.challenge.security.dto.TokenResponse;
import ai.conexa.challenge.security.dto.UserRequest;
import ai.conexa.challenge.security.user.RolesEnum;
import ai.conexa.challenge.security.user.UserEntity;
import ai.conexa.challenge.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public TokenResponse register(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        UserEntity savedUser = userRepository.save(
                UserEntity.builder()
                        .username(userRequest.getUsername())
                        .password(passwordEncoder.encode(userRequest.getPassword()))
                        .roles(Stream.of(RolesEnum.ROLE_USER).collect(Collectors.toSet()))
                        .build());
        return new TokenResponse(jwtUtils.generateToken(savedUser.getUsername(), savedUser.getRoles()));
    }

    @Override
    public TokenResponse login(UserRequest userRequest) {
        UserEntity user = userRepository.findByUsername(userRequest.getUsername())
                .orElseThrow(InvalidCredentialsException::new);
        if (passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            return new TokenResponse(jwtUtils.generateToken(user.getUsername(), user.getRoles()));
        }
        throw new InvalidCredentialsException();
    }

    @Override
    public void delete(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findById(id).orElseThrow(UnauthorizedException::new);
        boolean isUserOwnAccount = auth.getName().equals(user.getUsername());
        boolean isAdminUser = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(RolesEnum.ROLE_ADMIN.toString()));
        if (isAdminUser || isUserOwnAccount) {
            userRepository.deleteById(id);
        } else{
            throw new UnauthorizedException();
        }
    }
}
