package com.app

import com.app.encapsulados.RetornoOrdenes
import com.app.encapsulados.TramaMovil
import grails.converters.JSON
import grails.mongodb.OrdenCompra

class OrdenCompraController {

    def ordenCompraService

    def index() { render "Bienvenido a los servicios" }

    def procesarMovimiento(long codigoMovimiento, String tipoMovimiento, long codigoArticulo, long cantidad) {
        TramaMovil trama = new TramaMovil()
        try {
            ordenCompraService.procesarMovimiento(codigoMovimiento, tipoMovimiento, codigoArticulo, cantidad)
            trama = new TramaMovil(false, "Guardado satisfactoriamente...")

        } catch (Exception ex) {
            ex.printStackTrace()
            trama = new TramaMovil(true, ex.message)
        }
        render trama as JSON
    }

    def listarOrdenes() {
        RetornoOrdenes retorno = new RetornoOrdenes()
        TramaMovil trama = new TramaMovil()
        try {
            retorno.ordenes = OrdenCompra.findAll()
            trama = new TramaMovil(false, "Correcto...")

        } catch (Exception ex) {
            ex.printStackTrace()
            trama = new TramaMovil(true, ex.message)
        }
        retorno.tramaMovil = trama
        render retorno as JSON
    }


}
