/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_controlEquipos.ejb.Procesos;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class Programas extends Pantalla {

    private Tabla tabProgramas = new Tabla();
    private Tabla tabLicencia = new Tabla();
    @EJB
    private Procesos accesoDatos = (Procesos) utilitario.instanciarEJB(Procesos.class);

    public Programas() {
        tabProgramas.setId("tabProgramas");
        tabProgramas.setTabla("cei_programas", "progs_codigo", 1);
        tabProgramas.setHeader("LISTADO DE PROGRAMAS");
        tabProgramas.agregarRelacion(tabLicencia);
        tabProgramas.dibujar();
        PanelTabla ptp = new PanelTabla();
        ptp.setPanelTabla(tabProgramas);

        tabLicencia.setId("tabLicencia");
        tabLicencia.setTabla("cei_licencia_programas", "licen_codigo", 2);
        tabLicencia.setHeader("LISTADO DE LICENCIAS POR PROGRAMAS");
        tabLicencia.getColumna("tipo_licencia_codigo").setCombo("SELECT TIPO_LICENCIA_CODIGO,TIPO_LICENCIA_DESCRIPCION FROM CEI_TIPO_LICENCIA");
        tabLicencia.getColumna("tipo_licencia_codigo").setMetodoChange("ActiCasillas");
        List list = new ArrayList();
        Object fila1[] = {
            "1", "OIGN"
        };
        Object fila2[] = {
            "2", "VOLUMEN"
        };
        list.add(fila1);
        list.add(fila2);
        tabLicencia.getColumna("licen_modelo_licencia").setCombo(list);
        tabLicencia.getColumna("licen_fecha_compra").setLectura(true);
        tabLicencia.getColumna("licen_numero_licencia").setLectura(true);
        tabLicencia.getColumna("licen_tiempo_vigencia").setLectura(true);
        tabLicencia.getColumna("licen_cantidad").setLectura(true);
        
        tabLicencia.getColumna("licen_fecha_compra").setValorDefecto(utilitario.getFechaActual());
        tabLicencia.setTipoFormulario(true);
        tabLicencia.dibujar();
        PanelTabla ptl = new PanelTabla();
        ptl.setPanelTabla(tabLicencia);

        Division div = new Division();
        div.dividir2(ptp, ptl, "55%", "H");
        agregarComponente(div);

    }

    public void ActiCasillas() {
        TablaGenerica tadDato = accesoDatos.getTipoLicencia(Integer.parseInt(tabLicencia.getValor("tipo_licencia_codigo")));
        if (!tadDato.isEmpty()) {
            if (tadDato.getValor("TIPO_LICENCIA_DESCRIPCION").equals("PAGADA")) {
                tabLicencia.getColumna("licen_fecha_compra").setLectura(false);
                tabLicencia.getColumna("licen_numero_licencia").setLectura(false);
                tabLicencia.getColumna("licen_tiempo_vigencia").setLectura(false);
                tabLicencia.getColumna("licen_cantidad").setLectura(false);
                utilitario.addUpdate("tabLicencia");
            } else {
                tabLicencia.getColumna("licen_fecha_compra").setLectura(true);
                tabLicencia.getColumna("licen_numero_licencia").setLectura(true);
                tabLicencia.getColumna("licen_tiempo_vigencia").setLectura(true);
                tabLicencia.getColumna("licen_cantidad").setLectura(true);
                utilitario.addUpdate("tabLicencia");
            }
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabProgramas.guardar()) {
            if (tabLicencia.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTabProgramas() {
        return tabProgramas;
    }

    public void setTabProgramas(Tabla tabProgramas) {
        this.tabProgramas = tabProgramas;
    }

    public Tabla getTabLicencia() {
        return tabLicencia;
    }

    public void setTabLicencia(Tabla tabLicencia) {
        this.tabLicencia = tabLicencia;
    }
}
