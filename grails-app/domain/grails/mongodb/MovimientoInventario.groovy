package grails.mongodb

class MovimientoInventario {
    long codigoMovimiento
    String tipoMovimiento
    long codigoArticulo
    long cantidad
    Date fechaMovimiento


    static mapWith = "mongo"
    static constraints = {
    }
}
