package grails.mongodb

class BootStrap {

    def init = { servletContext ->

        //INSERTANDO SUPLIDORES
        def suplidor1 = new Suplidor(codigoSuplidor: 1,descripcion: "LA SIRENA",direccion: "AUTOPISTA DUARTE # 123",tiempoEntrega: 4).insert()
        def suplidor2 = new Suplidor(codigoSuplidor: 2,descripcion: "EL ENCANTO",direccion: "AVENIDA 27 FEBRERO # 12",tiempoEntrega: 10).insert()
        def suplidor3 = new Suplidor(codigoSuplidor: 3,descripcion: "EL NACIONAL",direccion: "AVENIDA DUARTE # 120",tiempoEntrega: 15).insert()
        def suplidor4 = new Suplidor(codigoSuplidor: 2,descripcion: "TREBOL",direccion: "C10 #30 EMBRUJO # 12",tiempoEntrega: 6).insert()
        def suplidor5 = new Suplidor(codigoSuplidor: 2,descripcion: "EL ENCANTO",direccion: "AVENIDA 27 FEBRERO # 12",tiempoEntrega: 8).insert()




    }
    def destroy = {
    }
}
