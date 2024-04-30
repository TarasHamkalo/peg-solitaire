package sk.tuke.gamestudio.data.service;

import sk.tuke.gamestudio.data.entity.Comment;
import sk.tuke.gamestudio.data.exception.CommentException;

import java.util.List;

public interface CommentService {
  void addComment(Comment comment) throws CommentException;

  List<Comment> getComments(String game) throws CommentException;

  void reset() throws CommentException;
}
