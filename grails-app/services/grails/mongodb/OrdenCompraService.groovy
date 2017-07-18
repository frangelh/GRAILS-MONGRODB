package grails.mongodb


import com.app.encapsulados.OrdenTabla
import grails.transaction.Transactional

@Transactional
class OrdenCompraService {

    void procesarMovimiento(long codigoMovimiento, String tipoMovimiento, long codigoArticulo, long cantidad) {
        Articulo articulo = Articulo.findByCodigoArticulo(codigoArticulo)
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


    public List<OrdenTabla> buscarTodo() {
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
    // Aplicar ordenes al inventario
    void aplicarOrdenes() {
        Date fechaServidor = new Date()
        OrdenCompra.findAll().each { a ->
            if (a.fechaOrden < fechaServidor && !a.procesada) {
                def detalles = DetalleOrden.findAllByCodigoOrdenCompra(a.codigoOrdenCompra)
                detalles.each { d ->
                    Articulo articulo = Articulo.findByCodigoArticulo(d.codigoArticulo)
                    articulo.cantidadDisponible += d.cantidad
                    articulo.save(flush:true)
                }
                OrdenCompra orden = OrdenCompra.findById(a.id)
                println "ORDEN:" + orden.id + "Procesada"
                orden.markDirty('procesada')
                orden.setProcesada(true)
                orden.save(flush:true)
            }
        }
    }


    def serviceMethod() {

    }
}
