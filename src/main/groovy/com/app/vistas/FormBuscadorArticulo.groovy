package com.app.vistas

import com.app.encapsulados.ArticuloTabla
import com.vaadin.event.ShortcutAction
import com.vaadin.event.ShortcutListener
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.FontAwesome
import com.vaadin.server.Page
import com.vaadin.server.Responsive
import com.vaadin.server.Sizeable
import com.vaadin.ui.*
import com.vaadin.ui.renderers.ButtonRenderer
import com.vaadin.ui.renderers.ClickableRenderer
import com.vaadin.ui.themes.ValoTheme
import grails.mongodb.Articulo
import grails.mongodb.ArticuloService

public class FormBuscadorArticulo extends Window implements View {

    //CotizacionService cotizacionService = new CotizacionService()
    List<ArticuloTabla> articulos
    List<ArticuloTabla> listaArticulos
    List<ArticuloTabla> listaSeleccionada

    List<Articulo> articulosSeleccionados

    private Grid tablaArticulos;
    private Grid tablaSeleccionados;
    Button btnAgregar, btnSalir


    private static final collapsibleColumns = new LinkedHashSet<>();
    TextField tfBuscarPor


    private Callback callback

    public interface Callback {
        public void onDialogResult(List<ArticuloTabla> articulos);
    }

    public FormBuscadorArticulo(Callback callback) {
        this.callback = callback
        this.setId("FormBuscadorArticulo")
        this.setCaption("Buscador de Articulos")
        this.center()
        this.setClosable(true)
        this.setModal(false)
        this.setSizeUndefined()

        listaArticulos = new ArrayList<>()
        listaSeleccionada = new ArrayList<>()
        articulos = new ArrayList<>()

        articulosSeleccionados = new ArrayList<>()
        construirTablaArticulos();
        construirTablaSeleccionados();
        VerticalLayout superior = new VerticalLayout()
        superior.addComponent(buildToolbar());
        superior.addComponent(tablaArticulos);

        superior.setExpandRatio(tablaArticulos, 1);


        VerticalLayout inferior = new VerticalLayout()
        inferior.addComponent(tablaSeleccionados)
        inferior.addComponent(construirControles())

        VerticalLayout vl = new VerticalLayout()
        vl.setMargin(true)
        vl.setSpacing(true)

        inferior.setExpandRatio(tablaSeleccionados, 1);
        vl.addComponents(superior, inferior)
        this.setContent(vl)
    }

    private Component construirControles() {
        btnSalir = new Button("Cancelar (ESC)")
        btnSalir.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        btnSalir.addStyleName(ValoTheme.BUTTON_SMALL)
        btnSalir.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                callback.onDialogResult(null)
                close()
            }
        })
        btnAgregar = new Button("Cargar Articulos")
        btnAgregar.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        btnAgregar.setStyleName(ValoTheme.BUTTON_PRIMARY)
        btnAgregar.addStyleName(ValoTheme.BUTTON_SMALL)

        btnAgregar.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                if (listaSeleccionada.size() > 0) {
                    /* listaSeleccionada.each { s ->
                         Articulo art = s.articulo
                         articulosSeleccionados.add(art)
                     }*/
                    callback.onDialogResult(listaSeleccionada)
                    close()
                }
            }
        })




        HorizontalLayout layout = new HorizontalLayout()
        layout.setSizeUndefined()
        layout.setSpacing(true)
        layout.setWidth(100, Sizeable.Unit.PERCENTAGE)
        layout.addComponents(btnSalir, btnAgregar)
        layout.setComponentAlignment(btnSalir, Alignment.BOTTOM_LEFT)
        layout.setComponentAlignment(btnAgregar, Alignment.BOTTOM_RIGHT)

        return layout

    }

    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.setSpacing(true);
        Responsive.makeResponsive(header);
        HorizontalLayout tools = new HorizontalLayout(ConstruirBuscador());
        tools.setSpacing(true);
        header.addComponent(tools);

        return header;
    }


    private Component ConstruirBuscador() {
        tfBuscarPor = new TextField("Descripcion");

        tfBuscarPor.setIcon(FontAwesome.SEARCH);
        tfBuscarPor.setWidth("200px ")
        tfBuscarPor.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        tfBuscarPor.addShortcutListener(
                new ShortcutListener("Clear", ShortcutAction.KeyCode.ESCAPE, null) {
                    @Override
                    public void handleAction(final Object sender,
                                             final Object target) {
                        tfBuscarPor.setValue("");
                    }
                });

        Button btnBuscar = new Button("Buscar")
        btnBuscar.setStyleName(ValoTheme.BUTTON_PRIMARY)
        btnBuscar.addStyleName(ValoTheme.BUTTON_SMALL)
        btnBuscar.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent event) {
                ArticuloService articuloService = new ArticuloService()
                listaArticulos.clear()
                articulos = articuloService.findAll()
                listaArticulos = articulos
                /*detalle.each { a ->
                    ArticuloTabla at = new ArticuloTabla()
                    at.codigo = a.codigoArticulo
                    at.descripcion = a.descripcion
                    at.precio = a.precio
                    at.cantidad = a.cantidadDisponible
                    at.articulo = a
                    listaArticulos.add(at)
                }*/
                tablaArticulos.setItems(listaArticulos)
            }
        })
        HorizontalLayout hl = new HorizontalLayout()
        hl.addComponents(tfBuscarPor, btnBuscar)
        return hl
    }

    private construirTablaArticulos() {
        tablaArticulos = new Grid<>(ArticuloTabla.class);
        tablaArticulos.setWidth("1000px")
        tablaArticulos.setHeight("300px")
        tablaArticulos.addStyleName(ValoTheme.TABLE_SMALL)
        tablaArticulos.addStyleName(ValoTheme.TABLE_COMPACT)
        tablaArticulos.removeColumn("metaClass")
        tablaArticulos.removeColumn("articulo")
        tablaArticulos.getColumn("boton").setRenderer(new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
            @Override
            void click(ClickableRenderer.RendererClickEvent e) {
                ArticuloTabla at = (ArticuloTabla) e.item
                listaSeleccionada.add(listaArticulos.get(listaArticulos.indexOf(at)))
                tablaArticulos.setItems(listaSeleccionada)
            }
        }))


    }

    private construirTablaSeleccionados() {
        tablaSeleccionados = new Grid<>(ArticuloTabla.class);
        tablaSeleccionados.addStyleName(ValoTheme.TABLE_SMALL)
        tablaSeleccionados.addStyleName(ValoTheme.TABLE_COMPACT)
        tablaSeleccionados.removeColumn("metaClass")
        tablaSeleccionados.removeColumn("articulo")
        tablaSeleccionados.getColumn("boton").setRenderer(new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
            @Override
            void click(ClickableRenderer.RendererClickEvent e) {
                ArticuloTabla at = (ArticuloTabla) e.item
                listaSeleccionada.remove(at)
                tablaSeleccionados.setItems(listaSeleccionada)
            }
        }))


    }

    private boolean defaultColumnsVisible() {
        boolean result = true;
        for (Grid.Column<ArticuloTabla, ?> column : collapsibleColumns) {
            if (column.isHidden() == Page.getCurrent()
                    .getBrowserWindowWidth() < 800) {
                result = false;
            }
        }
        return result;
    }


    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
    }
}
