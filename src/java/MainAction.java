
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport {

    private String name;

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public String showName() throws Exception {
        return SUCCESS;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}