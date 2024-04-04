package sk.tuke.gamestudio.service.impl.jdbc;

import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.commons.service.impl.jdbc.RatingServiceJDBC;
import sk.tuke.gamestudio.service.RatingServiceTest;

@SpringBootTest(classes = RatingServiceJDBC.class)
public class RatingServiceJDBCTest extends RatingServiceTest {
}
