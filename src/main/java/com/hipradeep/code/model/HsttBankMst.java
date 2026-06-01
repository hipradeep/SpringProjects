package com.hipradeep.code.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@NamedQueries({ 
	@NamedQuery(name = "hstt_bank_mst", query = "select coalesce(max(c.gnumBankId), 1000) + 1  from HsttBankMst c where c.gnumHospitalCode =:gnumHospitalCode")
})
@Entity
@IdClass(HsttBankMstId.class)
@Table(name = "hstt_bank_mst")
public class HsttBankMst implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer gnumBankId;
	private Integer gnumHospitalCode;
	private String gstrBankName;
	private String gstrBankShortName;
	private Date gdtEntryDate;
	private Integer gnumIsvalid;

	public HsttBankMst() {
	}

	public HsttBankMst(Integer gnumBankId, Integer gnumHospitalCode) {
		this.gnumBankId = gnumBankId;
		this.gnumHospitalCode = gnumHospitalCode;
	}
	
	@Id
  	@GeneratedValue(generator = "TableIdGenerator")
    @GenericGenerator(name = "TableIdGenerator", strategy = "com.hipradeep.code.jpa.DvdmsIdGenerator" )
	@Column(name = "gnum_bank_id", nullable = false, precision = 4, scale = 0)
	public Integer getGnumBankId() {
		return this.gnumBankId;
	}

	public void setGnumBankId(Integer gnumBankId) {
		this.gnumBankId = gnumBankId;
	}

	@Id
	@Column(name = "gnum_hospital_code", nullable = false, precision = 3, scale = 0)
	public Integer getGnumHospitalCode() {
		return this.gnumHospitalCode;
	}

	public void setGnumHospitalCode(Integer gnumHospitalCode) {
		this.gnumHospitalCode = gnumHospitalCode;
	}

	@Column(name = "gstr_bank_name", nullable = false, length = 50)
	public String getGstrBankName() {
		return this.gstrBankName;
	}

	public void setGstrBankName(String gstrBankName) {
		this.gstrBankName = gstrBankName;
	}

	@Column(name = "gstr_bank_short_name", nullable = false, length = 20)
	public String getGstrBankShortName() {
		return this.gstrBankShortName;
	}

	public void setGstrBankShortName(String gstrBankShortName) {
		this.gstrBankShortName = gstrBankShortName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "gdt_entry_date", length = 8, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	public Date getGdtEntryDate() {
		return this.gdtEntryDate;
	}

	public void setGdtEntryDate(Date gdtEntryDate) {
		this.gdtEntryDate = gdtEntryDate;
	}

	@Column(name = "gnum_isvalid", nullable = false, precision = 1, scale = 0)
	public Integer getGnumIsvalid() {
		return gnumIsvalid;
	}

	public void setGnumIsvalid(Integer gnumIsvalid) {
		this.gnumIsvalid = gnumIsvalid;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof HsttBankMst))
			return false;
		HsttBankMst castOther = (HsttBankMst) other;

		return (this.getGnumBankId() != null && this.getGnumBankId().equals(castOther.getGnumBankId()))
				&& (this.getGnumHospitalCode() != null && this.getGnumHospitalCode().equals(castOther.getGnumHospitalCode()));
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + (this.getGnumBankId() != null ? this.getGnumBankId() : 0);
		result = 37 * result + (this.getGnumHospitalCode() != null ? this.getGnumHospitalCode() : 0);
		return result;
	}
}
