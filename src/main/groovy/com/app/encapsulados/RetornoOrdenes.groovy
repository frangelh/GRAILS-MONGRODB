package com.app.encapsulados

import grails.mongodb.MovimientoInventario
import grails.mongodb.OrdenCompra

class RetornoOrdenes {
    TramaMovil tramaMovil
    List<OrdenCompra> ordenes
}
