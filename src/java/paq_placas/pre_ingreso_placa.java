/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_ingreso_placa extends Pantalla{

private Tabla tab_placa = new Tabla();
private Tabla tab_ingreso = new Tabla();
private Tabla tab_consulta = new Tabla();

    public pre_ingreso_placa() {
        int i;
                
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        tab_ingreso.setId("tab_ingreso");
        tab_ingreso.setTabla("TRANS_INGRESOS_PLACAS", "IDE_INGRESO_PLACAS", 1);
        tab_ingreso.setHeader("Acta Ingresos de Placas");
        tab_ingreso.getColumna("IDE_INGRESO_PLACAS").setNombreVisual("ID");
        tab_ingreso.getColumna("FECHA_ENVIO_ACTA").setNombreVisual("Fecha de Envio");
        tab_ingreso.getColumna("FECHA_REGISTRO_ACTA").setNombreVisual("Fecha de Registro");
        tab_ingreso.getColumna("ANO").setNombreVisual("Año");
        tab_ingreso.getColumna("NUMERO_ACTA").setNombreVisual("Nro. Acta");
        tab_ingreso.getColumna("NUMERO_ACTA").setMascara("999-9999-99-999");
        tab_ingreso.getColumna("ENTREGADO_ACTA").setNombreVisual("Quien Entrega");
        tab_ingreso.getColumna("RECIBIDO_ACTA").setNombreVisual("Quien Recibe");
        tab_ingreso.getColumna("fecha_envio_acta").setValorDefecto(utilitario.getFechaActual());
        tab_ingreso.getColumna("fecha_registro_acta").setValorDefecto(utilitario.getFechaActual());      
        tab_ingreso.getColumna("fecha_registro_acta").setLectura(true);
        tab_ingreso.getColumna("usu_ingreso").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_ingreso.getColumna("usu_ingreso").setVisible(false);
        tab_ingreso.setTipoFormulario(true);
        tab_ingreso.getGrid().setColumns(4);
        tab_ingreso.dibujar();
        tab_ingreso.agregarRelacion(tab_placa);
        PanelTabla pat_panel=new PanelTabla(); 
        pat_panel.setPanelTabla(tab_ingreso);
        
        tab_placa.setId("tab_placa");
        tab_placa.setTabla("TRANS_PLACA", "IDE_PLACA", 2);
        tab_placa.setHeader("Placas");
        tab_placa.getColumna("cedula_ruc_propietario").setVisible(false);
        tab_placa.getColumna("nombre_propietario").setVisible(false);
        tab_placa.getColumna("fecha_entrega_placa").setVisible(false);
        tab_placa.getColumna("ide_placa").setNombreVisual("ID");
        tab_placa.getColumna("ide_tipo_vehiculo").setNombreVisual("Tipo Vehiculo");
        tab_placa.getColumna("ide_tipo_servicio").setNombreVisual("Tipo Servicio");
        tab_placa.getColumna("ide_tipo_placa").setNombreVisual("Tipo Placa");
        tab_placa.getColumna("ide_tipo_estado").setNombreVisual("Estado");
        tab_placa.getColumna("placa").setNombreVisual("Nro. Placa");
        tab_placa.getColumna("FECHA_REGISTRO_placa").setNombreVisual("Fecha de Registro");
        tab_placa.getColumna("fecha_registro_placa").setValorDefecto(utilitario.getFechaActual());
        tab_placa.getColumna("fecha_registro_placa").setLectura(true);
        tab_placa.getColumna("ide_tipo_vehiculo").setCombo("select ide_tipo_vehiculo,des_tipo_vehiculo from trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        tab_placa.getColumna("ide_tipo_servicio").setCombo("select ide_tipo_servicio,DESCRIPCION_SERVICIO from trans_tipo_servicio");
        tab_placa.getColumna("ide_tipo_placa").setCombo("select ide_tipo_placa,DESCRIPCION_TIPO from trans_tipo_placa");
        tab_placa.getColumna("ide_tipo_estado").setCombo("select ide_tipo_estado,DESCRIPCION_ESTADO from trans_tipo_estado"); 
        tab_placa.dibujar();
        PanelTabla pat_panel1=new PanelTabla(); 
        pat_panel1.setPanelTabla(tab_placa);
             
        Division div = new Division();
        div.dividir2(pat_panel, pat_panel1, "30%", "h");
        agregarComponente(div);

    }

    @Override
    public void insertar() {
    utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_ingreso.guardar()) {
            if (tab_placa.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
    utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_ingreso() {
        return tab_ingreso;
    }

    public void setTab_ingreso(Tabla tab_ingreso) {
        this.tab_ingreso = tab_ingreso;
    }

    public Tabla getTab_placa() {
        return tab_placa;
    }

    public void setTab_placa(Tabla tab_placa) {
        this.tab_placa = tab_placa;
    }
    
}
