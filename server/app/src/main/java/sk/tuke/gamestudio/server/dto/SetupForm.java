package sk.tuke.gamestudio.server.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetupForm {

    Integer level;

    List<String> selectedEvents;

}
