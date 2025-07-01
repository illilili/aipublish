package aipublish.config;

import aipublish.domain.User;
import aipublish.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // 관리자 계정이 없으면 생성
        if (userRepository.findByEmail("admin@gmail.com") == null) {
            User admin = new User();
            admin.setName("관리자");
            admin.setEmail("admin@gmail.com");
            admin.setPasswordHash("admin1234");
            admin.setIsAdmin(true);
            admin.setIsKtCustomer(false);
            admin.setSubscription(false);
            userRepository.save(admin);
            System.out.println("관리자 계정 초기화 완료");
        }
    }
}
