/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Autores;
import java.util.ArrayList;
import java.util.List;
import entity.Ejemplares;
import entity.Obras;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Luis
 */
public class ObrasJpaController1 implements Serializable {

    public ObrasJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Obras obras) {
        if (obras.getAutoresList() == null) {
            obras.setAutoresList(new ArrayList<Autores>());
        }
        if (obras.getEjemplaresList() == null) {
            obras.setEjemplaresList(new ArrayList<Ejemplares>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Autores> attachedAutoresList = new ArrayList<Autores>();
            for (Autores autoresListAutoresToAttach : obras.getAutoresList()) {
                autoresListAutoresToAttach = em.getReference(autoresListAutoresToAttach.getClass(), autoresListAutoresToAttach.getIdAutor());
                attachedAutoresList.add(autoresListAutoresToAttach);
            }
            obras.setAutoresList(attachedAutoresList);
            List<Ejemplares> attachedEjemplaresList = new ArrayList<Ejemplares>();
            for (Ejemplares ejemplaresListEjemplaresToAttach : obras.getEjemplaresList()) {
                ejemplaresListEjemplaresToAttach = em.getReference(ejemplaresListEjemplaresToAttach.getClass(), ejemplaresListEjemplaresToAttach.getIdEjemplar());
                attachedEjemplaresList.add(ejemplaresListEjemplaresToAttach);
            }
            obras.setEjemplaresList(attachedEjemplaresList);
            em.persist(obras);
            for (Autores autoresListAutores : obras.getAutoresList()) {
                autoresListAutores.getObrasList().add(obras);
                autoresListAutores = em.merge(autoresListAutores);
            }
            for (Ejemplares ejemplaresListEjemplares : obras.getEjemplaresList()) {
                Obras oldObraOfEjemplaresListEjemplares = ejemplaresListEjemplares.getObra();
                ejemplaresListEjemplares.setObra(obras);
                ejemplaresListEjemplares = em.merge(ejemplaresListEjemplares);
                if (oldObraOfEjemplaresListEjemplares != null) {
                    oldObraOfEjemplaresListEjemplares.getEjemplaresList().remove(ejemplaresListEjemplares);
                    oldObraOfEjemplaresListEjemplares = em.merge(oldObraOfEjemplaresListEjemplares);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Obras obras) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Obras persistentObras = em.find(Obras.class, obras.getIdObra());
            List<Autores> autoresListOld = persistentObras.getAutoresList();
            List<Autores> autoresListNew = obras.getAutoresList();
            List<Ejemplares> ejemplaresListOld = persistentObras.getEjemplaresList();
            List<Ejemplares> ejemplaresListNew = obras.getEjemplaresList();
            List<Autores> attachedAutoresListNew = new ArrayList<Autores>();
            for (Autores autoresListNewAutoresToAttach : autoresListNew) {
                autoresListNewAutoresToAttach = em.getReference(autoresListNewAutoresToAttach.getClass(), autoresListNewAutoresToAttach.getIdAutor());
                attachedAutoresListNew.add(autoresListNewAutoresToAttach);
            }
            autoresListNew = attachedAutoresListNew;
            obras.setAutoresList(autoresListNew);
            List<Ejemplares> attachedEjemplaresListNew = new ArrayList<Ejemplares>();
            for (Ejemplares ejemplaresListNewEjemplaresToAttach : ejemplaresListNew) {
                ejemplaresListNewEjemplaresToAttach = em.getReference(ejemplaresListNewEjemplaresToAttach.getClass(), ejemplaresListNewEjemplaresToAttach.getIdEjemplar());
                attachedEjemplaresListNew.add(ejemplaresListNewEjemplaresToAttach);
            }
            ejemplaresListNew = attachedEjemplaresListNew;
            obras.setEjemplaresList(ejemplaresListNew);
            obras = em.merge(obras);
            for (Autores autoresListOldAutores : autoresListOld) {
                if (!autoresListNew.contains(autoresListOldAutores)) {
                    autoresListOldAutores.getObrasList().remove(obras);
                    autoresListOldAutores = em.merge(autoresListOldAutores);
                }
            }
            for (Autores autoresListNewAutores : autoresListNew) {
                if (!autoresListOld.contains(autoresListNewAutores)) {
                    autoresListNewAutores.getObrasList().add(obras);
                    autoresListNewAutores = em.merge(autoresListNewAutores);
                }
            }
            for (Ejemplares ejemplaresListOldEjemplares : ejemplaresListOld) {
                if (!ejemplaresListNew.contains(ejemplaresListOldEjemplares)) {
                    ejemplaresListOldEjemplares.setObra(null);
                    ejemplaresListOldEjemplares = em.merge(ejemplaresListOldEjemplares);
                }
            }
            for (Ejemplares ejemplaresListNewEjemplares : ejemplaresListNew) {
                if (!ejemplaresListOld.contains(ejemplaresListNewEjemplares)) {
                    Obras oldObraOfEjemplaresListNewEjemplares = ejemplaresListNewEjemplares.getObra();
                    ejemplaresListNewEjemplares.setObra(obras);
                    ejemplaresListNewEjemplares = em.merge(ejemplaresListNewEjemplares);
                    if (oldObraOfEjemplaresListNewEjemplares != null && !oldObraOfEjemplaresListNewEjemplares.equals(obras)) {
                        oldObraOfEjemplaresListNewEjemplares.getEjemplaresList().remove(ejemplaresListNewEjemplares);
                        oldObraOfEjemplaresListNewEjemplares = em.merge(oldObraOfEjemplaresListNewEjemplares);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
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
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Obras obras;
            try {
                obras = em.getReference(Obras.class, id);
                obras.getIdObra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The obras with id " + id + " no longer exists.", enfe);
            }
            List<Autores> autoresList = obras.getAutoresList();
            for (Autores autoresListAutores : autoresList) {
                autoresListAutores.getObrasList().remove(obras);
                autoresListAutores = em.merge(autoresListAutores);
            }
            List<Ejemplares> ejemplaresList = obras.getEjemplaresList();
            for (Ejemplares ejemplaresListEjemplares : ejemplaresList) {
                ejemplaresListEjemplares.setObra(null);
                ejemplaresListEjemplares = em.merge(ejemplaresListEjemplares);
            }
            em.remove(obras);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
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
        EntityManager em = getEntityManager();
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
            em.close();
        }
    }

    public Obras findObras(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Obras.class, id);
        } finally {
            em.close();
        }
    }

    public int getObrasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Obras> rt = cq.from(Obras.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
