package com.app

import com.app.encapsulados.MovimientoRest
import com.app.encapsulados.TramaMovil
import grails.converters.JSON

class OrdenCompraController {

    def ordenCompraService

    def index() {render "Bienvenido a los servicios"}

    def procesarMovimiento(long codigoMovimiento,String tipoMovimiento,long codigoArticulo,long cantidad){
        TramaMovil trama = new TramaMovil()
        try {
            ordenCompraService.procesarMovimiento(codigoMovimiento, tipoMovimiento, codigoArticulo, cantidad)
            trama = new TramaMovil(false,"Guardado satisfactoriamente...")

        }catch (Exception ex) {
            ex.printStackTrace()
            trama = new TramaMovil(true, ex.message)
        }
        render trama as JSON
    }
    def listarMovimientos(){
        TramaMovil trama = new TramaMovil()
        try {
            render ordenCompraService.buscarTodo() as JSON


        }catch (Exception ex) {
            ex.printStackTrace()
            trama = new TramaMovil(true, ex.message)
        }
        render trama as JSON
    }


}
