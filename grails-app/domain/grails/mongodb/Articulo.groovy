package grails.mongodb

class Articulo {

    long codigoArticulo
    String descripcion
    String UNIDAD

    long codigoAlmacen
    long cantidadDisponible // cada vez que se haga un movimiento de inventario debe modificar esta tabla

    List<Suplidor> suplidores

    static mapWith = "mongo"
    static constraints = {
    }
}
