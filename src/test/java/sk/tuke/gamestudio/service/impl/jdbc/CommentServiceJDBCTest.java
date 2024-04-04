package sk.tuke.gamestudio.service.impl.jdbc;

import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.commons.service.impl.jdbc.CommentServiceJDBC;
import sk.tuke.gamestudio.service.CommentServiceTest;

@SpringBootTest(classes = CommentServiceJDBC.class)
public class CommentServiceJDBCTest extends CommentServiceTest {
}
