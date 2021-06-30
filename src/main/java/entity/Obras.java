/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
    @NamedQuery(name = "Obras.findAll", query = "SELECT o FROM Obras o"),
    @NamedQuery(name = "Obras.findByIdObra", query = "SELECT o FROM Obras o WHERE o.idObra = :idObra"),
    @NamedQuery(name = "Obras.findByTitulo", query = "SELECT o FROM Obras o WHERE o.titulo = :titulo"),
    @NamedQuery(name = "Obras.findByNacionalidad", query = "SELECT o FROM Obras o WHERE o.nacionalidad = :nacionalidad"),
    @NamedQuery(name = "Obras.findByEditora", query = "SELECT o FROM Obras o WHERE o.editora = :editora"),
    @NamedQuery(name = "Obras.findByFechaPublicacion", query = "SELECT o FROM Obras o WHERE o.fechaPublicacion = :fechaPublicacion")})
public class Obras implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Column(name = "ID_OBRA")
    @GeneratedValue(generator = "sec_obra", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sec_obra", sequenceName = "obras_seq", allocationSize = 1)
    private BigDecimal idObra;
    @Size(max = 100)
    private String titulo;
    @Size(max = 100)
    private String nacionalidad;
    @Size(max = 100)
    private String editora;
    @Column(name = "FECHA_PUBLICACION")
    private Date fechaPublicacion;
    @ManyToMany(mappedBy = "obrasList", fetch = FetchType.LAZY)
    private List<Autores> autoresList;
    
    @OneToMany(mappedBy = "obra", fetch = FetchType.LAZY)
    private List<Ejemplares> ejemplaresList;

    public Obras() {
        fechaPublicacion=new Date();
    }

    public Obras(BigDecimal idObra) {
        this.idObra = idObra;
    }

    public BigDecimal getIdObra() {
        return idObra;
    }

    public void setIdObra(BigDecimal idObra) {
        this.idObra = idObra;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public List<Autores> getAutoresList() {
        return autoresList;
    }

    public void setAutoresList(List<Autores> autoresList) {
        this.autoresList = autoresList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idObra != null ? idObra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Obras)) {
            return false;
        }
        Obras other = (Obras) object;
        if ((this.idObra == null && other.idObra != null) || (this.idObra != null && !this.idObra.equals(other.idObra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Obras{idObra=").append(idObra);
        sb.append(", titulo=").append(titulo);
        sb.append(", nacionalidad=").append(nacionalidad);
        sb.append(", editora=").append(editora);
        sb.append(", fechaPublicacion=").append(fechaPublicacion);
        sb.append('}');
        return sb.toString();
    }

   

    /**
     * @return the ejemplaresList
     */
    public List<Ejemplares> getEjemplaresList() {
        return ejemplaresList;
    }

    /**
     * @param ejemplaresList the ejemplaresList to set
     */
    public void setEjemplaresList(List<Ejemplares> ejemplaresList) {
        this.ejemplaresList = ejemplaresList;
    }

}
