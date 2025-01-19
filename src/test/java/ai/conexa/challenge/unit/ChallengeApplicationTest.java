package ai.conexa.challenge.unit;

import ai.conexa.challenge.ChallengeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChallengeApplicationTest {

    @Test
    public void testMain() {
        String[] args = {"--server.port=8081"};
        ChallengeApplication.main(args);
    }
}
