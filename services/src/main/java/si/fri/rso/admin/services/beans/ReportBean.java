
package si.fri.rso.admin.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import si.fri.rso.admin.lib.Ocena;
import si.fri.rso.admin.lib.Report;
import si.fri.rso.admin.models.converters.ReportConverter;
import si.fri.rso.admin.models.entities.ReportEntity;


@RequestScoped
public class ReportBean {

    private Logger log = Logger.getLogger(ReportBean.class.getName());

    @Inject
    private EntityManager em;

    /**  GET ALL **/
    public List<Report> getAllReports() {
        TypedQuery<ReportEntity> query = em.createNamedQuery(
                "ReportEntity.getAll",  ReportEntity.class);

        List<ReportEntity> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return  null;
        }
        return resultList.stream().map(ReportConverter::toDto).collect(Collectors.toList());
    }

    /**  GET BY USER ID **/
    public List<Report> getReportsByUserId(Integer userId) {
        TypedQuery<ReportEntity> query = em.createNamedQuery(
                "ReportEntity.getByUserId",  ReportEntity.class);
        query.setParameter("userId", userId);

        List<ReportEntity> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return  null;
        }
        return resultList.stream().map(ReportConverter::toDto).collect(Collectors.toList());
    }
    /**  GET BY ID **/
    public Report getOceneById(Integer id) {
        TypedQuery<ReportEntity> query = em.createNamedQuery(
                "ReportEntity.getById",  ReportEntity.class);
        query.setParameter("id", id);

        List<ReportEntity> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return  null;
        }
        return ReportConverter.toDto(resultList.get(0));
    }
    /** POST **/
    public Report createReport(Report r) {

        ReportEntity reportEntity = ReportConverter.toEntity(r);


        if (reportEntity != null) {
            try {
                beginTx();
                em.persist(reportEntity);
                commitTx();
                em.refresh(reportEntity);
            }
            catch (Exception e) {
                reportEntity.setKomentar(e.getMessage());
                rollbackTx();
                Report rr = ReportConverter.toDto(reportEntity);
                rr.setTimestamp(999999999999L);
                return rr;
            }
        }
        else {
            return null;
        }

        return ReportConverter.toDto(reportEntity);
    }

    /** DELETE **/
    public boolean deleteReport(Integer id) {

        ReportEntity reportEntity = em.find(ReportEntity.class, id);


        if (reportEntity != null) {
            try {
                beginTx();
                em.remove(reportEntity);
                commitTx();
                em.refresh(reportEntity);
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    /**  TRANSACTION METHODS **/
    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}