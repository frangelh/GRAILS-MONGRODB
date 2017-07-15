package com.app

import com.app.vistas.VistaPrincipal
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.UI

class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
		setContent(new VistaPrincipal())
    }
}
