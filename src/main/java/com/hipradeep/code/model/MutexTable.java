package com.hipradeep.code.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "selectPk", query = "select p.pkValue from MutexTable p where p.pkName = :pkName"),
		@NamedQuery(name = "updatePk", query = "update MutexTable p set p.pkValue = :pkValue where p.pkName = :pkName")
})
@Entity
@Table(name = "mutex_table")
public class MutexTable implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String pkName;
	private Integer pkValue;

	public MutexTable() {
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
