package si.fri.rso.admin.models.entities;

import si.fri.rso.admin.lib.Ocena;
import si.fri.rso.admin.lib.Report;
import si.fri.rso.admin.models.converters.ReportConverter;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "reports")
@NamedQueries(value =
        {
                @NamedQuery(name = "ReportEntity.getAll",
                        query = "SELECT report FROM ReportEntity report"),

                @NamedQuery(name = "ReportEntity.getById",
                        query = "SELECT report FROM ReportEntity report WHERE report.id = :id"),

                @NamedQuery(name = "ReportEntity.getByUserId",
                        query = "SELECT report FROM ReportEntity report WHERE report.userId = :userId")

        })
public class ReportEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Integer id;

        @Column(name = "ocenaId", unique=true)
        private Integer ocenaId;

        @Column(name = "userId")
        private Integer userId;

        @Column(name = "komentar")
        private String komentar;

        @Column(name = "timestamp")
        private Long timestamp;


        // ============= Getters and Setters =============


        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public Integer getOcenaId() {
                return ocenaId;
        }

        public void setOcenaId(Integer ocenaId) {
                this.ocenaId = ocenaId;
        }

        public Integer getUserId() {
                return userId;
        }

        public void setUserId(Integer userId) {
                this.userId = userId;
        }

        public String getKomentar() {
                return komentar;
        }

        public void setKomentar(String komentar) {
                this.komentar = komentar;
        }

        public Long getTimestamp() {
                return timestamp;
        }

        public void setTimestamp(Long timestamp) {
                this.timestamp = timestamp;
        }

        /*
        public List<Report> getReports() {
                // Loop over list and convert from entity
                return ocene.stream().map(ReportConverter::toDto).collect(Collectors.toList());
        }

        public void setOcene(List<OcenaEntity> ocene) {
                this.ocene = ocene;
        }

        public List<Termin> getTermini() {
                return termini.stream().map(TerminConverter::toDto).collect(Collectors.toList());
        }

        public void setTermini(List<TerminEntity> termini) {
                this.termini = termini;
        }
        */

}
