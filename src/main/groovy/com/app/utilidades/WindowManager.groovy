package com.app.utilidades

import com.vaadin.ui.UI
import com.vaadin.ui.Window


public class WindowManager {

    public static void addWindow(Window miWindow){
        boolean loContiene = false

        def windows = UI.current.getWindows()
        for(Window w: windows){
            if(miWindow.getId().equals(w.getId())){
                loContiene = true
                w.bringToFront()
                w.focus()
            }
        }

        if(!loContiene){
            UI.current.addWindow(miWindow)
        }
    }
}
