package grails.mongodb

class BootStrap {

    def init = { servletContext ->

        //INSERTANDO SUPLIDORES
        def suplidor1 = Suplidor.findByCodigoSuplidor(1) ?: new Suplidor(codigoSuplidor: 1, descripcion: "LA SIRENA", direccion: "AUTOPISTA DUARTE # 123", tiempoEntrega: 4).insert()
        def suplidor2 = Suplidor.findByCodigoSuplidor(2) ?: new Suplidor(codigoSuplidor: 2, descripcion: "EL ENCANTO", direccion: "AVENIDA 27 FEBRERO # 12", tiempoEntrega: 10).insert()
        def suplidor3 = Suplidor.findByCodigoSuplidor(3) ?: new Suplidor(codigoSuplidor: 3, descripcion: "EL NACIONAL", direccion: "AVENIDA DUARTE # 120", tiempoEntrega: 15).insert()
        def suplidor4 = Suplidor.findByCodigoSuplidor(4) ?: new Suplidor(codigoSuplidor: 2, descripcion: "TREBOL", direccion: "C10 #30 EMBRUJO # 12", tiempoEntrega: 6).insert()
        def suplidor5 = Suplidor.findByCodigoSuplidor(5) ?: new Suplidor(codigoSuplidor: 2, descripcion: "COLMADON", direccion: "AVENIDA 27 FEBRERO # 12", tiempoEntrega: 8).insert()

        def articulo1 = Articulo.findByCodigoArticulo(1) ?: new Articulo(codigoArticulo: 1, descripcion: "COMPUTADORA DELL", UNIDAD: "UNIDAD", precio: new BigDecimal(200), cantidadDisponible: 2, suplidores: [1, 2]).insert()
        def articulo2 = Articulo.findByCodigoArticulo(2) ?: new Articulo(codigoArticulo: 2, descripcion: "COMPUTADORA HP", UNIDAD: "UNIDAD", precio: new BigDecimal(150), cantidadDisponible: 10, suplidores: [2]).insert()
        def articulo3 = Articulo.findByCodigoArticulo(3) ?: new Articulo(codigoArticulo: 3, descripcion: "COMPUTADORA COMPAQ", UNIDAD: "UNIDAD", precio: new BigDecimal(100), cantidadDisponible: 3, suplidores: [2]).insert()


    }
    def destroy = {
    }
}
