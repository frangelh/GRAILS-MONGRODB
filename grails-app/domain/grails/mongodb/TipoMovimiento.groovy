package grails.mongodb

class TipoMovimiento {

    public static final String ENTRADA = "ENTRADA"
    public static final String SALIDA = "SALIDA"

    String descripcion

    static mapWith = "mongo"
    static constraints = {
    }
}
