package com.hipradeep.code.model;

import java.io.Serializable;

/**
 * Composite Primary Key class for HsttBankMst.
 */
public class HsttBankMstId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer gnumBankId;
    private Integer gnumHospitalCode;

    public HsttBankMstId() {
    }

    public HsttBankMstId(Integer gnumBankId, Integer gnumHospitalCode) {
        this.gnumBankId = gnumBankId;
        this.gnumHospitalCode = gnumHospitalCode;
    }

    public Integer getGnumBankId() {
        return gnumBankId;
    }

    public void setGnumBankId(Integer gnumBankId) {
        this.gnumBankId = gnumBankId;
    }

    public Integer getGnumHospitalCode() {
        return gnumHospitalCode;
    }

    public void setGnumHospitalCode(Integer gnumHospitalCode) {
        this.gnumHospitalCode = gnumHospitalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HsttBankMstId)) return false;
        HsttBankMstId that = (HsttBankMstId) o;
        return (gnumBankId != null ? gnumBankId.equals(that.gnumBankId) : that.gnumBankId == null) &&
                (gnumHospitalCode != null ? gnumHospitalCode.equals(that.gnumHospitalCode) : that.gnumHospitalCode == null);
    }

    @Override
    public int hashCode() {
        int result = gnumBankId != null ? gnumBankId.hashCode() : 0;
        result = 31 * result + (gnumHospitalCode != null ? gnumHospitalCode.hashCode() : 0);
        return result;
    }
}
