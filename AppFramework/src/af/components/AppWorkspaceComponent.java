/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package af.components;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public abstract class AppWorkspaceComponent {
    
    protected Pane workspace;
    protected boolean workspaceActived;
    
    public void activateWorkspace (BorderPane appPane) {
        if (!workspaceActived) {
            appPane.setCenter(workspace);
            workspaceActived = true;
        }
    }
    
    public void setWorkspace(Pane initWorkspace) {
        workspace = initWorkspace;
    }
    
    public Pane getWorkspace() { return workspace; }
    
    public abstract void resetWorkspace();
    
    public abstract void reloadWorkspace(AppDataComponent dataComponent);
    
}
