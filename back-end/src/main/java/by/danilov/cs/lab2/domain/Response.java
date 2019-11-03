package by.danilov.cs.lab2.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Response {

    private String message;
    private Integer code;

}
