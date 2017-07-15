package grails.mongodb

class MovimientoInventario {
    long codigoMovimiento
    long codigoAlmacen
    TipoMovimiento tipoMovimiento
    long codigoArticulo
    long cantidad
    String unidad


    static mapWith = "mongo"
    static constraints = {
    }
}
