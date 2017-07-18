package grails.mongodb

import com.app.encapsulados.MovimientoTabla
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
            if (cantidad > articulo.cantidadDisponible) {
                cantidad = articulo.cantidadDisponible
            }
            articulo.cantidadDisponible -= cantidad
            articulo.save()
        }
        println "PROCESANDO: $codigoMovimiento $tipoMovimiento $codigoArticulo $cantidad"
        def movimiento = new MovimientoInventario(
                codigoMovimiento: codigoMovimiento,
                tipoMovimiento: tipoMovimiento,
                codigoArticulo: codigoArticulo,
                cantidad: cantidad,
                fechaMovimiento: new Date()
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

    void pedidoEmergencia() {

        List<DetalleOrden> detalles = new ArrayList<>()
        BigDecimal total = new BigDecimal(0)
        Articulo.findAll().each { art ->
            if ((art.cantidadDisponible - art.cantidadConsumoDiario) < 0) {
                println "PROCESANDO JOB: "+art.codigoArticulo
                DetalleOrden d = new DetalleOrden()
                d.codigoArticulo = art.codigoArticulo
                d.descripcion = art.descripcion
                d.UNIDAD = art.UNIDAD
                d.precio = art.precio
                d.cantidad = art.cantidadConsumoDiario * 1.05 // de excedente
                detalles.add(d)
                total += d.cantidad * d.precio
            }
        }
        procesarOrdenCompra(new Random().nextInt(10000), detalles.codigoSuplidor.get(0), new Date(), total, detalles)


    }

    public List<Suplidor> listarSuplidores() {
        return Suplidor.findAll()
    }

    void calcularConsumo() {
        def articulos = Articulo.findAll()
        long max = 0;
        articulos.each { a ->
            def movimientos = MovimientoInventario.findAllByCodigoArticulo(a.codigoArticulo)
            movimientos.each { m ->
                if (m.cantidad > max)
                    max = m.cantidad
            }
            Articulo articulo = Articulo.findById(a.id)
            articulo.cantidadConsumoDiario = max
            articulo.save()
        }
    }


    def serviceMethod() {

    }
}
