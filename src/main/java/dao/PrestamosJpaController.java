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
import entity.Ejemplares;
import entity.Prestamos;
import entity.Usuarios;
import java.math.BigDecimal;
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
public class PrestamosJpaController implements Serializable {

    @PersistenceContext(unitName = "bibliotecaPU")
    EntityManager em;

    public void create(Prestamos prestamos) throws PreexistingEntityException, RollbackFailureException, Exception {

        try {

            Ejemplares idEjemplar = prestamos.getIdEjemplar();
            if (idEjemplar != null) {
                idEjemplar = em.getReference(idEjemplar.getClass(), idEjemplar.getIdEjemplar());
                prestamos.setIdEjemplar(idEjemplar);
            }
            Usuarios idUsuario = prestamos.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                prestamos.setIdUsuario(idUsuario);
            }
            em.persist(prestamos);
            System.out.println(prestamos.toString());
            if (idEjemplar != null) {
                idEjemplar.getPrestamosList().add(prestamos);
                idEjemplar = em.merge(idEjemplar);
            }
            if (idUsuario != null) {
                idUsuario.getPrestamosList().add(prestamos);
                idUsuario = em.merge(idUsuario);
            }

        } catch (Exception ex) {
            try {

            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPrestamos(prestamos.getIdPrestamo()) != null) {
                throw new PreexistingEntityException("Prestamos " + prestamos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {

            }
        }
    }

    public void edit(Prestamos prestamos) throws NonexistentEntityException, RollbackFailureException, Exception {

        try {

            Prestamos persistentPrestamos = em.find(Prestamos.class, prestamos.getIdPrestamo());
            Ejemplares idEjemplarOld = persistentPrestamos.getIdEjemplar();
            Ejemplares idEjemplarNew = prestamos.getIdEjemplar();
            Usuarios idUsuarioOld = persistentPrestamos.getIdUsuario();
            Usuarios idUsuarioNew = prestamos.getIdUsuario();
            if (idEjemplarNew != null) {
                idEjemplarNew = em.getReference(idEjemplarNew.getClass(), idEjemplarNew.getIdEjemplar());
                prestamos.setIdEjemplar(idEjemplarNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                prestamos.setIdUsuario(idUsuarioNew);
            }
            prestamos = em.merge(prestamos);
            if (idEjemplarOld != null && !idEjemplarOld.equals(idEjemplarNew)) {
                idEjemplarOld.getPrestamosList().remove(prestamos);
                idEjemplarOld = em.merge(idEjemplarOld);
            }
            if (idEjemplarNew != null && !idEjemplarNew.equals(idEjemplarOld)) {
                idEjemplarNew.getPrestamosList().add(prestamos);
                idEjemplarNew = em.merge(idEjemplarNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getPrestamosList().remove(prestamos);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getPrestamosList().add(prestamos);
                idUsuarioNew = em.merge(idUsuarioNew);
            }

        } catch (Exception ex) {
            try {

            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = prestamos.getIdPrestamo();
                if (findPrestamos(id) == null) {
                    throw new NonexistentEntityException("The prestamos with id " + id + " no longer exists.");
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

            Prestamos prestamos;
            try {
                prestamos = em.getReference(Prestamos.class, id);
                prestamos.getIdPrestamo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prestamos with id " + id + " no longer exists.", enfe);
            }
            Ejemplares idEjemplar = prestamos.getIdEjemplar();
            if (idEjemplar != null) {
                idEjemplar.getPrestamosList().remove(prestamos);
                idEjemplar = em.merge(idEjemplar);
            }
            Usuarios idUsuario = prestamos.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getPrestamosList().remove(prestamos);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(prestamos);

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

    public List<Prestamos> findPrestamosEntities() {
        return findPrestamosEntities(true, -1, -1);
    }

    public List<Prestamos> findPrestamosEntities(int maxResults, int firstResult) {
        return findPrestamosEntities(false, maxResults, firstResult);
    }

    private List<Prestamos> findPrestamosEntities(boolean all, int maxResults, int firstResult) {

        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prestamos.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {

        }
    }

    public Prestamos findPrestamos(BigDecimal id) {

        try {
            return em.find(Prestamos.class, id);
        } finally {

        }
    }

    public int getPrestamosCount() {

        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prestamos> rt = cq.from(Prestamos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {

        }
    }

}
