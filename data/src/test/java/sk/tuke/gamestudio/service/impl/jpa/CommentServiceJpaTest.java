package sk.tuke.gamestudio.service.impl.jpa;

import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.data.service.jpa.CommentServiceJpa;
import sk.tuke.gamestudio.service.CommentServiceTest;

@SpringBootTest(classes = CommentServiceJpa.class)
public class CommentServiceJpaTest extends CommentServiceTest {
}
