/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dao.exceptions.RollbackFailureException;
import entity.Ejemplares;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class EjemplaresJpaController implements Serializable {

    @PersistenceContext(unitName = "bibliotecaPU")
    EntityManager em;

    public void create(Ejemplares ejemplares) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (ejemplares.getPrestamosList() == null) {
            ejemplares.setPrestamosList(new ArrayList<Prestamos>());
        }
       
        try {

            List<Prestamos> attachedPrestamosList = new ArrayList<Prestamos>();
            for (Prestamos prestamosListPrestamosToAttach : ejemplares.getPrestamosList()) {
                prestamosListPrestamosToAttach = em.getReference(prestamosListPrestamosToAttach.getClass(), prestamosListPrestamosToAttach.getIdPrestamo());
                attachedPrestamosList.add(prestamosListPrestamosToAttach);
            }
            ejemplares.setPrestamosList(attachedPrestamosList);
            em.persist(ejemplares);
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
            try {

            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEjemplares(ejemplares.getIdEjemplar()) != null) {
                throw new PreexistingEntityException("Ejemplares " + ejemplares + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {

            }
        }
    }

    public void edit(Ejemplares ejemplares) throws NonexistentEntityException, RollbackFailureException, Exception {
       
        try {

            Ejemplares persistentEjemplares = em.find(Ejemplares.class, ejemplares.getIdEjemplar());
            List<Prestamos> prestamosListOld = persistentEjemplares.getPrestamosList();
            List<Prestamos> prestamosListNew = ejemplares.getPrestamosList();
            List<Prestamos> attachedPrestamosListNew = new ArrayList<Prestamos>();
            for (Prestamos prestamosListNewPrestamosToAttach : prestamosListNew) {
                prestamosListNewPrestamosToAttach = em.getReference(prestamosListNewPrestamosToAttach.getClass(), prestamosListNewPrestamosToAttach.getIdPrestamo());
                attachedPrestamosListNew.add(prestamosListNewPrestamosToAttach);
            }
            prestamosListNew = attachedPrestamosListNew;
            ejemplares.setPrestamosList(prestamosListNew);
            ejemplares = em.merge(ejemplares);
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
            try {

            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
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

    public void destroy(BigDecimal id) throws NonexistentEntityException, RollbackFailureException, Exception {
       
        try {

            Ejemplares ejemplares;
            try {
                ejemplares = em.getReference(Ejemplares.class, id);
                ejemplares.getIdEjemplar();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ejemplares with id " + id + " no longer exists.", enfe);
            }
            List<Prestamos> prestamosList = ejemplares.getPrestamosList();
            for (Prestamos prestamosListPrestamos : prestamosList) {
                prestamosListPrestamos.setIdEjemplar(null);
                prestamosListPrestamos = em.merge(prestamosListPrestamos);
            }
            em.remove(ejemplares);

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
