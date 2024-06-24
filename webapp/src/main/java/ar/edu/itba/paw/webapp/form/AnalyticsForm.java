package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.YearMonth;

public class AnalyticsForm {

    private boolean byMonth = false;
    private Integer year = YearMonth.now().getYear();
    private Integer month = YearMonth.now().getMonthValue();

    @NotNull
    @Min(1)
    private Integer page = 1;

    public boolean byMonth() {
        return byMonth;
    }

    public void setByMonth(boolean byMonth) {
        this.byMonth = byMonth;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
