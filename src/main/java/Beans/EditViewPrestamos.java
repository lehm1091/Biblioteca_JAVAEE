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
import java.util.Date;

@Named("dtEditViewPrestamos")
@ViewScoped
public class EditViewPrestamos implements Serializable {

    private Prestamos selectedPrestamo;
    private Prestamos toDeletePrestamo;
    private Prestamos detailPrestamo;
    private Prestamos newPrestamo;
    private List<Prestamos> listaPrestamos;
    private ArrayList<Prestamos> filteredPrestamos;

    @Inject
    private Service service;

    @Inject
    private SessionView sessionview;

    @Inject
    private EditViewEjemplares ejemplaresBean;

    @Inject
    private EditViewUsuarios usuariosBean;

    @PostConstruct
    public void init() {

        setListaPrestamos(new ArrayList<Prestamos>(service.findAllPrestamos()));
        setSelectedPrestamo(new Prestamos());
        getSelectedPrestamo().setIdPrestamo(new BigDecimal(0));
        setNewPrestamo(new Prestamos());

    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        Prestamos u = listaPrestamos.get(event.getRowIndex());

        if (newValue != null && !newValue.equals(oldValue)) {
            if (!service.update(u)) {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
                FacesContext.getCurrentInstance().addMessage(null, msj);

            }
        }
    }

    public void botonEliminar() {
        if (service.delete(toDeletePrestamo)) {
            listaPrestamos.remove(toDeletePrestamo);
            FacesMessage msj = new FacesMessage("item borrado");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } else {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }

    public void marcarPrestamoComoDevuelto() {
        if (selectedPrestamo.getFechaDevolucion() != null) {
            FacesMessage msj = new FacesMessage("item ya ha sido devuelto");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } else {
            selectedPrestamo.setFechaDevolucion(new Date());

            if (service.update(selectedPrestamo)) {
                listaPrestamos = service.findAllPrestamos();
                FacesMessage msj = new FacesMessage("item marcado como devuelto");
                FacesContext.getCurrentInstance().addMessage(null, msj);
            } else {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contacte al administrador");
                FacesContext.getCurrentInstance().addMessage(null, msj);
            }
        }
    }

    public void botonAgregarPrestamos() {

        newPrestamo.setIdUsuario(getUsuariosBean().getSelectedUsuario());
        newPrestamo.setIdEjemplar(getEjemplaresBean().getSelectedEjemplar());
        if (service.save(newPrestamo)) {
            listaPrestamos = service.findAllPrestamos();
            FacesMessage msj = new FacesMessage("Se a agregado un item");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            newPrestamo = new Prestamos();
        } else {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo agregar el item ");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }

    /**
     * @return the selectedPrestamos
     */
    public Prestamos getSelectedPrestamo() {
        return selectedPrestamo;
    }

    /**
     * @param selectedPrestamo the selectedPrestamos to set
     */
    public void setSelectedPrestamo(Prestamos selectedPrestamo) {
        this.selectedPrestamo = selectedPrestamo;
    }

    /**
     * @return the toDelete
     */
    public Prestamos getToDelete() {
        return toDeletePrestamo;
    }

    /**
     * @param toDelete the toDelete to set
     */
    public void setToDelete(Prestamos toDelete) {
        this.toDeletePrestamo = toDelete;
    }

    /**
     * @return the DetailPrestamos
     */
    public Prestamos getDetailPrestamos() {
        return detailPrestamo;
    }

    /**
     * @param DetailPrestamos the DetailPrestamos to set
     */
    public void setDetailPrestamos(Prestamos DetailPrestamos) {
        this.detailPrestamo = DetailPrestamos;
    }

    /**
     * @return the newPrestamos
     */
    public Prestamos getNewPrestamo() {
        return newPrestamo;
    }

    /**
     * @param newPrestamo the newPrestamos to set
     */
    public void setNewPrestamo(Prestamos newPrestamo) {
        this.newPrestamo = newPrestamo;
    }

    /**
     * @return the listaPrestamos
     */
    public List<Prestamos> getListaPrestamos() {
        return listaPrestamos;
    }

    /**
     * @param listaPrestamos the listaPrestamos to set
     */
    public void setListaPrestamos(List<Prestamos> listaPrestamos) {
        this.listaPrestamos = listaPrestamos;
    }

    /**
     * @return the filteredPrestamos
     */
    public ArrayList<Prestamos> getFilteredPrestamos() {
        return filteredPrestamos;
    }

    /**
     * @param filteredPrestamos the filteredPrestamos to set
     */
    public void setFilteredPrestamos(ArrayList<Prestamos> filteredPrestamos) {
        this.filteredPrestamos = filteredPrestamos;
    }

    /**
     * @return the ejemplaresBean
     */
    public EditViewEjemplares getEjemplaresBean() {
        return ejemplaresBean;
    }

    /**
     * @param ejemplaresBean the ejemplaresBean to set
     */
    public void setEjemplaresBean(EditViewEjemplares ejemplaresBean) {
        this.ejemplaresBean = ejemplaresBean;
    }

    /**
     * @return the usuariosBean
     */
    public EditViewUsuarios getUsuariosBean() {
        return usuariosBean;
    }

    /**
     * @param usuariosBean the usuariosBean to set
     */
    public void setUsuariosBean(EditViewUsuarios usuariosBean) {
        this.usuariosBean = usuariosBean;
    }

}
