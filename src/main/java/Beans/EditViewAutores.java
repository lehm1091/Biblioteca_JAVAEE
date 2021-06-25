package Beans;

import Service.Service;
import entity.Autores;
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

@Named("dtEditViewAutores")
@ViewScoped
public class EditViewAutores implements Serializable {
    
    private Autores selectedAutor;
    private Autores toDelete;
    private Autores DetailAutor;
    private Autores newAutor;
    private List<Autores> listaAutores;
    private ArrayList<Autores> filteredAutores;
    
    @Inject
    private Service service;
    
    @Inject
    private SessionView sessionview;
    
    @PostConstruct
    public void init() {
        
        setListaAutores(new ArrayList<Autores>(service.findAllAutores()));
        setSelectedAutor(new Autores());
        getSelectedAutor().setIdAutor(new BigDecimal(0));
        setNewAutor(new Autores());
        
    }
    
    public void onRowEdit(RowEditEvent event) {
        
    }
    
    public void onRowCancel(RowEditEvent event) {
    }
    
    public void onAddNew() {
        
        Autores nuevo = new Autores();
        nuevo.setNombre("Nombre Temporal");
        
        if (service.save(nuevo) == false) {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } else {
            listaAutores = service.findAllAutores();
        }
        
    }
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        
        Autores u = listaAutores.get(event.getRowIndex());
        
        if (newValue != null && !newValue.equals(oldValue)) {
            if (!service.update(u)) {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
                FacesContext.getCurrentInstance().addMessage(null, msj);
                
            }
        }
    }
    
    public void botonEliminar() {
        if (service.delete(toDelete)) {
            listaAutores.remove(toDelete);
            FacesMessage msj = new FacesMessage("item borrado");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } else {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }
    
    public void botonAgregarAutores() {
        System.out.println(newAutor.getNombre());
        if (service.save(newAutor)) {
            listaAutores = service.findAllAutores();
            FacesMessage msj = new FacesMessage("Se a agregado un item");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            newAutor = new Autores();
        } else {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No agrego el autor "+newAutor);
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }

    /**
     * @return the selectedAutor
     */
    public Autores getSelectedAutor() {
        return selectedAutor;
    }

    /**
     * @param selectedAutor the selectedAutor to set
     */
    public void setSelectedAutor(Autores selectedAutor) {
        this.selectedAutor = selectedAutor;
    }

    /**
     * @return the toDelete
     */
    public Autores getToDelete() {
        return toDelete;
    }

    /**
     * @param toDelete the toDelete to set
     */
    public void setToDelete(Autores toDelete) {
        this.toDelete = toDelete;
    }

    /**
     * @return the DetailAutor
     */
    public Autores getDetailAutor() {
        return DetailAutor;
    }

    /**
     * @param DetailAutor the DetailAutor to set
     */
    public void setDetailAutor(Autores DetailAutor) {
        this.DetailAutor = DetailAutor;
    }

    /**
     * @return the newAutor
     */
    public Autores getNewAutor() {
        return newAutor;
    }

    /**
     * @param newAutor the newAutor to set
     */
    public void setNewAutor(Autores newAutor) {
        this.newAutor = newAutor;
    }

    /**
     * @return the listaAutores
     */
    public List<Autores> getListaAutores() {
        return listaAutores;
    }

    /**
     * @param listaAutores the listaAutores to set
     */
    public void setListaAutores(List<Autores> listaAutores) {
        this.listaAutores = listaAutores;
    }

    /**
     * @return the filteredAutores
     */
    public ArrayList<Autores> getFilteredAutores() {
        return filteredAutores;
    }

    /**
     * @param filteredAutores the filteredAutores to set
     */
    public void setFilteredAutores(ArrayList<Autores> filteredAutores) {
        this.filteredAutores = filteredAutores;
    }
    
}
