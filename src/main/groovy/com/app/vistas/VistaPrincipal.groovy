package com.app.vistas

import com.app.encapsulados.ArticuloTabla

import com.app.encapsulados.OrdenTabla
import com.vaadin.grails.Grails
import com.vaadin.ui.*
import com.vaadin.ui.renderers.ButtonRenderer
import com.vaadin.ui.renderers.ClickableRenderer
import com.vaadin.ui.themes.ValoTheme
import grails.mongodb.ArticuloService
import grails.mongodb.DetalleOrden
import grails.mongodb.OrdenCompraService
import grails.mongodb.Suplidor

import java.time.ZoneId

class VistaPrincipal extends VerticalLayout {


    public static final String ENTRADA = "ENTRADA"
    public static final String SALIDA = "SALIDA"

    VistaPrincipal() {
        setSizeFull()
        //addComponent(construirHeader())
        addComponent(construirBody())

    }

    private Component construirHeader() {
        HorizontalLayout header = new HorizontalLayout()
        Label titleLabel = new Label("Sistema de pedidos")
        titleLabel.addStyleName(ValoTheme.LABEL_H1)
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN)
        header.addComponent(titleLabel)
        return header;
    }

    private Component construirBody() {
        VerticalLayout body = new VerticalLayout()
        body.setSizeFull()
        TabSheet tabSheet = new TabSheet()
        tabSheet.setSizeFull()
        tabSheet.addTab(construirMovimientos(), "Movimientos de inventario")
        tabSheet.addTab(construirPedidos(), "Pedidos Automaticos")
        tabSheet.addTab(construirArticulos(), "Inventario Articulos")
        tabSheet.addTab(construirOrdenCompra(), "Pedidos Pendientes")

        body.addComponent(tabSheet)
        return body
    }

    private Component construirMovimientos() {
        VerticalLayout layout = new VerticalLayout()
        layout.setSizeUndefined()

        TextField tfCodigoMovimiento = new TextField("No.Movimiento:")
        tfCodigoMovimiento.requiredIndicatorVisible = true

        ComboBox cbTipoMovimiento = new ComboBox("Tipo de Movimiento:")
        cbTipoMovimiento.requiredIndicatorVisible = true
        cbTipoMovimiento.items = [ENTRADA, SALIDA]
        cbTipoMovimiento.selectedItem = ENTRADA

        TextField tfArticulo = new TextField("No.Articulo:")
        tfArticulo.requiredIndicatorVisible = true


        TextField tfCantidad = new TextField("Cantidad:")
        tfCantidad.requiredIndicatorVisible = true

        // agregando campos al layout
        layout.addComponent(tfCodigoMovimiento)
        layout.addComponent(cbTipoMovimiento)
        layout.addComponent(tfArticulo)
        layout.addComponent(tfCantidad)

        Button btnAceptar = new Button("Procesar Movmiento")
        btnAceptar.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {

                try {

                    long codigo = tfCodigoMovimiento.getValue().toString().toLong()
                    String tipo = cbTipoMovimiento.value.toString()
                    long articulo = tfArticulo.getValue().toString().toLong()
                    long cantidad = tfCantidad.getValue().toString().toLong()
                    def oc = Grails.get(OrdenCompraService)
                    oc.procesarMovimiento(codigo, tipo, articulo, cantidad)
                    tfCodigoMovimiento.clear()
                    cbTipoMovimiento.selectedItem = ENTRADA
                    tfArticulo.clear()
                    tfCantidad.clear()
                } catch (Exception e) {
                    e.printStackTrace()
                }
            }
        })

        //Boton de cancelar limpia la pantalla
        Button btnCancelar = new Button("Cancelar")
        btnCancelar.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                tfCodigoMovimiento.clear()
                cbTipoMovimiento.selectedItem = ENTRADA
                tfArticulo.clear()
                tfCantidad.clear()
            }
        })
        //creando botones
        HorizontalLayout hl = new HorizontalLayout()
        hl.addComponents(btnCancelar, btnAceptar)
        hl.setComponentAlignment(btnCancelar, Alignment.BOTTOM_LEFT)
        hl.setComponentAlignment(btnAceptar, Alignment.BOTTOM_RIGHT)
        layout.addComponent(hl)

        return layout

    }

    private Component construirPedidos() {

        List<ArticuloTabla> articulos = new ArrayList<>()
        def suplidor // suplidor mas comun

        VerticalLayout layout = new VerticalLayout()
        TextField tfOrdenCompra = new TextField("No.Orden Compra:")
        tfOrdenCompra.requiredIndicatorVisible = true

        DateField pdfFechaInicio = new DateField("Fecha Inicio:")
        pdfFechaInicio.setStyleName(ValoTheme.DATEFIELD_ALIGN_CENTER)
        pdfFechaInicio.setWidth("150px")

        //buscador
        HorizontalLayout hlBuscador = new HorizontalLayout()
        hlBuscador.setSizeUndefined()
        hlBuscador.setSpacing(true)
        hlBuscador.setMargin(false)
        TextField tfBuscar = new TextField("Codigo Articulo:")
        tfBuscar.setWidth("200px ")
        tfBuscar.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        TextField tfCantidad = new TextField("Cantidad:")
        tfCantidad.setWidth("200px ")

        Button btnAgregar = new Button("Agregar")
        btnAgregar.setStyleName(ValoTheme.BUTTON_PRIMARY)
        btnAgregar.addStyleName(ValoTheme.BUTTON_SMALL)
        hlBuscador.addComponents(tfBuscar,tfCantidad, btnAgregar)
        hlBuscador.setComponentAlignment(btnAgregar, Alignment.BOTTOM_LEFT)

        hlBuscador.setSizeUndefined()

        Grid<ArticuloTabla> grid = new Grid<>(ArticuloTabla.class)
        grid.setSizeFull()
        grid.removeColumn("metaClass")
        grid.removeColumn("articulo")


        TextField tfSuplidor = new TextField("Suplidor:")
        tfSuplidor.readOnly = true

        TextField tfMonto = new TextField("Monto Total:")
        tfMonto.readOnly = true

        //acciones de los componentes
        //botones del grid
        grid.getColumn("boton").setRenderer(new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
            @Override
            void click(ClickableRenderer.RendererClickEvent e) {
                ArticuloTabla at = (ArticuloTabla) e.item
                articulos.remove(at)
                grid.setItems(articulos)
                BigDecimal monto = BigDecimal.ZERO
                articulos.each { a -> monto = monto.add(a.precio * new BigDecimal(a.cantidad))}
                tfMonto.value = monto.toString()
            }
        }))
        //boton de buscar
        def suplidores = [:]
        //Suplidor mayorSuplidor = new Suplidor(codigoSuplidor: 0)
        btnAgregar.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                articulos.addAll(Grails.get(ArticuloService).buscarArticulo(tfBuscar.value.toString().toLong(),tfCantidad.value.toString().toLong()))
                //mayorSuplidor = articulos.get(0).suplidor
                int mayor = 1
                BigDecimal monto = BigDecimal.ZERO
                articulos.each { a ->
                    monto = monto.add(a.precio* new BigDecimal(a.cantidad))
                    suplidores.put(a.codigo.toString(),a.suplidor)
                }
                grid.setItems(articulos)
                tfMonto.value = monto.toString()
                //tfSuplidor.value = mayorSuplidor.descripcion
            }
        })

        Button btnAceptar = new Button("Procesar Movmiento")
        btnAceptar.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    long codigo = tfOrdenCompra.value.toString().toLong()
                    Date fecha = Date.from(pdfFechaInicio.value.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    BigDecimal monto = tfMonto.value.toString().toBigDecimal()

                    def oc = Grails.get(OrdenCompraService)
                    suplidores.each {s->
                        long supl = ((Suplidor)s.value).codigoSuplidor
                        List<DetalleOrden> detalles = new ArrayList<>()
                        articulos.each {a->
                            if(a.suplidor.codigoSuplidor == supl) {
                                DetalleOrden d = new DetalleOrden()
                                d.codigoArticulo = a.articulo.codigoArticulo
                                d.descripcion = a.articulo.descripcion
                                d.UNIDAD = a.articulo.UNIDAD
                                d.precio = a.articulo.precio
                                d.cantidad = a.cantidad
                                detalles.add(d)
                            }
                        }
                        oc.procesarOrdenCompra(codigo, supl, fecha, monto, detalles)
                    }

                    //limpiando
                    tfOrdenCompra.clear()
                    pdfFechaInicio.clear()
                    tfBuscar.clear()
                    grid.setItems(new ArrayList<ArticuloTabla>())
                    tfSuplidor.clear()
                    tfMonto.clear()
                } catch (Exception e) {
                    e.printStackTrace()
                }
            }
        })

        //Boton de cancelar limpia la pantalla
        Button btnCancelar = new Button("Cancelar")
        btnCancelar.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                tfOrdenCompra.clear()
                pdfFechaInicio.clear()
                tfBuscar.clear()
                grid.setItems(new ArrayList<ArticuloTabla>())
                tfSuplidor.clear()
                tfMonto.clear()
            }
        })
        HorizontalLayout hlBotones = new HorizontalLayout()
        hlBotones.addComponents(btnCancelar, btnAceptar)
        hlBotones.setComponentAlignment(btnCancelar, Alignment.BOTTOM_LEFT)
        hlBotones.setComponentAlignment(btnAceptar, Alignment.BOTTOM_RIGHT)

        layout.addComponents(tfOrdenCompra, pdfFechaInicio, hlBuscador, grid, tfSuplidor, tfMonto,hlBotones)
        return layout
    }

    private Component construirArticulos() {

        VerticalLayout layout = new VerticalLayout()
        Button btnRefresh = new Button("Refresh")
        List<ArticuloTabla> articulos = new ArrayList<>()
        Grid<ArticuloTabla> grid = new Grid<>(ArticuloTabla.class)
        grid.setSizeFull()
        grid.removeColumn("metaClass")
        grid.removeColumn("articulo")
        grid.removeColumn("boton")

        articulos = Grails.get(ArticuloService).buscarTodo()
        grid.setItems(articulos)

        btnRefresh.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                articulos.removeAll()
                articulos = Grails.get(ArticuloService).buscarTodo()
                grid.setItems(articulos)
            }
        })
        layout.addComponents(grid, btnRefresh)
        return layout
    }

    private Component construirOrdenCompra() {

        VerticalLayout layout = new VerticalLayout()
        Button btnRefresh = new Button("Refresh")
        List<OrdenTabla> articulos = new ArrayList<>()
        Grid<OrdenTabla> grid = new Grid<>(OrdenTabla.class)
        grid.setSizeFull()
        grid.removeColumn("metaClass")

        articulos = Grails.get(OrdenCompraService).buscarTodo()
        grid.setItems(articulos)

        btnRefresh.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                articulos.removeAll()
                articulos = Grails.get(OrdenCompraService).buscarTodo()
                grid.setItems(articulos)
            }
        })
        layout.addComponents(grid, btnRefresh)
        return layout
    }


}
