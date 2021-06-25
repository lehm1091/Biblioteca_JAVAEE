package Beans;

import Service.Service;
import entity.Usuarios;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;

@Named("dtEditViewUsuarios")
@ViewScoped
public class EditViewUsuarios implements Serializable {
    
    
    
    private Usuarios selectedUsuario;
    private Usuarios toDelete;
    private Usuarios DetailUsuario;
    private Usuarios newUsuario;
    private List<Usuarios> listaUsuarios;
    private ArrayList<Usuarios> filteredUsuarios;

    @Inject
    private Service service;

    @Inject
    private SessionView sessionview;

    @PostConstruct
    public void init() {

        setListaUsuarios(new ArrayList<Usuarios>(service.findAllUsuarios()));
        setSelectedUsuario(new Usuarios());
        getSelectedUsuario().setIdUsuario(new BigDecimal(0));
        setNewUsuario(new Usuarios());

    }

    public void onRowEdit(RowEditEvent event) {

    }

    public void onRowCancel(RowEditEvent event) {
    }

    public void onAddNew() {

        Usuarios nuevo = new Usuarios();
        nuevo.setNombre("Nombre Temporal");

        if (service.save(nuevo) == false) {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } else {
            listaUsuarios = service.findAllUsuarios();
        }

    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        Usuarios u = listaUsuarios.get(event.getRowIndex());

        if (newValue != null && !newValue.equals(oldValue)) {
            if (!service.update(u)) {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
                FacesContext.getCurrentInstance().addMessage(null, msj);

            }
        }
    }

    public void botonEliminar() {
        if (service.delete(toDelete)) {
            listaUsuarios.remove(toDelete);
            FacesMessage msj = new FacesMessage("item borrado");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } else {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }

    public void botonAgregarUsuarios() {
        if (service.save(newUsuario)) {
            listaUsuarios = service.findAllUsuarios();
            FacesMessage msj = new FacesMessage("Se a agregado el usuario");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            newUsuario = new Usuarios();
            listaUsuarios = service.findAllUsuarios();
        } else {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No agrego ningun item");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }

    public String botonGuardarUsuarios() {
        if (service.update(sessionview.getUsuarioSelected())) {
            FacesMessage msj = new FacesMessage("Se han guardado los cambios");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            selectedUsuario = new Usuarios();
            sessionview.setUsuarioSelected(selectedUsuario);
        } else {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se guardo");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }

        return "usuarios";
    }

    /**
     * @return the selectedUsuario
     */
    public Usuarios getSelectedUsuario() {
        return selectedUsuario;
    }

    /**
     * @param selectedUsuario the selectedUsuario to set
     */
    public void setSelectedUsuario(Usuarios selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }

    /**
     * @return the toDelete
     */
    public Usuarios getToDelete() {
        return toDelete;
    }

    /**
     * @param toDelete the toDelete to set
     */
    public void setToDelete(Usuarios toDelete) {
        this.toDelete = toDelete;
    }

    /**
     * @return the DetailUsuario
     */
    public Usuarios getDetailUsuario() {
        return DetailUsuario;
    }

    /**
     * @param DetailUsuario the DetailUsuario to set
     */
    public void setDetailUsuario(Usuarios DetailUsuario) {
        this.DetailUsuario = DetailUsuario;
    }

    /**
     * @return the newUsuario
     */
    public Usuarios getNewUsuario() {
        return newUsuario;
    }

    /**
     * @param newUsuario the newUsuario to set
     */
    public void setNewUsuario(Usuarios newUsuario) {
        this.newUsuario = newUsuario;
    }

    /**
     * @return the listaUsuarios
     */
    public List<Usuarios> getListaUsuarios() {
        return listaUsuarios;
    }

    /**
     * @param listaUsuarios the listaUsuarios to set
     */
    public void setListaUsuarios(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    /**
     * @return the filteredUsuarios
     */
    public ArrayList<Usuarios> getFilteredUsuarios() {
        return filteredUsuarios;
    }

    /**
     * @param filteredUsuarios the filteredUsuarios to set
     */
    public void setFilteredUsuarios(ArrayList<Usuarios> filteredUsuarios) {
        this.filteredUsuarios = filteredUsuarios;
    }

}
