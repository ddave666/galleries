
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dave
 */
public class Gallery {
    private String title;
    private List images;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Gallery(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Gallery() {
        this.title = "";
        this.description = "";
    }

    public List getImages() {
        return images;
    }

    public void setImages(List images) {
        this.images = images;
    }


}
