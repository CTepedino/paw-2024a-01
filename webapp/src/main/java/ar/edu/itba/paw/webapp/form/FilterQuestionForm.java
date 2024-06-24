package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class FilterQuestionForm {

    private Boolean showComplete;

    @NotNull
    @Min(1)
    private Integer page = 1;



    public Boolean getShowComplete() {
        return showComplete;
    }

    public void setShowComplete(Boolean showComplete) {
        this.showComplete = showComplete;
    }
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
