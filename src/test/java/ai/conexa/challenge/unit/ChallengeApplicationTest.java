package ai.conexa.challenge.unit;

import ai.conexa.challenge.ChallengeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ChallengeApplicationTest {

    @Test
    void testMain() {
        String[] args = {"--server.port=8081"};
        assertDoesNotThrow(() -> ChallengeApplication.main(args));
    }
}
