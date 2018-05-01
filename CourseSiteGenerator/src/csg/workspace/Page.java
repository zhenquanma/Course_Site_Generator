package csg.workspace;

import af.components.AppDataComponent;
import javafx.scene.Node;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public interface Page {
    
    public void reloadPage(AppDataComponent dataComponent);
    public void resetPage();
    public Node getPage();
}
