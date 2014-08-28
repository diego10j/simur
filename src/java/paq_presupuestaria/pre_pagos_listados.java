/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_pagos_listados extends Pantalla{

   //declaracion de conexion
    private Conexion con_postgres= new Conexion();
    
    //declaracion de tablas
    private Tabla tab_consulta =  new Tabla();
    private Tabla tab_comprobante = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_detalle1 = new Tabla();
    private SeleccionTabla set_comprobante = new SeleccionTabla();
    private SeleccionTabla set_lista = new SeleccionTabla();
    
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();//cabecera
    private Panel pan_opcion1 = new Panel();//detalle
    private Panel pan_opcion2 = new Panel();//componentes
    private Panel pan_opcion3 = new Panel();//listado
    
    //para busqueda
    private Texto txt_buscar = new Texto();
    private Texto txt_buscar1 = new Texto();
    private Texto txt_num_listado = new Texto();
    
    private Calendario cal_fecha = new Calendario();
    private Calendario cal_fecha1 = new Calendario();
        //Auto completar
    private AutoCompletar aut_busca = new AutoCompletar();
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
        
    public pre_pagos_listados() {
        
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_financiero.png");
        agregarComponente(quinde);
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //Creación de Botones; Busqueda,Limpieza
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
        //Auto complentar en el formulario
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT ide_listado,fecha_listado,ci_envia,responsable_envia,devolucion,estado,usuario_ingre_envia\n" +
                                   "FROM tes_comprobante_pago_listado");
        aut_busca.setSize(100);
        bar_botones.agregarComponente(new Etiqueta("Busca Listado:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        //Creación de Divisiones
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader(" COMPROBANTE DE PAGOS POR LISTADOS ");
        agregarComponente(pan_opcion);
        
        //Busqueda de comprobantes
        Grid gri_busca1 = new Grid();
        gri_busca1.setColumns(2);
        txt_buscar.setSize(20);
        gri_busca1.getChildren().add(new Etiqueta("# ITEM :"));
        gri_busca1.getChildren().add(txt_buscar);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEntrega");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca1.getChildren().add(bot_buscar);
        
        set_comprobante.setId("set_comprobante");
        set_comprobante.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_comprobante.setSeleccionTabla("SELECT ide_listado,fecha_listado,ci_envia,responsable_envia,devolucion,estado FROM tes_comprobante_pago_listado where IDE_LISTADO=-1", "IDE_LISTADO");
        set_comprobante.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_comprobante.getTab_seleccion().setRows(5);
        set_comprobante.setRadio();
        set_comprobante.setWidth("40%");
        set_comprobante.setHeight("30%");
        set_comprobante.getGri_cuerpo().setHeader(gri_busca1);
        set_comprobante.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_comprobante.setHeader("BUSCAR LISTADO O ITEM  A PAGAR");
        agregarComponente(set_comprobante);
        
        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
        Grupo gru_lis = new Grupo();
        gru_lis.getChildren().add(new Etiqueta("FECHA: "));
        gru_lis.getChildren().add(cal_fecha1);
        Boton bot_lista = new Boton();
        bot_lista.setValue("Buscar");
        bot_lista.setIcon("ui-icon-search");
        bot_lista.setMetodo("buscarColumna");
        bar_botones.agregarBoton(bot_lista);
        gru_lis.getChildren().add(bot_lista);
        
        set_lista.setId("set_lista");
        set_lista.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_lista.setSeleccionTabla("SELECT DISTINCT on (num_transferencia)ide_detalle_listado,num_transferencia FROM tes_detalle_comprobante_pago_listado WHERE ide_detalle_listado=-1  order by num_transferencia", "ide_detalle_listado");
        set_lista.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_lista.getTab_seleccion().setRows(10);
        set_lista.setRadio();
        set_lista.setWidth("20%");
        set_lista.setHeight("40%");
        set_lista.getGri_cuerpo().setHeader(gru_lis);
        set_lista.getBot_aceptar().setMetodo("aceptoAnticipo");
        set_lista.setHeader("SELECCIONE LISTADO");
        agregarComponente(set_lista);
    }

     public void buscarColumna() {
        if (cal_fecha1.getValue() != null && cal_fecha1.getValue().toString().isEmpty() == false ) {
            set_lista.getTab_seleccion().setSql("SELECT DISTINCT on (num_transferencia)ide_detalle_listado,num_transferencia FROM tes_detalle_comprobante_pago_listado where fecha_transferencia='"+cal_fecha1.getFecha()+"' order by num_transferencia");
            set_lista.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una fecha", "");
        }
    }
    
    public void abrirBusqueda(){
      set_comprobante.dibujar();
      txt_buscar.limpiar();
      set_comprobante.getTab_seleccion().limpiar();
      limpiarPanel();
    }
    
    public void buscarEntrega() {
      if (txt_buscar.getValue() != null && txt_buscar.getValue().toString().isEmpty() == false) {
                 set_comprobante.getTab_seleccion().setSql("SELECT ide_listado,fecha_listado,ci_envia,responsable_envia,devolucion,estado \n" +
                                                            "FROM tes_comprobante_pago_listado WHERE item =" + txt_buscar.getValue());
                 set_comprobante.getTab_seleccion().ejecutarSql();
                 limpiar();
          } else {
                 utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
                }
    }
    
    public void aceptarBusqueda() {
        limpiarPanel();
      if (set_comprobante.getValorSeleccionado() != null) {
             aut_busca.setValor(set_comprobante.getValorSeleccionado());
             set_comprobante.cerrar();
             dibujarLista();
             utilitario.addUpdate("aut_busca,pan_opcion");
         } else {
                utilitario.agregarMensajeInfo("Debe seleccionar un listado", "");
                }
    }
    
    //limpieza paneles y abrir busqueda
    public void limpiar() {
      aut_busca.limpiar();
      utilitario.addUpdate("aut_busca");
    }  

    private void limpiarPanel() {
        //borra el contenido de la división central
      pan_opcion1.getChildren().clear();
      pan_opcion.getChildren().clear();
    }
    
    //Para Pagos de Comprobantes    
    public void dibujarLista(){
     if (aut_busca.getValue() != null) {
         limpiarPanel();
        //comprobante pago listado
        tab_comprobante.setId("tab_comprobante");
        tab_comprobante.setConexion(con_postgres);
        tab_comprobante.setTabla("tes_comprobante_pago_listado", "ide_listado", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_comprobante.setCondicion("ide_listado=-1");
        } else {
            tab_comprobante.setCondicion("ide_listado=" + aut_busca.getValor());
        }
        tab_comprobante.getColumna("ci_paga").setLectura(true);
        tab_comprobante.getColumna("responsable_paga").setLectura(true);
        tab_comprobante.getColumna("FECHA_LISTADO").setLectura(true);
        tab_comprobante.getColumna("CI_ENVIA").setLectura(true);
        tab_comprobante.getColumna("RESPONSABLE_ENVIA").setLectura(true);
        tab_comprobante.getColumna("ESTADO").setLectura(true);
        tab_comprobante.getColumna("DEVOLUCION").setLectura(false);
        
        tab_comprobante.getColumna("USUARIO_INGRE_ENVIA").setVisible(false);
        tab_comprobante.getColumna("USUARIO_ACTUA_ENVIA").setVisible(false);
        tab_comprobante.getColumna("FECHA_PAGADO").setVisible(false);
        tab_comprobante.getColumna("IP_INGRE_ENVIA").setVisible(false);
        tab_comprobante.getColumna("IP_ACTUA_ENVIA").setVisible(false);
        tab_comprobante.getColumna("USUARIO_ACTUA_PAGA").setVisible(false);
        tab_comprobante.getColumna("IP_ACTUA_PAGA").setVisible(false);
        tab_comprobante.getColumna("FECHA_DEVOLUCION").setVisible(false);
        tab_comprobante.getColumna("CI_DEVOLUCION").setVisible(false);
        tab_comprobante.getColumna("USUARIO_ACTUA_DEVOLUCION").setVisible(false);
        tab_comprobante.getColumna("IP_ACTUA_DEVOLUCION").setVisible(false);
        tab_comprobante.getColumna("ESTADO").setCombo("SELECT ide_estado_listado,estado FROM tes_estado_listado");
        tab_comprobante.setTipoFormulario(true);
        tab_comprobante.agregarRelacion(tab_detalle);
        tab_comprobante.getGrid().setColumns(6);
        tab_comprobante.dibujar();
        PanelTabla tcp = new PanelTabla();
        tcp.setPanelTabla(tab_comprobante);
        
        //tabla detalle
        tab_detalle.setId("tab_detalle");
        tab_detalle.setConexion(con_postgres);
        tab_detalle.setSql("SELECT\n" +
                            "d.ide_detalle_listado,\n" +
                            "d.ide_listado,\n" +
                            "d.item,\n" +
                            "d.comprobante,\n" +
                            "d.cedula_pass_beneficiario,\n" +
                            "d.nombre_beneficiario,\n" +
                            "d.valor,\n" +
                            "d.usuario_ingre_envia,\n" +
                            "d.usuario_actua_envia,\n" +
                            "d.ip_ingre_envia,\n" +
                            "d.ip_actua_envia,\n" +
                            "d.numero_cuenta,\n" +
                            "d.ban_nombre,\n" +
                            "d.tipo_cuenta,\n" +
                            "d.usuario_actua_pagado,\n" +
                            "d.ip_actua_pagado,\n" +
                            "d.usuario_actua_devolucion,\n" +
                            "d.ip_actua_devolucion,\n" +
                            "null as proceso\n" +
                            "FROM\n" +
                            "tes_detalle_comprobante_pago_listado AS d\n" +
                            "where ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'ENVIADO')");
        tab_detalle.setCampoPrimaria("ide_detalle_listado");
        tab_detalle.setCampoOrden("ide_listado");
        List lista = new ArrayList();
        Object fila2[] = {
            "2", "ACREDITAR"
        };
        Object fila3[] = {
            "3", "DEVOLVER"
        };
        lista.add(fila2);;
        lista.add(fila3);;
        tab_detalle.getColumna("proceso").setRadio(lista, " ");
        tab_detalle.getColumna("ide_detalle_listado").setVisible(false);
        tab_detalle.getColumna("item").setVisible(false);
        tab_detalle.getColumna("USUARIO_ACTUA_ENVIA").setVisible(false);
        tab_detalle.getColumna("IP_ACTUA_ENVIA").setVisible(false);
        tab_detalle.getColumna("IP_INGRE_ENVIA").setVisible(false);
        tab_detalle.getColumna("USUARIO_ACTUA_DEVOLUCION").setVisible(false);
        tab_detalle.getColumna("IP_ACTUA_DEVOLUCION").setVisible(false);
        tab_detalle.getColumna("USUARIO_INGRE_ENVIA").setVisible(false);
        tab_detalle.getColumna("USUARIO_ACTUA_PAGADO").setVisible(false);
        tab_detalle.getColumna("IP_ACTUA_PAGADO").setVisible(false);
        
        tab_detalle.setRows(5);
        tab_detalle.dibujar();
        PanelTabla tdd = new PanelTabla();
        tdd.setPanelTabla(tab_detalle);

        //tabla de detalle comprobantes a pagar
        tab_detalle1.setId("tab_detalle1");
        tab_detalle1.setConexion(con_postgres);
        tab_detalle1.setSql("SELECT \n" +
                            "d.ide_detalle_listado, \n" +
                            "d.ide_listado, \n" +
                            "d.item, \n" +
                            "d.comprobante, \n" +
                            "d.cedula_pass_beneficiario, \n" +
                            "d.nombre_beneficiario, \n" +
                            "d.valor, \n" +
                            "d.usuario_ingre_envia, \n" +
                            "d.usuario_actua_envia, \n" +
                            "d.ip_ingre_envia, \n" +
                            "d.ip_actua_envia, \n" +
                            "d.numero_cuenta, \n" +
                            "d.ban_nombre, \n" +
                            "d.tipo_cuenta, \n" +
                            "d.usuario_actua_pagado, \n" +
                            "d.ip_actua_pagado, \n" +
                            "d.usuario_actua_devolucion, \n" +
                            "d.ip_actua_devolucion, \n" +
                            "null as regresar \n" +
                            "FROM \n" +
                            "tes_detalle_comprobante_pago_listado AS d \n" +
                            "where ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'PAGADO') and num_transferencia is null");
        tab_detalle1.setCampoPrimaria("ide_detalle_listado");
        tab_detalle1.setCampoOrden("ide_listado");
        List list = new ArrayList();
        Object fil1[] = {
            "1", " "
        };
        list.add(fil1);;
        tab_detalle1.getColumna("regresar").setRadio(list, " ");
        tab_detalle1.getColumna("ide_detalle_listado").setVisible(false);
        tab_detalle1.getColumna("ide_listado").setVisible(false);
        tab_detalle1.getColumna("item").setVisible(false);
        tab_detalle1.getColumna("USUARIO_ACTUA_ENVIA").setVisible(false);
        tab_detalle1.getColumna("IP_ACTUA_ENVIA").setVisible(false);
        tab_detalle1.getColumna("IP_INGRE_ENVIA").setVisible(false);
        tab_detalle1.getColumna("USUARIO_ACTUA_DEVOLUCION").setVisible(false);
        tab_detalle1.getColumna("IP_ACTUA_DEVOLUCION").setVisible(false);
        tab_detalle1.getColumna("USUARIO_INGRE_ENVIA").setVisible(false);
        tab_detalle1.getColumna("USUARIO_ACTUA_PAGADO").setVisible(false);
        tab_detalle1.getColumna("IP_ACTUA_PAGADO").setVisible(false);
        
        tab_detalle1.setRows(5);
        tab_detalle1.dibujar();
        PanelTabla tda = new PanelTabla();
        tda.setPanelTabla(tab_detalle1);
        
        Division div = new Division();
        div.dividir2(tcp, tdd, "42%", "h");
        
        cal_fecha.setDisabled(true); //Desactiva el cuadro de texto
        Grupo gru = new Grupo();
        pan_opcion.getChildren().add(gru);
        txt_num_listado.setId("txt_num_listado");
        Grid gri_busca = new Grid();
        gri_busca.setColumns(6);
        gri_busca.getChildren().add(new Etiqueta("Fecha : ")); 
        cal_fecha.setFechaActual();
        gri_busca.getChildren().add(cal_fecha);
        gri_busca.getChildren().add(new Etiqueta("# Transferencia : "));    
        gri_busca.getChildren().add(txt_num_listado);
        
        Boton bot_save = new Boton();
        bot_save.setValue("Guardar Listado");
        bot_save.setExcluirLectura(true);
        bot_save.setIcon("ui-icon-disk");
        bot_save.setMetodo("save_lista");
        
        Boton bot_delete = new Boton();
        bot_delete.setValue("Quitar de Listado");
        bot_delete.setExcluirLectura(true);
        bot_delete.setIcon("ui-icon-extlink");
        bot_delete.setMetodo("regresa");
        
        gri_busca.getChildren().add(bot_save);
        gri_busca.getChildren().add(bot_delete);
        agregarComponente(gri_busca);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(div, gri_busca, tda, "48%", "45%", "H");
        pan_opcion.getChildren().add(div_division);
        usuario(Integer.parseInt(tab_comprobante.getValor("ide_listado")));
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione Listado en el autocompletar");
            limpiar();
        }
    }
    
    public void usuario(Integer valor){
      TablaGenerica tab_dato1 = programas.item(Integer.parseInt(set_comprobante.getValorSeleccionado()));
          if (!tab_dato1.isEmpty()) {
              TablaGenerica tab_dato = programas.empleado();
              if (!tab_dato.isEmpty()) {
                   tab_comprobante.setValor("responsable_paga", tab_dato.getValor("nombres"));
                   tab_comprobante.setValor("ci_paga", tab_dato.getValor("cedula_pass"));
                   utilitario.addUpdate("tab_comprobante");
              } else {
                  utilitario.agregarMensajeInfo("El Número de RUC O C.I. ingresado no existe en la base de datos del municipio", "");
              }
          }
    }
    
     //Actualizacion tipo de cuenta
  public void tipoCuenta(){
       
    if (utilitario.validarCedula(tab_detalle1.getValor("cedula_pass_beneficiario"))) {
       TablaGenerica tab_dato = programas.empleado1(tab_detalle1.getValor("cedula_pass_beneficiario"));
         if (!tab_dato.isEmpty()) {
             tab_detalle1.setValor("numero_cuenta", tab_dato.getValor("numero_cuenta"));
             tab_detalle1.setValor("ban_codigo", tab_dato.getValor("cod_banco"));
             tab_detalle1.setValor("tipo_cuenta", tab_dato.getValor("tipo_cuenta"));
             utilitario.addUpdate("tab_detalle1");
         }else {
                utilitario.agregarMensajeInfo("Datos no disponibles", "");
            }
     } else if (utilitario.validarRUC(tab_detalle1.getValor("cedula_pass_beneficiario"))) {
         TablaGenerica tab_dato = programas.proveedor(tab_detalle1.getValor("cedula_pass_beneficiario"));
         if (!tab_dato.isEmpty()) {
             tab_detalle1.setValor("numero_cuenta", tab_dato.getValor("numero_cuenta"));
             tab_detalle1.setValor("ban_codigo", tab_dato.getValor("ban_codigo"));
             tab_detalle1.setValor("tipo_cuenta", tab_dato.getValor("tipo_cuenta"));
             utilitario.addUpdate("tab_detalle1");
         }else {
                utilitario.agregarMensajeInfo("Datos no disponibles", "");
            }
     } else  {
            utilitario.agregarMensajeError("El Número de Identificación no es válido", "");
            }
  }
    
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
            programas.actuListado(tab_comprobante.getValor("CI_PAGA"), tab_comprobante.getValor("RESPONSABLE_PAGA"), tab_consulta.getValor("NICK_USUA"), 
            Integer.parseInt(tab_comprobante.getValor("IDE_LISTADO")));
            for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                if(tab_detalle.getValor(i, "proceso")!=null){
                    programas.actuaComprobante(tab_detalle.getValor(i, "numero_cuenta"),tab_detalle.getValor(i, "ban_nombre"),
                        tab_detalle.getValor(i, "tipo_cuenta"),  utilitario.getVariable("NICK"),tab_detalle.getValor(i, "comprobante"),Integer.parseInt(tab_detalle.getValor(i, "ide_listado")),Integer.parseInt(tab_detalle.getValor(i, "ide_detalle_listado")));
                }
            }
                tab_detalle.actualizar();
                utilitario.agregarMensaje("Comprobantes", "Listo Para Pago");
                tab_detalle1.actualizar();
    }

    @Override
    public void eliminar() {
    }

    
    public void regresa(){
        for (int i = 0; i < tab_detalle1.getTotalFilas(); i++) {
            if(tab_detalle1.getValor(i, "regresar")!=null){
                programas.regreComprobante(tab_detalle1.getValor(i, "numero_cuenta"),utilitario.getVariable("NICK"),tab_detalle1.getValor(i, "comprobante"),
                        Integer.parseInt(tab_detalle1.getValor(i, "ide_listado")),Integer.parseInt(tab_detalle1.getValor(i, "ide_detalle_listado")));
            }
        }
        tab_detalle1.actualizar();
        utilitario.agregarMensaje("Comprobante", "Regreso a Listado");
        tab_detalle.actualizar();
    }
    
    public void save_lista(){
        for (int i = 0; i < tab_detalle1.getTotalFilas(); i++) {
            programas.numTransComprobante(txt_num_listado.getValue()+"", cal_fecha.getFecha(), tab_detalle1.getValor(i, "comprobante"),Integer.parseInt(tab_detalle1.getValor(i, "ide_listado"))
                    ,Integer.parseInt(tab_detalle1.getValor(i, "ide_detalle_listado")));
        }
        utilitario.agregarMensaje("Comprobante", "Generado");
        tab_detalle1.actualizar();
    }
    
        /*CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
        @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        cal_fecha1.setFechaActual();
        switch (rep_reporte.getNombre()) {
           case "LISTA DE ACREDITACION":
                 set_lista.dibujar();
                set_lista.getTab_seleccion().limpiar();
          break;
        }
    } 
    
      public void aceptoAnticipo(){
        switch (rep_reporte.getNombre()) {
               case "LISTA DE ACREDITACION":
                    TablaGenerica tab_dato = programas.getTranferencia(Integer.parseInt(set_lista.getValorSeleccionado()));
               if (!tab_dato.isEmpty()) {
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    p_parametros.put("fecha_acre", cal_fecha1.getFecha()+"");
                    p_parametros.put("num_tran", tab_dato.getValor("num_transferencia")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
               break;
        }
    }
      
    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_comprobante() {
        return tab_comprobante;
    }

    public void setTab_comprobante(Tabla tab_comprobante) {
        this.tab_comprobante = tab_comprobante;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }

    public Tabla getTab_detalle1() {
        return tab_detalle1;
    }

    public void setTab_detalle1(Tabla tab_detalle1) {
        this.tab_detalle1 = tab_detalle1;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public SeleccionTabla getSet_comprobante() {
        return set_comprobante;
    }

    public void setSet_comprobante(SeleccionTabla set_comprobante) {
        this.set_comprobante = set_comprobante;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }

    public SeleccionTabla getSet_lista() {
        return set_lista;
    }

    public void setSet_lista(SeleccionTabla set_lista) {
        this.set_lista = set_lista;
    }
    
}