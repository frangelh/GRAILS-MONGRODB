package grails.mongodb

class Suplidor {
    long codigoSuplidor
    String descripcion
    String direccion
    long tiempoEntrega // en dias


    static mapWith = "mongo"

    static constraints = {
    }


    @Override
    public String toString() {
        return descripcion
    }
}
