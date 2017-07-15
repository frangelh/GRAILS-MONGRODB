package com.app.vistas

import com.vaadin.grails.Grails
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
import grails.mongodb.OrdenCompraService

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
        tabSheet.setSizeUndefined()
        tabSheet.addTab(construirMovimientos(), "Movimientos de inventario")
        tabSheet.addTab(construirPedidos(), "Pedidos Automaticos")
        body.addComponent(tabSheet)
        return body
    }

    private Component construirMovimientos() {
        VerticalLayout layout = new VerticalLayout()
        layout.setSizeFull()

        TextField tfCodigoMovimiento = new TextField("No.MovimientoRest:")
        tfCodigoMovimiento.requiredIndicatorVisible = true

        ComboBox cbTipoMovimiento = new ComboBox("Tipo de MovimientoRest:")
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
        btnAceptar.addStyleName(ValoTheme.BUTTON_FRIENDLY)
        btnAceptar.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {


                try {
                    def oc = Grails.get(OrdenCompraService)
                    oc.procesarMovimiento(tfCodigoMovimiento.getValue().toString().toLong(),
                            cbTipoMovimiento.selectedItem.toString(),
                            tfArticulo.getValue().toString().toLong(),
                            tfCantidad.getValue().toString().toLong()

                    )
                } catch (Exception e) {
                    e.printStackTrace()
                }
                /* try {
                     //TODO: revisar existencia y modificar tabla de Articulo



                      def movimiento = new MovimientoInventario(
                              codigoMovimiento: tfCodigoMovimiento.getValue().toString().toLong(),
                              tipoMovimiento: cbTipoMovimiento.selectedItem.toString(),
                              codigoArticulo: tfArticulo.getValue().toString().toLong(),
                              cantidad: tfCantidad.getValue().toString().toLong()
                      )
                     movimiento.insert()

                 } catch (Exception e) {
                     println("HUBO UN ERROR...")
                     e.printStackTrace()
                 }*/

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
        VerticalLayout layout = new VerticalLayout()

        return layout
    }


}
