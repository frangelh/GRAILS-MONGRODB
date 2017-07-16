package com.app.encapsulados

import com.vaadin.ui.Button
import grails.mongodb.Articulo
import grails.mongodb.Suplidor

class ArticuloTabla {

    long codigo
    String descripcion
    BigDecimal precio
    long cantidad

    //suplidor
    Suplidor suplidor
    long tiempo

    Articulo articulo
    String boton = "Eliminar"

}
