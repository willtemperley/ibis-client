package org.issg.ibis.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = "ibis", name = "redlist_category")
public class RedlistCategory {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.redlist_code_id_seq")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    private String redlistCode;

    @Column(name="redlist_code")
    public String getRedlistCode() {
        return redlistCode;
    }

    public void setRedlistCode(String redlistCode) {
        this.redlistCode = redlistCode;
    }

    private String label;

    @Column
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

        if (obj instanceof RedlistCategory) {
           RedlistCategory otherObj = (RedlistCategory) obj;
           if (otherObj.getId().equals(this.getId())) {
                return true;
            }
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return label;
    }
}
