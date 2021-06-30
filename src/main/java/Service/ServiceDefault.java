/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import dao.AutoresJpaController;
import dao.EjemplaresJpaController;
import dao.ObrasJpaController;
import dao.PrestamosJpaController;
import dao.UsuariosJpaController;
import entity.Autores;
import entity.Ejemplares;
import entity.Obras;
import entity.Prestamos;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;

import javax.inject.Inject;

import entity.Usuarios;

/**
 *
 * @author lehm
 */
@Stateless
@Default
public class ServiceDefault implements Service, Serializable {

    @Inject
    UsuariosJpaController usuariosController;
    @Inject
    AutoresJpaController autoresController;
    @Inject
    ObrasJpaController obrasController;
    @Inject
    EjemplaresJpaController ejemplaresController;
    @Inject
    PrestamosJpaController prestamosController;

    @Override
    public List<Usuarios> findAllUsuarios() {
        return usuariosController.findUsuariosEntities();
    }

    @Override
    public boolean save(Usuarios usuario) {
        boolean flag = true;
        try {
            usuariosController.create(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;

    }

    @Override
    public boolean delete(Usuarios usuario) {
        boolean flag = true;
        try {
            usuariosController.destroy(usuario.getIdUsuario());
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean update(Usuarios usuario) {
        boolean flag = true;
        try {
            usuariosController.edit(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Autores> findAllAutores() {
        return autoresController.findAutoresEntities();
    }

    @Override
    public boolean save(Autores o) {
        boolean flag = true;
        try {
            autoresController.create(o);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;

    }

    @Override
    public boolean delete(Autores o) {
        boolean flag = true;
        try {
            autoresController.destroy(o.getIdAutor());
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean update(Autores o) {
        boolean flag = true;
        try {
            autoresController.edit(o);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Obras> findAllObras() {
        return obrasController.findObrasEntities();
    }

    @Override
    public boolean save(Obras o) {
        boolean flag = true;
        try {
            Obras obra = new Obras();

            List<Autores> autores_ = o.getAutoresList();
            Obras obra_ = obrasController.create(o);
            //System.out.println("obra " + obra_.getAutoresList().size());
            //int id = obra.getIdObra().intValue();
            System.out.println(" integer " + obra_.getIdObra().intValue());

            Obras obra_1 = obrasController.findObras(obra_.getIdObra());
            System.out.println("obra_1 " + obra_1.toString());
            obra_1.setAutoresList(autores_);
            obrasController.create_(obra_1);

        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean delete(Obras o) {
        boolean flag = true;
        try {
            obrasController.destroy(o.getIdObra());
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean update(Obras o) {
        boolean flag = true;
        try {
            obrasController.edit(o);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Ejemplares> findAllEjemplares() {
        return ejemplaresController.findEjemplaresEntities();
    }

    @Override
    public boolean save(Ejemplares ejm) {
        boolean flag = true;
        try {
            ejemplaresController.create(ejm);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean delete(Ejemplares ejm) {
        boolean flag = true;
        try {
            ejemplaresController.destroy(ejm.getIdEjemplar());
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean update(Ejemplares ejm) {
        boolean flag = true;
        try {
            ejemplaresController.edit(ejm);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Prestamos> findAllPrestamos() {
        return prestamosController.findPrestamosEntities();
    }

    @Override
    public boolean save(Prestamos p) {
        boolean flag = true;
        try {
            prestamosController.create(p);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean delete(Prestamos p) {
        boolean flag = true;
        try {
            prestamosController.destroy(p.getIdPrestamo());
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean update(Prestamos p) {
        boolean flag = true;
        try {
            prestamosController.edit(p);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

}
