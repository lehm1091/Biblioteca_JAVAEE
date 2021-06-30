/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import entity.Ejemplares;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Obras;
import entity.Prestamos;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import javax.persistence.PersistenceContext;

/**
 *
 * @author Luis
 */
@Stateless
public class EjemplaresJpaController implements Serializable {

    @PersistenceContext(unitName = "bibliotecaPU")
    EntityManager em;
    public void create(Ejemplares ejemplares) throws PreexistingEntityException, Exception {
        if (ejemplares.getPrestamosList() == null) {
            ejemplares.setPrestamosList(new ArrayList<Prestamos>());
        }
       
        try {
            
          
            Obras obra = ejemplares.getObra();
            if (obra != null) {
                obra = em.getReference(obra.getClass(), obra.getIdObra());
                ejemplares.setObra(obra);
            }
            List<Prestamos> attachedPrestamosList = new ArrayList<Prestamos>();
            for (Prestamos prestamosListPrestamosToAttach : ejemplares.getPrestamosList()) {
                prestamosListPrestamosToAttach = em.getReference(prestamosListPrestamosToAttach.getClass(), prestamosListPrestamosToAttach.getIdPrestamo());
                attachedPrestamosList.add(prestamosListPrestamosToAttach);
            }
            ejemplares.setPrestamosList(attachedPrestamosList);
            em.persist(ejemplares);
            if (obra != null) {
                obra.getEjemplaresList().add(ejemplares);
                obra = em.merge(obra);
            }
            for (Prestamos prestamosListPrestamos : ejemplares.getPrestamosList()) {
                Ejemplares oldIdEjemplarOfPrestamosListPrestamos = prestamosListPrestamos.getIdEjemplar();
                prestamosListPrestamos.setIdEjemplar(ejemplares);
                prestamosListPrestamos = em.merge(prestamosListPrestamos);
                if (oldIdEjemplarOfPrestamosListPrestamos != null) {
                    oldIdEjemplarOfPrestamosListPrestamos.getPrestamosList().remove(prestamosListPrestamos);
                    oldIdEjemplarOfPrestamosListPrestamos = em.merge(oldIdEjemplarOfPrestamosListPrestamos);
                }
            }
            
        } catch (Exception ex) {
            if (findEjemplares(ejemplares.getIdEjemplar()) != null) {
                throw new PreexistingEntityException("Ejemplares " + ejemplares + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
               
            }
        }
    }

    public void edit(Ejemplares ejemplares) throws NonexistentEntityException, Exception {
       
        try {
            
          
            Ejemplares persistentEjemplares = em.find(Ejemplares.class, ejemplares.getIdEjemplar());
            Obras obraOld = persistentEjemplares.getObra();
            Obras obraNew = ejemplares.getObra();
            List<Prestamos> prestamosListOld = persistentEjemplares.getPrestamosList();
            List<Prestamos> prestamosListNew = ejemplares.getPrestamosList();
            if (obraNew != null) {
                obraNew = em.getReference(obraNew.getClass(), obraNew.getIdObra());
                ejemplares.setObra(obraNew);
            }
            List<Prestamos> attachedPrestamosListNew = new ArrayList<Prestamos>();
            for (Prestamos prestamosListNewPrestamosToAttach : prestamosListNew) {
                prestamosListNewPrestamosToAttach = em.getReference(prestamosListNewPrestamosToAttach.getClass(), prestamosListNewPrestamosToAttach.getIdPrestamo());
                attachedPrestamosListNew.add(prestamosListNewPrestamosToAttach);
            }
            prestamosListNew = attachedPrestamosListNew;
            ejemplares.setPrestamosList(prestamosListNew);
            ejemplares = em.merge(ejemplares);
            if (obraOld != null && !obraOld.equals(obraNew)) {
                obraOld.getEjemplaresList().remove(ejemplares);
                obraOld = em.merge(obraOld);
            }
            if (obraNew != null && !obraNew.equals(obraOld)) {
                obraNew.getEjemplaresList().add(ejemplares);
                obraNew = em.merge(obraNew);
            }
            for (Prestamos prestamosListOldPrestamos : prestamosListOld) {
                if (!prestamosListNew.contains(prestamosListOldPrestamos)) {
                    prestamosListOldPrestamos.setIdEjemplar(null);
                    prestamosListOldPrestamos = em.merge(prestamosListOldPrestamos);
                }
            }
            for (Prestamos prestamosListNewPrestamos : prestamosListNew) {
                if (!prestamosListOld.contains(prestamosListNewPrestamos)) {
                    Ejemplares oldIdEjemplarOfPrestamosListNewPrestamos = prestamosListNewPrestamos.getIdEjemplar();
                    prestamosListNewPrestamos.setIdEjemplar(ejemplares);
                    prestamosListNewPrestamos = em.merge(prestamosListNewPrestamos);
                    if (oldIdEjemplarOfPrestamosListNewPrestamos != null && !oldIdEjemplarOfPrestamosListNewPrestamos.equals(ejemplares)) {
                        oldIdEjemplarOfPrestamosListNewPrestamos.getPrestamosList().remove(prestamosListNewPrestamos);
                        oldIdEjemplarOfPrestamosListNewPrestamos = em.merge(oldIdEjemplarOfPrestamosListNewPrestamos);
                    }
                }
            }
            
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = ejemplares.getIdEjemplar();
                if (findEjemplares(id) == null) {
                    throw new NonexistentEntityException("The ejemplares with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
               
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
       
        try {
            
          
            Ejemplares ejemplares;
            try {
                ejemplares = em.getReference(Ejemplares.class, id);
                ejemplares.getIdEjemplar();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ejemplares with id " + id + " no longer exists.", enfe);
            }
            Obras obra = ejemplares.getObra();
            if (obra != null) {
                obra.getEjemplaresList().remove(ejemplares);
                obra = em.merge(obra);
            }
            List<Prestamos> prestamosList = ejemplares.getPrestamosList();
            for (Prestamos prestamosListPrestamos : prestamosList) {
                prestamosListPrestamos.setIdEjemplar(null);
                prestamosListPrestamos = em.merge(prestamosListPrestamos);
            }
            em.remove(ejemplares);
            
        } finally {
            if (em != null) {
               
            }
        }
    }

    public List<Ejemplares> findEjemplaresEntities() {
        return findEjemplaresEntities(true, -1, -1);
    }

    public List<Ejemplares> findEjemplaresEntities(int maxResults, int firstResult) {
        return findEjemplaresEntities(false, maxResults, firstResult);
    }

    private List<Ejemplares> findEjemplaresEntities(boolean all, int maxResults, int firstResult) {
         
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ejemplares.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
           
        }
    }

    public Ejemplares findEjemplares(BigDecimal id) {
         
        try {
            return em.find(Ejemplares.class, id);
        } finally {
           
        }
    }

    public int getEjemplaresCount() {
         
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ejemplares> rt = cq.from(Ejemplares.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
           
        }
    }
    
}
