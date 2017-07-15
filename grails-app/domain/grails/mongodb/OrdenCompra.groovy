package grails.mongodb

class OrdenCompra {

    long codigoOrdenCompra
    long codigoSuplidor
    Date fechaOrden
    BigDecimal montoTotal
    List<Articulo> articulos


    static mapWith = "mongo"

    static constraints = {
    }
}
