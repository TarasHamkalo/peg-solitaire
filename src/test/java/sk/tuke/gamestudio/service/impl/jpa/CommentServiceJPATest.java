package sk.tuke.gamestudio.service.impl.jpa;

import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.commons.service.impl.jpa.CommentServiceJPA;
import sk.tuke.gamestudio.service.CommentServiceTest;

@SpringBootTest(classes = CommentServiceJPA.class)
public class CommentServiceJPATest extends CommentServiceTest {
}
