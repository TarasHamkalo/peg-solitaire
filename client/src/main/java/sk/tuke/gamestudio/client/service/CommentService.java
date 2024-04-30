package sk.tuke.gamestudio.client.service;


import sk.tuke.gamestudio.client.service.exception.ServiceException;
import sk.tuke.gamestudio.server.api.rest.dto.CommentDto;

import java.util.List;

public interface CommentService {
  void addComment(CommentDto comment) throws ServiceException;

  List<CommentDto> getComments(String game) throws ServiceException;
}
