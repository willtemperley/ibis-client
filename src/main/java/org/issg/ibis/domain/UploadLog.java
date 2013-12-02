package org.issg.ibis.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = "ibis", name = "upload_log")
public class UploadLog {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.upload_log_id_seq")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.intValue();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof UploadLog) {
           UploadLog otherObj = (UploadLog) obj;
           if (otherObj.getId().equals(this.getId())) {
                return true;
           }
           return false;
        }
        return super.equals(obj);
    }
}
