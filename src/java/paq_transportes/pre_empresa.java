/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Diego
 */
public class pre_empresa extends Pantalla {

    private Tabla tab_tabla;
    private Tabla tab_permisos;
    private Tabla tab_socios;
    private Tabla tab_vehiculos;
    private Tabla tab_permiso_x_ruta;
    private Tabla tab_permiso_provicional;
    private Tabla tab_recorrido;
    private Tabla tab_seguro;
    private Tabla tab_revision;
    private AutoCompletar aut_empresas = new AutoCompletar();
    private Panel pan_opcion = new Panel();
    private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
    private PanelMenu pam_menu = new PanelMenu();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private SeleccionTabla sel_tab_tipo_vehiculo = new SeleccionTabla();

    public pre_empresa() {

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sef_reporte.setId("sef_reporte");
        agregarComponente(sef_reporte);

        sel_tab_tipo_vehiculo.setId("sel_tab_tipo_vehiculo");
        sel_tab_tipo_vehiculo.setTitle("SELECCION DE AREAS");
        sel_tab_tipo_vehiculo.setSeleccionTabla("SELECT codigo,nombre from trans_tipo where codigo=-1", "codigo");
        sel_tab_tipo_vehiculo.getTab_seleccion().getColumna("nombre").setFiltro(true);
        sel_tab_tipo_vehiculo.getBot_aceptar().setMetodo("aceptarReporte");
        sel_tab_tipo_vehiculo.setRadio();
        agregarComponente(sel_tab_tipo_vehiculo);

        aut_empresas.setId("aut_empresas");
        aut_empresas.setAutoCompletar("select ide_empresa,nombre,ruc from trans_empresa");
        aut_empresas.setMetodoChange("filtrarEmpresa");
        aut_empresas.setSize(70);
        bar_botones.agregarComponente(aut_empresas);

        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        bar_botones.agregarReporte();


        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);

