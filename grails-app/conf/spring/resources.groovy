// Place your Spring DSL code here

beans = {
    ordenCompraService(grails.mongodb.OrdenCompraService) {bean->
    bean.autowire = true
    }
}
