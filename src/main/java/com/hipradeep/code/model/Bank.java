package com.hipradeep.code.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@NamedQueries({ 
	@NamedQuery(name = "bank", query = "select coalesce(max(c.bankId), 1000) + 1  from Bank c")
})
@Entity
@Table(name = "bank")
public class Bank implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer bankId;
	private String bankName;
	private String bankShortName;

	public Bank() {
	}

	public Bank(Integer bankId) {
		this.bankId = bankId;
	}
	
	@Id
	@GeneratedValue(generator = "TableIdGenerator")
	@GenericGenerator(name = "TableIdGenerator", strategy = "com.hipradeep.code.jpa.TableIdGenerator" )
	@Column(name = "bank_id", nullable = false, precision = 4, scale = 0)
	public Integer getBankId() {
		return this.bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	@Column(name = "bank_name", nullable = false, length = 50)
	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name = "bank_short_name", nullable = false, length = 20)
	public String getBankShortName() {
		return this.bankShortName;
	}

	public void setBankShortName(String bankShortName) {
		this.bankShortName = bankShortName;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof Bank))
			return false;
		Bank castOther = (Bank) other;

		return (this.getBankId() != null && this.getBankId().equals(castOther.getBankId()));
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + (this.getBankId() != null ? this.getBankId() : 0);
		return result;
	}
}
