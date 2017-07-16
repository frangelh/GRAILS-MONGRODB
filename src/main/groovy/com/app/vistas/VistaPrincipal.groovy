package com.app.vistas

import com.app.encapsulados.ArticuloTabla
import com.vaadin.grails.Grails
import com.vaadin.ui.*
import com.vaadin.ui.renderers.ButtonRenderer
import com.vaadin.ui.renderers.ClickableRenderer
import com.vaadin.ui.themes.ValoTheme
import grails.mongodb.ArticuloService
import grails.mongodb.OrdenCompraService
import grails.mongodb.Suplidor

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
        HorizontalLayout hl = new HorizontalLayout()
        TextField tfBuscar = new TextField("Codigo Articulo:")
        tfBuscar.setWidth("200px ")
        tfBuscar.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        Button btnBuscar = new Button("Agregar")
        btnBuscar.setStyleName(ValoTheme.BUTTON_PRIMARY)
        btnBuscar.addStyleName(ValoTheme.BUTTON_SMALL)

        hl.addComponents(tfBuscar, btnBuscar)
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
                articulos.each {a-> monto = monto.add(a.precio)}
                tfMonto.value = monto.toString()
            }
        }))
        //boton de buscar
        btnBuscar.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                articulos.addAll(Grails.get(ArticuloService).buscarArticulo(tfBuscar.value.toString().toLong()))
                grid.setItems(articulos)
                BigDecimal monto = BigDecimal.ZERO
                articulos.each {a-> monto = monto.add(a.precio)}
                tfMonto.value = monto.toString()

            }
        })



        layout.addComponents(tfOrdenCompra, pdfFechaInicio, hl, grid, tfSuplidor, tfMonto)
        return layout
    }


}
