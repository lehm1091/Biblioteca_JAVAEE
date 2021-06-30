/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author lab02
 */
@Entity
@Table(catalog = "", schema = "LUIS")
@NamedQueries({
    @NamedQuery(name = "Ejemplares.findAll", query = "SELECT e FROM Ejemplares e"),
    @NamedQuery(name = "Ejemplares.findByIdEjemplar", query = "SELECT e FROM Ejemplares e WHERE e.idEjemplar = :idEjemplar"),
    @NamedQuery(name = "Ejemplares.findByNumeroEjemplar", query = "SELECT e FROM Ejemplares e WHERE e.numeroEjemplar = :numeroEjemplar"),
    @NamedQuery(name = "Ejemplares.findByEstadoConservacion", query = "SELECT e FROM Ejemplares e WHERE e.estadoConservacion = :estadoConservacion")})
public class Ejemplares implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Column(name = "ID_EJEMPLAR")
    @GeneratedValue(generator = "sec_ejemplar", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sec_ejemplar", sequenceName = "ejemplares_seq", allocationSize = 1)
    private BigDecimal idEjemplar;
    @Size(max = 100)
    @Column(name = "NUMERO_EJEMPLAR")
    private String numeroEjemplar;
    @Size(max = 100)
    @Column(name = "ESTADO_CONSERVACION")
    private String estadoConservacion;
    @OneToMany(mappedBy = "idEjemplar", fetch = FetchType.LAZY)
    private List<Prestamos> prestamosList;

    @JoinColumn(name = "ID_OBRA", referencedColumnName = "ID_OBRA")
    @ManyToOne(fetch = FetchType.EAGER)
    private Obras obra;

    public Ejemplares() {
    }

    public Ejemplares(BigDecimal idEjemplar) {
        this.idEjemplar = idEjemplar;
    }

    public BigDecimal getIdEjemplar() {
        return idEjemplar;
    }

    public void setIdEjemplar(BigDecimal idEjemplar) {
        this.idEjemplar = idEjemplar;
    }

    public String getNumeroEjemplar() {
        return numeroEjemplar;
    }

    public void setNumeroEjemplar(String numeroEjemplar) {
        this.numeroEjemplar = numeroEjemplar;
    }

    public String getEstadoConservacion() {
        return estadoConservacion;
    }

    public void setEstadoConservacion(String estadoConservacion) {
        this.estadoConservacion = estadoConservacion;
    }

    public List<Prestamos> getPrestamosList() {
        return prestamosList;
    }

    public void setPrestamosList(List<Prestamos> prestamosList) {
        this.prestamosList = prestamosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEjemplar != null ? idEjemplar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ejemplares)) {
            return false;
        }
        Ejemplares other = (Ejemplares) object;
        if ((this.idEjemplar == null && other.idEjemplar != null) || (this.idEjemplar != null && !this.idEjemplar.equals(other.idEjemplar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ejemplares{" + "idEjemplar=" + idEjemplar + ", numeroEjemplar=" + numeroEjemplar + ", estadoConservacion=" + estadoConservacion + '}';
    }

   

    /**
     * @return the obra
     */
    public Obras getObra() {
        return obra;
    }

    /**
     * @param obra the obra to set
     */
    public void setObra(Obras obra) {
        this.obra = obra;
    }

}
