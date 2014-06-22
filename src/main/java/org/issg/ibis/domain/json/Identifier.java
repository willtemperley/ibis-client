package org.issg.ibis.domain.json;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Identifier {

	@Expose
	private Long usageKey;
	@Expose
	private String datasetKey;
	@Expose
	private String type;
	@Expose
	private String identifier;
	@Expose
	private Long key;

	public Long getUsageKey() {
		return usageKey;
	}

	public void setUsageKey(Long usageKey) {
		this.usageKey = usageKey;
	}

	public String getDatasetKey() {
		return datasetKey;
	}

	public void setDatasetKey(String datasetKey) {
		this.datasetKey = datasetKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

}
