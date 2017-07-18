package grails.mongodb

class DetalleOrden {
    long codigoOrdenCompra
    long codigoArticulo
    String descripcion
    String UNIDAD

    long codigoSuplidor

    BigDecimal precio
    long cantidad

    static mapWith = "mongo"
    static constraints = {
    }
}
