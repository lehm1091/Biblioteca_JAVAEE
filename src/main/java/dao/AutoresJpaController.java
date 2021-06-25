/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dao.exceptions.RollbackFailureException;
import entity.Autores;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Obras;
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
public class AutoresJpaController implements Serializable {

    @PersistenceContext(unitName = "bibliotecaPU")
    EntityManager em;

    public void create(Autores autores) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (autores.getObrasList() == null) {
            autores.setObrasList(new ArrayList<Obras>());
        }
       
        try {

            List<Obras> attachedObrasList = new ArrayList<Obras>();
            for (Obras obrasListObrasToAttach : autores.getObrasList()) {
                obrasListObrasToAttach = em.getReference(obrasListObrasToAttach.getClass(), obrasListObrasToAttach.getIdObra());
                attachedObrasList.add(obrasListObrasToAttach);
            }
            autores.setObrasList(attachedObrasList);
            em.persist(autores);
            for (Obras obrasListObras : autores.getObrasList()) {
                obrasListObras.getAutoresList().add(autores);
                obrasListObras = em.merge(obrasListObras);
            }

        } catch (Exception ex) {
           
            throw ex;
        } finally {
            if (em != null) {

            }
        }
    }

    public void edit(Autores autores) throws NonexistentEntityException, RollbackFailureException, Exception {
       
        try {

            Autores persistentAutores = em.find(Autores.class, autores.getIdAutor());
            List<Obras> obrasListOld = persistentAutores.getObrasList();
            List<Obras> obrasListNew = autores.getObrasList();
            List<Obras> attachedObrasListNew = new ArrayList<Obras>();
            for (Obras obrasListNewObrasToAttach : obrasListNew) {
                obrasListNewObrasToAttach = em.getReference(obrasListNewObrasToAttach.getClass(), obrasListNewObrasToAttach.getIdObra());
                attachedObrasListNew.add(obrasListNewObrasToAttach);
            }
            obrasListNew = attachedObrasListNew;
            autores.setObrasList(obrasListNew);
            autores = em.merge(autores);
            for (Obras obrasListOldObras : obrasListOld) {
                if (!obrasListNew.contains(obrasListOldObras)) {
                    obrasListOldObras.getAutoresList().remove(autores);
                    obrasListOldObras = em.merge(obrasListOldObras);
                }
            }
            for (Obras obrasListNewObras : obrasListNew) {
                if (!obrasListOld.contains(obrasListNewObras)) {
                    obrasListNewObras.getAutoresList().add(autores);
                    obrasListNewObras = em.merge(obrasListNewObras);
                }
            }

        } catch (Exception ex) {
            try {

            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = autores.getIdAutor();
                if (findAutores(id) == null) {
                    throw new NonexistentEntityException("The autores with id " + id + " no longer exists.");
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

            Autores autores;
            try {
                autores = em.getReference(Autores.class, id);
                autores.getIdAutor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autores with id " + id + " no longer exists.", enfe);
            }
            List<Obras> obrasList = autores.getObrasList();
            for (Obras obrasListObras : obrasList) {
                obrasListObras.getAutoresList().remove(autores);
                obrasListObras = em.merge(obrasListObras);
            }
            em.remove(autores);

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

    public List<Autores> findAutoresEntities() {
        return findAutoresEntities(true, -1, -1);
    }

    public List<Autores> findAutoresEntities(int maxResults, int firstResult) {
        return findAutoresEntities(false, maxResults, firstResult);
    }

    private List<Autores> findAutoresEntities(boolean all, int maxResults, int firstResult) {

        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Autores.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {

        }
    }

    public Autores findAutores(BigDecimal id) {

        try {
            return em.find(Autores.class, id);
        } finally {

        }
    }

    public int getAutoresCount() {

        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Autores> rt = cq.from(Autores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {

        }
    }

}
