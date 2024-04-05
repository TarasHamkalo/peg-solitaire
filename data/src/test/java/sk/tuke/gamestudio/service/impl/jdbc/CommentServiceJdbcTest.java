package sk.tuke.gamestudio.service.impl.jdbc;

import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.data.service.jdbc.CommentServiceJdbc;
import sk.tuke.gamestudio.service.CommentServiceTest;

@SpringBootTest(classes = CommentServiceJdbc.class)
public class CommentServiceJdbcTest extends CommentServiceTest {
}
