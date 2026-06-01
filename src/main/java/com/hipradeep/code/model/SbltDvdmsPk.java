package com.hipradeep.code.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "selectPk", query = "select p.pkValue from SbltDvdmsPk p where p.pkName = :pkName"),
		@NamedQuery(name = "updatePk", query = "update SbltDvdmsPk p set p.pkValue = :pkValue where p.pkName = :pkName")
})
@Entity
@Table(name = "sblt_dvdms_pk")
public class SbltDvdmsPk implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String pkName;
	private Integer pkValue;

	public SbltDvdmsPk() {
	}

	@Id
	@Column(name = "pk_name", nullable = false, length = 100)
	public String getPkName() {
		return this.pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	@Column(name = "pk_value", precision = 50, scale = 0)
	public Integer getPkValue() {
		return this.pkValue;
	}

	public void setPkValue(Integer pkValue) {
		this.pkValue = pkValue;
	}
}
