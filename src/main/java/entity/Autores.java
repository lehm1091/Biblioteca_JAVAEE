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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
    @NamedQuery(name = "Autores.findAll", query = "SELECT a FROM Autores a"),
    @NamedQuery(name = "Autores.findByIdAutor", query = "SELECT a FROM Autores a WHERE a.idAutor = :idAutor"),
    @NamedQuery(name = "Autores.findByNombre", query = "SELECT a FROM Autores a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Autores.findByNacionalidad", query = "SELECT a FROM Autores a WHERE a.nacionalidad = :nacionalidad"),
    @NamedQuery(name = "Autores.findBySexo", query = "SELECT a FROM Autores a WHERE a.sexo = :sexo")})
public class Autores implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Column(name = "ID_AUTOR")
    @GeneratedValue(generator = "sec_autores", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sec_autores", sequenceName = "autores_seq", allocationSize = 1)
    private BigDecimal idAutor;
    @Size(max = 100)
    private String nombre;
    @Size(max = 100)
    private String nacionalidad;
    @Size(max = 100)
    private String sexo;
    @JoinTable(name = "AUTORES_OBRAS", joinColumns = {
        @JoinColumn(name = "ID_AUTOR", referencedColumnName = "ID_AUTOR")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_OBRA", referencedColumnName = "ID_OBRA")})
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Obras> obrasList;

    public Autores() {
    }

    public Autores(BigDecimal idAutor) {
        this.idAutor = idAutor;
    }

    public BigDecimal getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(BigDecimal idAutor) {
        this.idAutor = idAutor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public List<Obras> getObrasList() {
        return obrasList;
    }

    public void setObrasList(List<Obras> obrasList) {
        this.obrasList = obrasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAutor != null ? idAutor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Autores)) {
            return false;
        }
        Autores other = (Autores) object;
        if ((this.idAutor == null && other.idAutor != null) || (this.idAutor != null && !this.idAutor.equals(other.idAutor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Autores{idAutor=").append(idAutor);
        sb.append(", nombre=").append(nombre);
        sb.append(", nacionalidad=").append(nacionalidad);
        sb.append(", sexo=").append(sexo);
        sb.append(", obrasList=").append(obrasList);
        sb.append('}');
        return sb.toString();
    }

}
