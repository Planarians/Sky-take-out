import com.sky.entity.User;
import com.sky.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootTest(classes =User.class)

@MapperScan("com.sky.mapper")
@Slf4j
public class SkyApplicationTests {




//    public static void main(String[] args) {
//        SpringApplication.run(SkyApplicationTests.class, args);
//        log.info("server started");





    @Autowired
    private UserRepository userRepository;

    @Test
    void jpaTest(){
        userRepository.findUserBySex("男");
    }
}
