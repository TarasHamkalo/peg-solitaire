package sk.tuke.gamestudio.service.impl.jdbc;

import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.commons.service.impl.jdbc.ScoreServiceJDBC;
import sk.tuke.gamestudio.service.ScoreServiceTest;

@SpringBootTest(classes = ScoreServiceJDBC.class)
public class ScoreServiceJDBCTest extends ScoreServiceTest {
}
