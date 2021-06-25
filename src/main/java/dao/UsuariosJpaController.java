/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Usuarios;
import entity.Prestamos;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author lab02
 */
@Stateless
public class UsuariosJpaController implements Serializable {

    @PersistenceContext(unitName = "bibliotecaPU")
    EntityManager em;

    public void create(Usuarios usuarios) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (usuarios.getPrestamosList() == null) {
            usuarios.setPrestamosList(new ArrayList<Prestamos>());
        }
        if (usuarios.getUsuariosList() == null) {
            usuarios.setUsuariosList(new ArrayList<Usuarios>());
        }
        
        try {
            
            
            Usuarios avaladoPor = usuarios.getAvaladoPor();
            if (avaladoPor != null) {
                avaladoPor = em.getReference(avaladoPor.getClass(), avaladoPor.getIdUsuario());
                usuarios.setAvaladoPor(avaladoPor);
            }
            List<Prestamos> attachedPrestamosList = new ArrayList<Prestamos>();
            for (Prestamos prestamosListPrestamosToAttach : usuarios.getPrestamosList()) {
                prestamosListPrestamosToAttach = em.getReference(prestamosListPrestamosToAttach.getClass(), prestamosListPrestamosToAttach.getIdPrestamo());
                attachedPrestamosList.add(prestamosListPrestamosToAttach);
            }
            usuarios.setPrestamosList(attachedPrestamosList);
            List<Usuarios> attachedUsuariosList = new ArrayList<Usuarios>();
            for (Usuarios usuariosListUsuariosToAttach : usuarios.getUsuariosList()) {
                usuariosListUsuariosToAttach = em.getReference(usuariosListUsuariosToAttach.getClass(), usuariosListUsuariosToAttach.getIdUsuario());
                attachedUsuariosList.add(usuariosListUsuariosToAttach);
            }
            usuarios.setUsuariosList(attachedUsuariosList);
            em.persist(usuarios);
            if (avaladoPor != null) {
                avaladoPor.getUsuariosList().add(usuarios);
                avaladoPor = em.merge(avaladoPor);
            }
            for (Prestamos prestamosListPrestamos : usuarios.getPrestamosList()) {
                Usuarios oldIdUsuarioOfPrestamosListPrestamos = prestamosListPrestamos.getIdUsuario();
                prestamosListPrestamos.setIdUsuario(usuarios);
                prestamosListPrestamos = em.merge(prestamosListPrestamos);
                if (oldIdUsuarioOfPrestamosListPrestamos != null) {
                    oldIdUsuarioOfPrestamosListPrestamos.getPrestamosList().remove(prestamosListPrestamos);
                    oldIdUsuarioOfPrestamosListPrestamos = em.merge(oldIdUsuarioOfPrestamosListPrestamos);
                }
            }
            for (Usuarios usuariosListUsuarios : usuarios.getUsuariosList()) {
                Usuarios oldAvaladoPorOfUsuariosListUsuarios = usuariosListUsuarios.getAvaladoPor();
                usuariosListUsuarios.setAvaladoPor(usuarios);
                usuariosListUsuarios = em.merge(usuariosListUsuarios);
                if (oldAvaladoPorOfUsuariosListUsuarios != null) {
                    oldAvaladoPorOfUsuariosListUsuarios.getUsuariosList().remove(usuariosListUsuarios);
                    oldAvaladoPorOfUsuariosListUsuarios = em.merge(oldAvaladoPorOfUsuariosListUsuarios);
                }
            }
           
        } catch (Exception ex) {
            try {
                
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsuarios(usuarios.getIdUsuario()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
               
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, RollbackFailureException, Exception {
        
        try {
            
            
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIdUsuario());
            Usuarios avaladoPorOld = persistentUsuarios.getAvaladoPor();
            Usuarios avaladoPorNew = usuarios.getAvaladoPor();
            List<Prestamos> prestamosListOld = persistentUsuarios.getPrestamosList();
            List<Prestamos> prestamosListNew = usuarios.getPrestamosList();
            List<Usuarios> usuariosListOld = persistentUsuarios.getUsuariosList();
            List<Usuarios> usuariosListNew = usuarios.getUsuariosList();
            if (avaladoPorNew != null) {
                avaladoPorNew = em.getReference(avaladoPorNew.getClass(), avaladoPorNew.getIdUsuario());
                usuarios.setAvaladoPor(avaladoPorNew);
            }
            List<Prestamos> attachedPrestamosListNew = new ArrayList<Prestamos>();
            for (Prestamos prestamosListNewPrestamosToAttach : prestamosListNew) {
                prestamosListNewPrestamosToAttach = em.getReference(prestamosListNewPrestamosToAttach.getClass(), prestamosListNewPrestamosToAttach.getIdPrestamo());
                attachedPrestamosListNew.add(prestamosListNewPrestamosToAttach);
            }
            prestamosListNew = attachedPrestamosListNew;
            usuarios.setPrestamosList(prestamosListNew);
            List<Usuarios> attachedUsuariosListNew = new ArrayList<Usuarios>();
            for (Usuarios usuariosListNewUsuariosToAttach : usuariosListNew) {
                usuariosListNewUsuariosToAttach = em.getReference(usuariosListNewUsuariosToAttach.getClass(), usuariosListNewUsuariosToAttach.getIdUsuario());
                attachedUsuariosListNew.add(usuariosListNewUsuariosToAttach);
            }
            usuariosListNew = attachedUsuariosListNew;
            usuarios.setUsuariosList(usuariosListNew);
            usuarios = em.merge(usuarios);
            if (avaladoPorOld != null && !avaladoPorOld.equals(avaladoPorNew)) {
                avaladoPorOld.getUsuariosList().remove(usuarios);
                avaladoPorOld = em.merge(avaladoPorOld);
            }
            if (avaladoPorNew != null && !avaladoPorNew.equals(avaladoPorOld)) {
                avaladoPorNew.getUsuariosList().add(usuarios);
                avaladoPorNew = em.merge(avaladoPorNew);
            }
            for (Prestamos prestamosListOldPrestamos : prestamosListOld) {
                if (!prestamosListNew.contains(prestamosListOldPrestamos)) {
                    prestamosListOldPrestamos.setIdUsuario(null);
                    prestamosListOldPrestamos = em.merge(prestamosListOldPrestamos);
                }
            }
            for (Prestamos prestamosListNewPrestamos : prestamosListNew) {
                if (!prestamosListOld.contains(prestamosListNewPrestamos)) {
                    Usuarios oldIdUsuarioOfPrestamosListNewPrestamos = prestamosListNewPrestamos.getIdUsuario();
                    prestamosListNewPrestamos.setIdUsuario(usuarios);
                    prestamosListNewPrestamos = em.merge(prestamosListNewPrestamos);
                    if (oldIdUsuarioOfPrestamosListNewPrestamos != null && !oldIdUsuarioOfPrestamosListNewPrestamos.equals(usuarios)) {
                        oldIdUsuarioOfPrestamosListNewPrestamos.getPrestamosList().remove(prestamosListNewPrestamos);
                        oldIdUsuarioOfPrestamosListNewPrestamos = em.merge(oldIdUsuarioOfPrestamosListNewPrestamos);
                    }
                }
            }
            for (Usuarios usuariosListOldUsuarios : usuariosListOld) {
                if (!usuariosListNew.contains(usuariosListOldUsuarios)) {
                    usuariosListOldUsuarios.setAvaladoPor(null);
                    usuariosListOldUsuarios = em.merge(usuariosListOldUsuarios);
                }
            }
            for (Usuarios usuariosListNewUsuarios : usuariosListNew) {
                if (!usuariosListOld.contains(usuariosListNewUsuarios)) {
                    Usuarios oldAvaladoPorOfUsuariosListNewUsuarios = usuariosListNewUsuarios.getAvaladoPor();
                    usuariosListNewUsuarios.setAvaladoPor(usuarios);
                    usuariosListNewUsuarios = em.merge(usuariosListNewUsuarios);
                    if (oldAvaladoPorOfUsuariosListNewUsuarios != null && !oldAvaladoPorOfUsuariosListNewUsuarios.equals(usuarios)) {
                        oldAvaladoPorOfUsuariosListNewUsuarios.getUsuariosList().remove(usuariosListNewUsuarios);
                        oldAvaladoPorOfUsuariosListNewUsuarios = em.merge(oldAvaladoPorOfUsuariosListNewUsuarios);
                    }
                }
            }
           
        } catch (Exception ex) {
            try {
                
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = usuarios.getIdUsuario();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
               
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException, RollbackFailureException, Exception {
        
        try {
            
            
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            Usuarios avaladoPor = usuarios.getAvaladoPor();
            if (avaladoPor != null) {
                avaladoPor.getUsuariosList().remove(usuarios);
                avaladoPor = em.merge(avaladoPor);
            }
            List<Prestamos> prestamosList = usuarios.getPrestamosList();
            for (Prestamos prestamosListPrestamos : prestamosList) {
                prestamosListPrestamos.setIdUsuario(null);
                prestamosListPrestamos = em.merge(prestamosListPrestamos);
            }
            List<Usuarios> usuariosList = usuarios.getUsuariosList();
            for (Usuarios usuariosListUsuarios : usuariosList) {
                usuariosListUsuarios.setAvaladoPor(null);
                usuariosListUsuarios = em.merge(usuariosListUsuarios);
            }
            em.remove(usuarios);
           
        } catch (Exception ex) {
            try {
                
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
               
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return new ArrayList<Usuarios>(q.getResultList());
        } finally {
           
        }
    }

    public Usuarios findUsuarios(BigDecimal id) {
        
        try {
            return em.find(Usuarios.class, id);
        } finally {
           
        }
    }

    public int getUsuariosCount() {
        
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
           
        }
    }
    
}
