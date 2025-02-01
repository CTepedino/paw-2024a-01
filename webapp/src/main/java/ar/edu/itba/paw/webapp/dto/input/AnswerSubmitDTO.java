package ar.edu.itba.paw.webapp.dto.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AnswerSubmitDTO {

    @Size(min = 1, max = 500)
    @NotNull
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
