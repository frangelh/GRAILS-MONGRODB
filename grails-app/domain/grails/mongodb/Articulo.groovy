package grails.mongodb

class Articulo {

    long codigoArticulo
    String descripcion
    String UNIDAD
    BigDecimal precio

    //long codigoAlmacen no es necesario
    long cantidadDisponible // cada vez que se haga un movimiento de inventario debe modificar esta tabla

    List<Long> suplidores

    static mapWith = "mongo"
    static constraints = {
    }
}
