package grails.mongodb

class MovimientoInventario {
    long codigoMovimiento
    String tipoMovimiento
    long codigoArticulo
    long cantidad



    static mapWith = "mongo"
    static constraints = {
    }
}
