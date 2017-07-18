package com.app.encapsulados

import grails.mongodb.Articulo
import grails.mongodb.OrdenCompra
import grails.mongodb.Suplidor

class OrdenTabla {

    long codigo
    BigDecimal monto
    Date fechaOrden
    String suplidor
    long tiempo
    boolean estado
    OrdenCompra orden
}
