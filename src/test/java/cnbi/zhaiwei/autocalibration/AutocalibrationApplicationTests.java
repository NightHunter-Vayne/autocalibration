package cnbi.zhaiwei.autocalibration;

import cnbi.zhaiwei.autocalibration.service.AutoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan(value = {"cnbi.zhaiwei.autocalibration.dao"})
class AutocalibrationApplicationTests {
    @Autowired
    private AutoService service;

    @Test
    void contextLoads() {
        service.autoPaddingSubject("DW_FACTFIXEDBIOLOGICAL");
    }
}
