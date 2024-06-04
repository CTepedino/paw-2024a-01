package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class QuestionForm {

    @Size(min = 1, max = 500)
    private String question;

    public String getQuestion(){
        return question;
    }

    public void setQuestion(String question){
        this.question=question;
    }

}
