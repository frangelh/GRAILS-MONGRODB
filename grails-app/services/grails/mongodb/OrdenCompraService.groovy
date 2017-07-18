package grails.mongodb

import com.app.encapsulados.ArticuloTabla
import com.app.encapsulados.MovimientoTabla
import com.app.encapsulados.OrdenTabla
import com.vaadin.grails.Grails
import grails.transaction.Transactional

@Transactional
class OrdenCompraService {

    void procesarMovimiento(long codigoMovimiento, String tipoMovimiento, long codigoArticulo, long cantidad) {
        Articulo articulo = Articulo.findByCodigoArticulo(codigoArticulo)
        if (cantidad > articulo.cantidadDisponible) {
            cantidad = articulo.cantidadDisponible
            //pedidoEmergencia(articulo)
        }
        if (tipoMovimiento.contains("ENTRADA")) {
            articulo.cantidadDisponible += cantidad
            articulo.save()
        } else {
            articulo.cantidadDisponible -= cantidad
            articulo.save()
        }
        println "PROCESANDO: $codigoMovimiento $tipoMovimiento $codigoArticulo $cantidad"
        def movimiento = new MovimientoInventario(
                codigoMovimiento: codigoMovimiento,
                tipoMovimiento: tipoMovimiento,
                codigoArticulo: codigoArticulo,
                cantidad: cantidad
        ).insert()
    }

    void procesarOrdenCompra(long codigoOrdenCompra, long codigoSuplidor, Date fechaOrden, BigDecimal montoTotal, List<DetalleOrden> detalles) {
        println "PROCESANDO: $codigoOrdenCompra $codigoSuplidor $fechaOrden $montoTotal $detalles"

        detalles.each { d ->
            d.codigoOrdenCompra = codigoOrdenCompra
            d.insert()
        }

        def detallesOrden = DetalleOrden.findAllByCodigoOrdenCompra(codigoOrdenCompra)

        def orden = new OrdenCompra(
                codigoOrdenCompra: codigoOrdenCompra,
                codigoSuplidor: codigoSuplidor,
                fechaOrden: fechaOrden,
                montoTotal: montoTotal,
                procesada: false
        ).insert()
    }


    public List<OrdenTabla> buscarOrdenes() {
        List<OrdenTabla> listaOrdenes = new ArrayList<>()
        OrdenCompra.findAll().each { a ->
            OrdenTabla at = new OrdenTabla()
            at.codigo = a.codigoOrdenCompra
            at.monto = a.montoTotal
            at.fechaOrden = a.fechaOrden
            at.estado = a.procesada
            def suplidorSeleccionado = new Suplidor(tiempoEntrega: 10000)
            Suplidor sup = Suplidor.findByCodigoSuplidor(a.codigoSuplidor)
            if (sup.tiempoEntrega < suplidorSeleccionado.tiempoEntrega)
                suplidorSeleccionado = sup
            at.suplidor = suplidorSeleccionado.descripcion
            at.tiempo = suplidorSeleccionado.tiempoEntrega
            listaOrdenes.add(at)
        }
        return listaOrdenes
    }

    public List<MovimientoTabla> buscarMovimientos() {
        List<MovimientoTabla> movimientos = new ArrayList<>()
        MovimientoInventario.findAll().each { a ->
            MovimientoTabla at = new MovimientoTabla()
            at.codigoMovimiento = a.codigoMovimiento
            at.tipoMovimiento = a.tipoMovimiento
            at.codigoArticulo = a.codigoArticulo
            at.cantidad = a.cantidad
            movimientos.add(at)
        }
        return movimientos
    }
    // Aplicar ordenes al inventario
    void aplicarOrdenes() {
        Date fechaServidor = new Date()
        OrdenCompra.findAll().each { a ->
            if (a.fechaOrden < fechaServidor && !a.procesada) {
                def detalles = DetalleOrden.findAllByCodigoOrdenCompra(a.codigoOrdenCompra)
                detalles.each { d ->
                    procesarMovimiento(d.codigoOrdenCompra, "ENTRADA", d.codigoArticulo, d.cantidad)

                }
                OrdenCompra orden = OrdenCompra.findById(a.id)
                println "ORDEN:" + orden.id + "Procesada"
                orden.markDirty('procesada')
                orden.setProcesada(true)
                orden.save(flush: true)
            }
        }
    }

    void pedidoEmergencia(Articulo articulo) {
        //Ejecutar una orden de compra
        ArticuloTabla art = Grails.get(ArticuloService).buscarArticulo(articulo.codigoArticulo, 50).get(0)
        List<DetalleOrden> detalles = new ArrayList<>()
        DetalleOrden d = new DetalleOrden()
        d.codigoArticulo = art.articulo.codigoArticulo
        d.descripcion = art.articulo.descripcion
        d.UNIDAD = art.articulo.UNIDAD
        d.precio = art.articulo.precio
        d.cantidad = art.cantidad
        detalles.add(d)
        procesarOrdenCompra(new Random().nextInt(10000), art.suplidor.codigoSuplidor, new Date(), art.cantidad * art.precio, detalles)
    }
    public List<Suplidor> listarSuplidores() {
        return Suplidor.findAll()
    }


    def serviceMethod() {

    }
}
