package Beans;

import Service.Service;
import entity.Obras;
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

@Named("dtEditViewObras")
@ViewScoped
public class EditViewObras implements Serializable {
    
    private Obras selectedObra;
    private Obras toDelete;
    private Obras DetailObras;
    private Obras newObra;
    private List<Obras> listaObras;
    private ArrayList<Obras> filteredObras;
    
    @Inject
    private Service service;
    
    @Inject
    private SessionView sessionview;
    
    @Inject
    private EditViewAutores autoresBean;
    
    @PostConstruct
    public void init() {
        
        setListaObras(new ArrayList<Obras>(service.findAllObras()));
        setSelectedObra(new Obras());
        getSelectedObra().setIdObra(new BigDecimal(0));
        setNewObra(new Obras());
        
    }
    
    public void onAddNew() {
        
        Obras nuevo = new Obras();
        nuevo.setTitulo("Nombre Temporal");
        
        if (service.save(nuevo) == false) {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } else {
            listaObras = service.findAllObras();
        }
        
    }
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        
        Obras u = listaObras.get(event.getRowIndex());
        
        if (newValue != null && !newValue.equals(oldValue)) {
            if (!service.update(u)) {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
                FacesContext.getCurrentInstance().addMessage(null, msj);
                
            }
        }
    }
    
    public void botonEliminar() {
        if (service.delete(toDelete)) {
            listaObras.remove(toDelete);
            FacesMessage msj = new FacesMessage("item borrado");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } else {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }
    
    public void botonAgregarObras() {
        newObra.setAutoresList(autoresBean.getFilteredAutores());
        if (service.save(newObra)) {
            listaObras = service.findAllObras();
            FacesMessage msj = new FacesMessage("Se a agregado un item");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            newObra = new Obras();
            
        } else {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No agrego el autor " + newObra);
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }

    /**
     * @return the selectedObras
     */
    public Obras getSelectedObra() {
        return selectedObra;
    }

    /**
     * @param selectedObra the selectedObras to set
     */
    public void setSelectedObra(Obras selectedObra) {
        this.selectedObra = selectedObra;
    }

    /**
     * @return the toDelete
     */
    public Obras getToDelete() {
        return toDelete;
    }

    /**
     * @param toDelete the toDelete to set
     */
    public void setToDelete(Obras toDelete) {
        this.toDelete = toDelete;
    }

    /**
     * @return the DetailObras
     */
    public Obras getDetailObras() {
        return DetailObras;
    }

    /**
     * @param DetailObras the DetailObras to set
     */
    public void setDetailObras(Obras DetailObras) {
        this.DetailObras = DetailObras;
    }

    /**
     * @return the newObras
     */
    public Obras getNewObra() {
        return newObra;
    }

    /**
     * @param newObra the newObras to set
     */
    public void setNewObra(Obras newObra) {
        this.newObra = newObra;
    }

    /**
     * @return the listaObras
     */
    public List<Obras> getListaObras() {
        return listaObras;
    }

    /**
     * @param listaObras the listaObras to set
     */
    public void setListaObras(List<Obras> listaObras) {
        this.listaObras = listaObras;
    }

    /**
     * @return the filteredObras
     */
    public ArrayList<Obras> getFilteredObras() {
        return filteredObras;
    }

    /**
     * @param filteredObras the filteredObras to set
     */
    public void setFilteredObras(ArrayList<Obras> filteredObras) {
        this.filteredObras = filteredObras;
    }

    /**
     * @return the autoresBean
     */
    public EditViewAutores getAutoresBean() {
        return autoresBean;
    }

    /**
     * @param autoresBean the autoresBean to set
     */
    public void setAutoresBean(EditViewAutores autoresBean) {
        this.autoresBean = autoresBean;
    }
    
}
