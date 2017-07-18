package grails.mongodb


class OrdenCompra {

    long codigoOrdenCompra
    long codigoSuplidor
    Date fechaOrden
    BigDecimal montoTotal
    boolean procesada // una ves ha sido procesada se marca null


    static mapWith = "mongo"

    static constraints = {
    }
}
