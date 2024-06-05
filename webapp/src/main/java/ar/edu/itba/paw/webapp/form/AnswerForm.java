package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AnswerForm {

    @Size(min = 1, max = 500)
    private String answer;

    public String getAnswer(){
        return answer;
    }

    public void setAnswer(String answer){
        this.answer=answer;
    }

}