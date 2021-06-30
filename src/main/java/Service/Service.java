/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import entity.*;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author DSC-L
 */
@Local
public interface Service {

    public List<Usuarios> findAllUsuarios();

    public boolean save(Usuarios usuario);

    public boolean delete(Usuarios usuario);

    public boolean update(Usuarios usuario);

    public List<Autores> findAllAutores();

    public boolean save(Autores usuario);

    public boolean delete(Autores usuario);

    public boolean update(Autores usuario);

    public List<Obras> findAllObras();

    public boolean save(Obras o);

    public boolean delete(Obras o);

    public boolean update(Obras o);

    public List<Ejemplares> findAllEjemplares();

    public boolean save(Ejemplares e);

    public boolean delete(Ejemplares e);

    public boolean update(Ejemplares e);

    public List<Prestamos> findAllPrestamos();

    public boolean save(Prestamos p);

    public boolean delete(Prestamos p);

    public boolean update(Prestamos p);

}
