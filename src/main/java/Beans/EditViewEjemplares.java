package Beans;

import Service.Service;
import entity.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;

import java.util.ArrayList;

@Named("dtEditViewEjemplares")
@ViewScoped
public class EditViewEjemplares implements Serializable {

    private Ejemplares selectedEjemplar;
    private Ejemplares toDeleteEjemplar;
    private Ejemplares detailEjemplar;
    private Ejemplares newEjemplar;
    private List<Ejemplares> listaEjemplares;
    private ArrayList<Ejemplares> filteredEjemplares;

    @Inject
    private Service service;

    @Inject
    private SessionView sessionview;

    @Inject
    private EditViewObras obrasBean;

    @PostConstruct
    public void init() {

        setListaEjemplares(new ArrayList<Ejemplares>(service.findAllEjemplares()));
        setSelectedEjemplar(new Ejemplares());
        getSelectedEjemplar().setIdEjemplar(new BigDecimal(0));
        setNewEjemplar(new Ejemplares());

    }

    public void onAddNew() {

        Ejemplares nuevo = new Ejemplares();
        nuevo.setEstadoConservacion("Estado Temporal");

        if (service.save(nuevo) == false) {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } else {
            listaEjemplares = service.findAllEjemplares();
        }

    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        Ejemplares u = listaEjemplares.get(event.getRowIndex());

        if (newValue != null && !newValue.equals(oldValue)) {
            if (!service.update(u)) {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
                FacesContext.getCurrentInstance().addMessage(null, msj);

            }
        }
    }

    public void botonEliminar() {
        if (service.delete(toDeleteEjemplar)) {
            listaEjemplares.remove(toDeleteEjemplar);
            FacesMessage msj = new FacesMessage("item borrado");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } else {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }

    public void botonAgregarEjemplares() {

        
      
       
        newEjemplar.setObra(getObrasBean().getNewObra());

        if (service.save(newEjemplar)) {
            listaEjemplares = service.findAllEjemplares();
            FacesMessage msj = new FacesMessage("Se a agregado un item");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            newEjemplar = new Ejemplares();
            
        } else {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No agrego el autor " + newEjemplar);
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }

    /**
     * @return the selectedEjemplares
     */
    public Ejemplares getSelectedEjemplar() {
        return selectedEjemplar;
    }

    /**
     * @param selectedEjemplar the selectedEjemplares to set
     */
    public void setSelectedEjemplar(Ejemplares selectedEjemplar) {
        this.selectedEjemplar = selectedEjemplar;
    }

    /**
     * @return the toDelete
     */
    public Ejemplares getToDelete() {
        return toDeleteEjemplar;
    }

    /**
     * @param toDelete the toDelete to set
     */
    public void setToDelete(Ejemplares toDelete) {
        this.toDeleteEjemplar = toDelete;
    }

    /**
     * @return the DetailEjemplares
     */
    public Ejemplares getDetailEjemplares() {
        return detailEjemplar;
    }

    /**
     * @param DetailEjemplares the DetailEjemplares to set
     */
    public void setDetailEjemplares(Ejemplares DetailEjemplares) {
        this.detailEjemplar = DetailEjemplares;
    }

    /**
     * @return the newEjemplares
     */
    public Ejemplares getNewEjemplar() {
        return newEjemplar;
    }

    /**
     * @param newEjemplar the newEjemplares to set
     */
    public void setNewEjemplar(Ejemplares newEjemplar) {
        this.newEjemplar = newEjemplar;
    }

    /**
     * @return the listaEjemplares
     */
    public List<Ejemplares> getListaEjemplares() {
        return listaEjemplares;
    }

    /**
     * @param listaEjemplares the listaEjemplares to set
     */
    public void setListaEjemplares(List<Ejemplares> listaEjemplares) {
        this.listaEjemplares = listaEjemplares;
    }

    /**
     * @return the filteredEjemplares
     */
    public ArrayList<Ejemplares> getFilteredEjemplares() {
        return filteredEjemplares;
    }

    /**
     * @param filteredEjemplares the filteredEjemplares to set
     */
    public void setFilteredEjemplares(ArrayList<Ejemplares> filteredEjemplares) {
        this.filteredEjemplares = filteredEjemplares;
    }

    /**
     * @return the obrasBean
     */
    public EditViewObras getObrasBean() {
        return obrasBean;
    }

    /**
     * @param obrasBean the obrasBean to set
     */
    public void setObrasBean(EditViewObras obrasBean) {
        this.obrasBean = obrasBean;
    }

}
