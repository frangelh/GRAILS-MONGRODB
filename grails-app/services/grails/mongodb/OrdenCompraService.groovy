package grails.mongodb

import grails.transaction.Transactional

@Transactional
class OrdenCompraService {

    void procesarMovimiento( long codigoMovimiento,String tipoMovimiento, long codigoArticulo, long cantidad){
        def movimiento = new MovimientoInventario(
                codigoMovimiento: codigoMovimiento,
                tipoMovimiento: tipoMovimiento,
                codigoArticulo: codigoArticulo,
                cantidad: cantidad
        ).insert()
    }



    def serviceMethod() {

    }
}
