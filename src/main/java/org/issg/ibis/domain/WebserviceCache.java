package org.issg.ibis.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(schema = "ibis", name = "webservice_cache")
public class WebserviceCache {

	private String id;

	@Id
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String result;

    @Column
    public String getResult() {
		return result;
	}
    
    public void setResult(String result) {
		this.result = result;
	}
    
    private Date accessDate;
    
    @NotNull
    @Column(name = "access_date")
    public Date getAccessDate() {
		return accessDate;
	}
    
    public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}

}