        contruirMenu();

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pam_menu, pan_opcion, "20%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("MENU DE OPCIONES");
        agregarComponente(div_division);
    }

    private void contruirMenu() {
        // SUB MENU 1
        Submenu sum_empleado = new Submenu();
        sum_empleado.setLabel("EMPRESAS DE TRANSPORTE PUBLICO");
        pam_menu.getChildren().add(sum_empleado);

        // ITEM 1 : OPCION 0
        ItemMenu itm_datos_empl = new ItemMenu();
        itm_datos_empl.setValue("DATOS EMPRESA");
        itm_datos_empl.setIcon("ui-icon-contact");
        itm_datos_empl.setMetodo("dibujarEmpresa");
        itm_datos_empl.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_datos_empl);

        // ITEM 2 : OPCION 1
        ItemMenu itm_permisos = new ItemMenu();
        itm_permisos.setValue("PERMISOS");
        itm_permisos.setIcon("ui-icon-key");
        itm_permisos.setMetodo("dibujarPermisos");
        itm_permisos.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_permisos);

        // ITEM 2: OPCION 1
        ItemMenu itm_socios = new ItemMenu();
        itm_socios.setValue("SOCIOS");
        itm_socios.setIcon("ui-icon-person");
        itm_socios.setMetodo("dibujarSocios");
        itm_socios.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_socios);



    }

    public void dibujarPermisos() {
        if (aut_empresas.getValue() != null) {
            str_opcion = "1";
            limpiarPanel();
            tab_permisos = new Tabla();
            tab_permiso_x_ruta = new Tabla();
            tab_permiso_provicional = new Tabla();
            tab_permisos.setId("tab_permisos");
            tab_permisos.setTabla("trans_permiso", "ide_permiso", 2);
            tab_permisos.getColumna("ide_empresa").setVisible(false);
            tab_permisos.setCondicion("ide_empresa=" + aut_empresas.getValor());
            tab_permisos.setTipoFormulario(true);
            tab_permisos.getGrid().setColumns(4);
            tab_permisos.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_permisos.getColumna("fecha_responsable").setVisible(false);
            tab_permisos.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_permisos.getColumna("ip_responsable").setVisible(false);
            tab_permisos.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_permisos.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_permisos.agregarRelacion(tab_permiso_x_ruta);
            tab_permisos.agregarRelacion(tab_permiso_provicional);
            tab_permisos.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_permisos);



            tab_permiso_x_ruta.setId("tab_permiso_x_ruta");
            tab_recorrido = new Tabla();
            tab_permiso_x_ruta.setIdCompleto("tab_tabulador:tab_permiso_x_ruta");
            tab_permiso_x_ruta.setHeader("PERMISOS POR RUTA");
            tab_permiso_x_ruta.setTabla("trans_permiso_x_ruta", "ide_permiso_ruta", 5);
            tab_permiso_x_ruta.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_permiso_x_ruta.getColumna("fecha_responsable").setVisible(false);
            tab_permiso_x_ruta.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_permiso_x_ruta.getColumna("ip_responsable").setVisible(false);
            tab_permiso_x_ruta.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_permiso_x_ruta.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_permiso_x_ruta.setTipoFormulario(true);
            tab_permiso_x_ruta.getGrid().setColumns(6);
            tab_permiso_x_ruta.agregarRelacion(tab_recorrido);
            tab_permiso_x_ruta.dibujar();

            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_permiso_x_ruta);

            tab_recorrido.setId("tab_recorrido");
            tab_recorrido.setIdCompleto("tab_tabulador:tab_recorrido");
            tab_recorrido.setTabla("trans_recorrido", "ide_recorrido", 7);
            tab_recorrido.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_recorrido.getColumna("fecha_responsable").setVisible(false);
            tab_recorrido.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_recorrido.getColumna("ip_responsable").setVisible(false);
            tab_recorrido.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_recorrido.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_recorrido.setHeader("RECORRIDOS");
            tab_recorrido.dibujar();

            PanelTabla pat_panel4 = new PanelTabla();
            pat_panel4.setPanelTabla(tab_recorrido);

            Grupo gri_per_ruta = new Grupo();
            gri_per_ruta.getChildren().add(pat_panel2);
            gri_per_ruta.getChildren().add(pat_panel4);


            Tabulador tab_tabulador = new Tabulador();
            tab_tabulador.setId("tab_tabulador");
            tab_tabulador.agregarTab("PERMISO POR RUTA", gri_per_ruta);

            tab_permiso_provicional.setId("tab_permiso_provicional");
            tab_permiso_provicional.setIdCompleto("tab_tabulador:tab_permiso_provicional");
            tab_permiso_provicional.setTabla("trans_permiso_provicional", "ide_permiso_provicional", 6);
            tab_permiso_provicional.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_permiso_provicional.getColumna("fecha_responsable").setVisible(false);
            tab_permiso_provicional.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_permiso_provicional.getColumna("ip_responsable").setVisible(false);
            tab_permiso_provicional.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_permiso_provicional.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_permiso_provicional.setTipoFormulario(true);
            tab_permiso_provicional.getGrid().setColumns(4);
            tab_permiso_provicional.setHeader("PERMISO PROVICIONAL");
            tab_permiso_provicional.getColumna("ubi_codigo").setCombo("select ubi_codigo,ubi_descri from oceubica order by ubi_descri");

            tab_permiso_provicional.dibujar();

            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_permiso_provicional);
            tab_tabulador.agregarTab("PERMISO PROVICIONAL", pat_panel3);



            Division div_div = new Division();
            div_div.dividir2(pat_panel1, tab_tabulador, "H", "50%");
            try {
                int dou_alto = Integer.parseInt(utilitario.getVariable("ALTO_PANTALLA")) - 53;
                div_div.setStyle("padding: 0;margin: 0;height:" + dou_alto + "px;border:none");
            } catch (Exception e) {
            }

            pan_opcion.setTitle("INFORMACION DEL PERMISO DE OPERACION DE LA EMPRESA");
            pan_opcion.getChildren().add(div_div);
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Empresa en el autocompletar");
            limpiar();
        }
    }

    public void dibujarSocios() {
        if (aut_empresas.getValue() != null) {
            str_opcion = "2";
            limpiarPanel();
            tab_socios = new Tabla();
            tab_vehiculos = new Tabla();
            tab_socios.setId("tab_socios");
            tab_socios.setTabla("trans_socios", "ide_socios", 3);
            tab_socios.getColumna("ide_empresa").setVisible(false);
            tab_socios.setCondicion("ide_empresa=" + aut_empresas.getValor());
            tab_socios.getColumna("ide_tipo_licencia").setCombo("trans_tipo_licencia", "ide_tipo_licencia", "des_tipo_licencia", "");
            tab_socios.setTipoFormulario(true);
            tab_socios.getGrid().setColumns(6);
            tab_socios.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_socios.getColumna("fecha_responsable").setVisible(false);
            tab_socios.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_socios.getColumna("ip_responsable").setVisible(false);
            tab_socios.getColumna(" NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_socios.getColumna(" NOM_RESPONSABLE").setVisible(false);
            tab_socios.agregarRelacion(tab_vehiculos);
            tab_socios.setHeader("SOCIOS");
            tab_socios.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_socios);
            pan_opcion.setTitle("SOCIOS QUE PERTENECEN A LA EMPRESA");

            tab_seguro = new Tabla();
            tab_revision = new Tabla();
            tab_vehiculos.setId("tab_vehiculos");
            tab_vehiculos.setTabla("trans_vehiculos", "ide_vehiculo", 4);
            tab_vehiculos.setHeader("VEHÍCULOS DEL SOCIO");
            tab_vehiculos.setRows(10);
            tab_vehiculos.getColumna("ide_modelo").setCombo("select ide_modelo,des_modelo,marca from trans_modelos mode INNER JOIN trans_marcas marca on mode.ide_marca=marca.ide_marca order by des_modelo,marca");
            tab_vehiculos.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_vehiculos.getColumna("fecha_responsable").setVisible(false);
            tab_vehiculos.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_vehiculos.getColumna("ip_responsable").setVisible(false);
            tab_vehiculos.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_vehiculos.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_vehiculos.setTipoFormulario(true);
            tab_vehiculos.getColumna("FEC_ALTA").setControl("Calendario");
            tab_vehiculos.getColumna("FEC_BAJA").setControl("Calendario");
            tab_vehiculos.getGrid().setColumns(6);
            tab_vehiculos.agregarRelacion(tab_seguro);
            tab_vehiculos.agregarRelacion(tab_revision);
            tab_vehiculos.dibujar();
            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_vehiculos);
            tab_vehiculos.setStyle(null);
            pat_panel3.setStyle(null);

            tab_seguro.setId("tab_seguro");
            tab_seguro.setIdCompleto("tab_tabulador:tab_seguro");
            tab_seguro.setTabla("trans_seguro", "ide_seguro", 8);
            tab_seguro.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_seguro.getColumna("fecha_responsable").setVisible(false);
            tab_seguro.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_seguro.getColumna("ip_responsable").setVisible(false);
            tab_seguro.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_seguro.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_seguro.setHeader("SEGURO DEL VEHÍCULO");
            tab_seguro.setTipoFormulario(true);
            tab_seguro.getGrid().setColumns(4);
            tab_seguro.getColumna("FEC_EMISION").setControl("Calendario");
            tab_seguro.getColumna("FEC_CADUCIDAD").setControl("Calendario");
            tab_seguro.dibujar();

            PanelTabla pat_panel4 = new PanelTabla();
            pat_panel4.setPanelTabla(tab_seguro);
            tab_seguro.setStyle(null);
            pat_panel4.setStyle(null);


            tab_revision.setId("tab_revision");
            tab_revision.setIdCompleto("tab_tabulador:tab_revision");
            tab_revision.setTabla("trans_revision_vehicular", "ide_revision", 9);
            tab_revision.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_revision.getColumna("fecha_responsable").setVisible(false);
            tab_revision.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_revision.getColumna("ip_responsable").setVisible(false);
            tab_revision.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_revision.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_revision.setTipoFormulario(true);
            tab_revision.setHeader("REVISIÓN VEHICULAR");
            tab_revision.getGrid().setColumns(6);
            tab_revision.getColumna("FEC_REVISION").setControl("Calendario");
            tab_revision.dibujar();

            PanelTabla pat_panel5 = new PanelTabla();
            pat_panel5.setPanelTabla(tab_revision);
            tab_revision.setStyle(null);
            pat_panel5.setStyle(null);


            Grupo gru = new Grupo();
            gru.getChildren().add(pat_panel3);
            gru.getChildren().add(pat_panel4);
            gru.getChildren().add(pat_panel5);

            Division div_div = new Division();
            div_div.dividir2(pat_panel2, gru, "H", "50%");
            try {
                int dou_alto = Integer.parseInt(utilitario.getVariable("ALTO_PANTALLA")) - 53;
                div_div.setStyle("padding: 0;margin: 0;height:" + dou_alto + "px;border:none");
            } catch (Exception e) {
            }

            pan_opcion.getChildren().add(div_div);
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Empresa en el autocompletar");
            limpiar();
        }
    }

    public void dibujarEmpresa() {
        str_opcion = "0";
        limpiarPanel();

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("trans_empresa", "ide_empresa", 1);
        if (aut_empresas.getValue() == null) {
            tab_tabla.setCondicion("ide_empresa=-1");
        } else {
            tab_tabla.setCondicion("ide_empresa=" + aut_empresas.getValor());
        }
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getColumna("codigo").setCombo("trans_tipo", "codigo", "nombre", "");
        tab_tabla.getColumna("fecha_responsable").setCalendarioFechaHora();
        tab_tabla.getColumna("fecha_responsable").setVisible(false);
        tab_tabla.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
        tab_tabla.getColumna("ip_responsable").setVisible(false);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.getColumna("FEC_PATENTE").setControl("Calendario");
        tab_tabla.getColumna("FEC_PAGO_OCUPACION_VIA").setControl("Calendario");
        tab_tabla.getColumna(" NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
        tab_tabla.getColumna(" NOM_RESPONSABLE").setVisible(false);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);

        pan_opcion.setTitle("CATASTRO DE EMPRESAS DE TRANSPORTE PÚBLICO");
        pan_opcion.getChildren().add(pat_panel);
    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
        // pan_opcion.getChildren().add(efecto);
    }

    public void limpiar() {
        aut_empresas.limpiar();
        utilitario.addUpdate("aut_empresas");
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }

    public void filtrarEmpresa(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        aut_empresas.onSelect(evt);
        if (str_opcion.equals("0") || str_opcion.isEmpty()) {
            dibujarEmpresa();
        } else if (str_opcion.equals("1")) {
            dibujarPermisos();
        } else if (str_opcion.equals("2")) {
            dibujarSocios();
        }
        utilitario.addUpdate("pan_opcion");
    }

    @Override
    public void insertar() {
        if (str_opcion.equals("0")) {
            if (tab_tabla.isFocus()) {
                aut_empresas.limpiar();
                utilitario.addUpdate("aut_empresas");
                tab_tabla.limpiar();
                tab_tabla.insertar();
                if (tab_tabla.isFilaInsertada()) {
                    tab_tabla.setValor("fecha_responsable", utilitario.getFechaHoraActual());
                }
            }
        } else if (str_opcion.equals("1")) {

            if (tab_permisos.isFocus()) {
                tab_permisos.insertar();
                if (tab_permisos.isFilaInsertada()) {
                    tab_permisos.setValor("ide_empresa", utilitario.getFechaHoraActual());
                }
            } else if (tab_permiso_x_ruta.isFocus()) {
                tab_permiso_x_ruta.insertar();
            } else if (tab_permiso_provicional.isFocus()) {
                tab_permiso_provicional.insertar();
            }
        } else if (str_opcion.equals("2")) {
            if (tab_socios.isFocus()) {
                tab_socios.insertar();
                if (tab_socios.isFilaInsertada()) {
                    tab_socios.setValor("ide_empresa", utilitario.getFechaHoraActual());
                }
            } else if (tab_vehiculos.isFocus()) {
                tab_vehiculos.insertar();
            } else if (tab_revision.isFocus()) {
                tab_revision.insertar();
            } else if (tab_seguro.isFocus()) {
                tab_seguro.insertar();
            }
        }

    }

    @Override
    public void guardar() {
        if (str_opcion.equals("0")) {
            if (tab_tabla.isFilaInsertada()) {
                //Actualiza nuevamente la fecha
                tab_tabla.setValor("fecha_responsable", utilitario.getFechaHoraActual());
            }
            tab_tabla.guardar();
        } else if (str_opcion.equals("1")) {
            if (tab_permisos.isFilaInsertada()) {
                //Actualiza nuevamente la fecha
                tab_permisos.setValor("fecha_responsable", utilitario.getFechaHoraActual());
            }
            tab_permisos.guardar();
            tab_permiso_x_ruta.guardar();
            tab_permiso_provicional.guardar();
        } else if (str_opcion.equals("2")) {
            if (tab_permisos.isFilaInsertada()) {
                //Actualiza nuevamente la fecha
                tab_socios.setValor("fecha_responsable", utilitario.getFechaHoraActual());
            }
            tab_socios.guardar();
            tab_vehiculos.guardar();
            tab_seguro.guardar();
            tab_revision.guardar();
        }
        guardarPantalla();
        aut_empresas.actualizar();
        aut_empresas.setSize(70);
        if (tab_tabla.isFilaInsertada() == false) {
            aut_empresas.setValor(tab_tabla.getValorSeleccionado());
        }
        utilitario.addUpdate("aut_empresas");

    }

    @Override
    public void eliminar() {
        if (str_opcion.equals("0")) {
            if (tab_tabla.isFocus()) {
                if (tab_tabla.eliminar()) {
                    aut_empresas.actualizar();
                    aut_empresas.setSize(70);
                    utilitario.addUpdate("aut_empresas");
                }
            }
        } else if (str_opcion.equals("1")) {
            if (tab_permisos.isFocus()) {
                tab_permisos.eliminar();
            } else if (tab_permiso_x_ruta.isFocus()) {
                tab_permiso_x_ruta.eliminar();
            } else if (tab_permiso_provicional.isFocus()) {
                tab_permiso_provicional.eliminar();
            }
        } else if (str_opcion.equals("2")) {
            if (tab_socios.isFocus()) {
                tab_socios.eliminar();
            } else if (tab_vehiculos.isFocus()) {
                tab_vehiculos.eliminar();
            } else if (tab_seguro.isFocus()) {
                tab_seguro.eliminar();
            } else if (tab_revision.isFocus()) {
                tab_revision.eliminar();
            }
        }
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Tabla getTab_permisos() {
        return tab_permisos;
    }

    public void setTab_permisos(Tabla tab_permisos) {
        this.tab_permisos = tab_permisos;
    }

    public Tabla getTab_socios() {
        return tab_socios;
    }

    public void setTab_socios(Tabla tab_socios) {
        this.tab_socios = tab_socios;
    }

    public Tabla getTab_vehiculos() {
        return tab_vehiculos;
    }

    public void setTab_vehiculos(Tabla tab_vehiculos) {
        this.tab_vehiculos = tab_vehiculos;
    }

    public AutoCompletar getAut_empresas() {
        return aut_empresas;
    }

    public void setAut_empresas(AutoCompletar aut_empresas) {
        this.aut_empresas = aut_empresas;
    }

    public Tabla getTab_permiso_x_ruta() {
        return tab_permiso_x_ruta;
    }

    public void setTab_permiso_x_ruta(Tabla tab_permiso_x_ruta) {
        this.tab_permiso_x_ruta = tab_permiso_x_ruta;
    }

    public Tabla getTab_permiso_provicional() {
        return tab_permiso_provicional;
    }

    public void setTab_permiso_provicional(Tabla tab_permiso_provicional) {
        this.tab_permiso_provicional = tab_permiso_provicional;
    }

    public Tabla getTab_recorrido() {
        return tab_recorrido;
    }

    public void setTab_recorrido(Tabla tab_recorrido) {
        this.tab_recorrido = tab_recorrido;
    }

    public Tabla getTab_seguro() {
        return tab_seguro;
    }

    public void setTab_seguro(Tabla tab_seguro) {
        this.tab_seguro = tab_seguro;
    }

    public Tabla getTab_revision() {
        return tab_revision;
    }

    public void setTab_revision(Tabla tab_revision) {
        this.tab_revision = tab_revision;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_reporte() {
        return sef_reporte;
    }

    public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
        this.sef_reporte = sef_reporte;
    }

    public SeleccionTabla getSel_tab_tipo_vehiculo() {
        return sel_tab_tipo_vehiculo;
    }

    public void setSel_tab_tipo_vehiculo(SeleccionTabla sel_tab_tipo_vehiculo) {
        this.sel_tab_tipo_vehiculo = sel_tab_tipo_vehiculo;
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Empresas Catastradas 1")) {
            if (rep_reporte.isVisible()) {
                p_parametros = new HashMap();
                rep_reporte.cerrar();
                p_parametros.put("titulo", "EMPRESAS CATASTRADAS 1");
                sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_reporte.dibujar();
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Empresas Catastradas 2")) {
            if (rep_reporte.isVisible()) {
                p_parametros = new HashMap();
                sel_tab_tipo_vehiculo.getTab_seleccion().setSql("SELECT codigo,nombre from trans_tipo");
                sel_tab_tipo_vehiculo.getTab_seleccion().ejecutarSql();
                rep_reporte.cerrar();
                sel_tab_tipo_vehiculo.dibujar();                                       
            } else if (sel_tab_tipo_vehiculo.getValorSeleccionado() != null && !sel_tab_tipo_vehiculo.getValorSeleccionado().isEmpty()) {
                p_parametros.put("tipo_vehiculo",Integer.parseInt(sel_tab_tipo_vehiculo.getValorSeleccionado()));           
                p_parametros.put("titulo", "EMPRESAS CATASTRADAS");
                sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sel_tab_tipo_vehiculo.cerrar();
                sef_reporte.dibujar();                
            } else {
                utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos una Tipo de Transporte");
            }
        }
    }
}
