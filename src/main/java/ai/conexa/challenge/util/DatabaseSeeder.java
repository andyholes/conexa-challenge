package ai.conexa.challenge.util;

import ai.conexa.challenge.security.user.UserEntity;
import ai.conexa.challenge.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ai.conexa.challenge.security.user.RolesEnum.ROLE_ADMIN;
import static ai.conexa.challenge.security.user.RolesEnum.ROLE_USER;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(UserEntity.builder().username("admin").password(passwordEncoder.encode("1234")).roles(Stream.of(ROLE_ADMIN).collect(Collectors.toSet())).build());
        userRepository.save(UserEntity.builder().username("user").password(passwordEncoder.encode("1234")).roles(Stream.of(ROLE_USER).collect(Collectors.toSet())).build());
        log.info("Database seeded");
    }
}
