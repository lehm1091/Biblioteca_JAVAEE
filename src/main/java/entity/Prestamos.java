/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author lab02
 */
@Entity
@Table(catalog = "", schema = "LUIS")
@NamedQueries({
    @NamedQuery(name = "Prestamos.findAll", query = "SELECT p FROM Prestamos p"),
    @NamedQuery(name = "Prestamos.findByIdPrestamo", query = "SELECT p FROM Prestamos p WHERE p.idPrestamo = :idPrestamo"),
    @NamedQuery(name = "Prestamos.findByFechaPrestamo", query = "SELECT p FROM Prestamos p WHERE p.fechaPrestamo = :fechaPrestamo"),
    @NamedQuery(name = "Prestamos.findByFechaDevolucion", query = "SELECT p FROM Prestamos p WHERE p.fechaDevolucion = :fechaDevolucion")})
public class Prestamos implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Column(name = "ID_PRESTAMO")
    @GeneratedValue(generator = "sec_prestamo", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sec_prestamo", sequenceName = "prestamos_seq", allocationSize = 1)
    private BigDecimal idPrestamo;
    @Column(name = "FECHA_PRESTAMO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPrestamo;
    @Column(name = "FECHA_DEVOLUCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDevolucion;
    @JoinColumn(name = "ID_EJEMPLAR", referencedColumnName = "ID_EJEMPLAR")
    @ManyToOne(fetch = FetchType.EAGER)
    private Ejemplares idEjemplar;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuarios idUsuario;

    public Prestamos() {
    }

    public Prestamos(BigDecimal idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public BigDecimal getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(BigDecimal idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Ejemplares getIdEjemplar() {
        return idEjemplar;
    }

    public void setIdEjemplar(Ejemplares idEjemplar) {
        this.idEjemplar = idEjemplar;
    }

    public Usuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuarios idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPrestamo != null ? idPrestamo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prestamos)) {
            return false;
        }
        Prestamos other = (Prestamos) object;
        if ((this.idPrestamo == null && other.idPrestamo != null) || (this.idPrestamo != null && !this.idPrestamo.equals(other.idPrestamo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Prestamos[ idPrestamo=" + idPrestamo + " ]";
    }

}
