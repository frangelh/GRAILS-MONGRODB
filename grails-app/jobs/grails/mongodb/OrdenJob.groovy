package grails.mongodb

class OrdenJob {
    OrdenCompraService ordenCompraService

    static triggers = {
      simple repeatInterval: 10000l // execute job once in 5 seconds
    }

    def execute() {
        // execute job
        println "Corriendo JOB:"
        ordenCompraService.aplicarOrdenes()

    }
}
