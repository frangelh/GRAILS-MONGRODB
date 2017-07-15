package grails.mongodb

import grails.transaction.Transactional

@Transactional
class OrdenCompraService {

    void procesarMovimiento( long codigoMovimiento,String tipoMovimiento, long codigoArticulo, long cantidad){
        //TODO: debe afectar la tabla de articulos
        println "PROCESANDO: $codigoMovimiento $tipoMovimiento $codigoArticulo $cantidad"
        def movimiento = new MovimientoInventario(
                codigoMovimiento: codigoMovimiento,
                tipoMovimiento: tipoMovimiento,
                codigoArticulo: codigoArticulo,
                cantidad: cantidad
        ).insert()
    }
    void procesarOrdenCompra(){

        def orden = new OrdenCompra(

        ).insert()
    }



    def serviceMethod() {

    }
}
