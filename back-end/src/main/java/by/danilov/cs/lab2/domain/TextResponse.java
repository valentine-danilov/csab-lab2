package by.danilov.cs.lab2.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class TextResponse {

    private String text;

    public TextResponse(String text) {
        this.text = text;
    }

}
