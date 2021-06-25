/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;


import entity.Usuarios;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author lehm
 */
@Named
@SessionScoped
public class SessionView implements Serializable {

    /**
     * @return the usuarioSelected
     */
    public Usuarios getUsuarioSelected() {
        return usuarioSelected;
    }

    /**
     * @param usuarioSelected the usuarioSelected to set
     */
    public void setUsuarioSelected(Usuarios usuarioSelected) {
        this.usuarioSelected = usuarioSelected;
    }

    @javax.enterprise.inject.New
    EditViewUsuarios editView;

    private Usuarios usuarioSelected;

}
