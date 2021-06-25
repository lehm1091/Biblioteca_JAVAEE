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
import entity.Autores;
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
public class ObrasJpaController implements Serializable {

    @PersistenceContext(unitName = "bibliotecaPU")
    EntityManager em;

    public Obras create(Obras obras) throws PreexistingEntityException, RollbackFailureException, Exception {

        try {
            obras.setAutoresList(new ArrayList<>());
            em.persist(obras);
            em.flush();
        } catch (Exception ex) {

            throw ex;
        } finally {
            if (em != null) {

            }
        }

        return obras;
    }

    public void create_(Obras obras) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (obras.getAutoresList() == null) {
            obras.setAutoresList(new ArrayList<Autores>());
        }
        System.out.println("in ccreate_");
        try {

            
            System.out.println(obras.getAutoresList().toString());
            for (Autores autor : obras.getAutoresList()) {
                autor.getObrasList().add(obras);
                System.out.println("merge autor");
                em.merge(autor);
            }

        } catch (Exception ex) {

            throw ex;
        } finally {
            if (em != null) {

            }
        }
    }

    public void edit(Obras obras) throws NonexistentEntityException, RollbackFailureException, Exception {

        try {

            Obras persistentObras = em.find(Obras.class, obras.getIdObra());
            List<Autores> obrasListOld = persistentObras.getAutoresList();
            List<Autores> obrasListNew = obras.getAutoresList();
            List<Autores> attachedAutoresListNew = new ArrayList<Autores>();
            for (Autores obrasListNewAutoresToAttach : obrasListNew) {
                obrasListNewAutoresToAttach = em.getReference(obrasListNewAutoresToAttach.getClass(), obrasListNewAutoresToAttach.getIdAutor());
                attachedAutoresListNew.add(obrasListNewAutoresToAttach);
            }
            obrasListNew = attachedAutoresListNew;
            obras.setAutoresList(obrasListNew);
            obras = em.merge(obras);
            for (Autores obrasListOldAutores : obrasListOld) {
                if (!obrasListNew.contains(obrasListOldAutores)) {
                    obrasListOldAutores.getObrasList().remove(obras);
                    obrasListOldAutores = em.merge(obrasListOldAutores);
                }
            }
            for (Autores obrasListNewAutores : obrasListNew) {
                if (!obrasListOld.contains(obrasListNewAutores)) {
                    obrasListNewAutores.getObrasList().add(obras);
                    obrasListNewAutores = em.merge(obrasListNewAutores);
                }
            }

        } catch (Exception ex) {
            try {

            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = obras.getIdObra();
                if (findObras(id) == null) {
                    throw new NonexistentEntityException("The obras with id " + id + " no longer exists.");
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

            Obras obras;
            try {
                obras = em.getReference(Obras.class, id);
                obras.getIdObra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The obras with id " + id + " no longer exists.", enfe);
            }
            List<Autores> obrasList = obras.getAutoresList();
            for (Autores obrasListAutores : obrasList) {
                obrasListAutores.getObrasList().remove(obras);
                obrasListAutores = em.merge(obrasListAutores);
            }
            em.remove(obras);

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

    public List<Obras> findObrasEntities() {
        return findObrasEntities(true, -1, -1);
    }

    public List<Obras> findObrasEntities(int maxResults, int firstResult) {
        return findObrasEntities(false, maxResults, firstResult);
    }

    private List<Obras> findObrasEntities(boolean all, int maxResults, int firstResult) {

        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Obras.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {

        }
    }

    public Obras findObras(BigDecimal id) {

        try {
            return em.find(Obras.class, id);
        } finally {

        }
    }

    public int getObrasCount() {

        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Obras> rt = cq.from(Obras.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {

        }
    }

}
