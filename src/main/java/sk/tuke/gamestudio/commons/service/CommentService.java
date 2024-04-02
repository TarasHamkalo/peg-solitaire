package sk.tuke.gamestudio.commons.service;

import sk.tuke.gamestudio.commons.entity.Comment;
import sk.tuke.gamestudio.commons.exception.CommentException;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment) throws CommentException;

    List<Comment> getComments(String game) throws CommentException;

    void reset() throws CommentException;
}
