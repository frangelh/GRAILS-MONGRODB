package com.app.encapsulados

/**
 * Created by frang on 1/12/2017.
 */
class TramaMovil {
    boolean error = false
    String mensaje

    TramaMovil() {
    }

    TramaMovil(boolean error, String mensaje) {
        this.error = error
        this.mensaje = mensaje
    }
}
