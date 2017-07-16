package grails.mongodb

import com.app.encapsulados.ArticuloTabla
import grails.transaction.Transactional

@Transactional
class ArticuloService {

    public List<ArticuloTabla> findAll(int offset, int limit) {
        List<ArticuloTabla> listaArticulos = new ArrayList<>()
        Articulo.findAll().each { a ->
            ArticuloTabla at = new ArticuloTabla()
            at.codigo = a.codigoArticulo

            at.descripcion = a.descripcion
            at.precio = a.precio
            at.cantidad = a.cantidadDisponible

            at.articulo = a
            listaArticulos.add(at)
        }
        return listaArticulos
    }

    public List<ArticuloTabla> buscarArticulo(long codigo) {
        List<ArticuloTabla> listaArticulos = new ArrayList<>()
        Articulo.findAllByCodigoArticulo(codigo).each { a ->
            ArticuloTabla at = new ArticuloTabla()
            at.codigo = a.codigoArticulo
            at.descripcion = a.descripcion
            at.precio = a.precio
            at.cantidad = a.cantidadDisponible
            at.articulo = a
            def suplidorSeleccionado = new Suplidor(tiempoEntrega: 10000)
            a.suplidores.each { s ->
                Suplidor sup = Suplidor.findByCodigoSuplidor(s)
                if (sup.tiempoEntrega < suplidorSeleccionado.tiempoEntrega)
                    suplidorSeleccionado = sup
            }
            at.suplidor = suplidorSeleccionado

            listaArticulos.add(at)
        }
        return listaArticulos
    }

    public int count() {
        return Articulo.findAll().size()
    }

}