package com.hipradeep.code.jpa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.hipradeep.code.model.HsttBankMst;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.query.Query;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * Custom Hibernate Identifier Generator for legacy DVDMS bank entities.
 */
public class DvdmsIdGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

		if (object instanceof HsttBankMst) {
			HsttBankMst bankTypeMst = (HsttBankMst) object;

			if (bankTypeMst.getGnumBankId() != null && bankTypeMst.getGnumBankId() != 0) {
				return bankTypeMst.getGnumBankId();
			}

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("gnumHospitalCode", bankTypeMst.getGnumHospitalCode());

			return this.generatePk(session, "hstt_bank_mst", params);
		}

		return 1001; // Fallback sequence base
	}

	/**
	 * Generates a primary key integer using the registered Hibernate NamedQuery.
	 */
	private Integer generatePk(SharedSessionContractImplementor session, String queryName, Map<String, Object> params) {
		try {
			// Acquire pessimistic write lock on the corresponding row in sblt_dvdms_pk
			session.getNamedQuery("selectPk")
					.setParameter("pkName", queryName)
					.setLockOptions(LockOptions.UPGRADE)
					.uniqueResult();

			Query<?> queryObj = session.getNamedQuery(queryName);
			if (params != null) {
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					queryObj.setParameter(entry.getKey(), entry.getValue());
				}
			}
			Object result = queryObj.uniqueResult();

			// Execute dummy update to keep the database lock registered/consistent
			session.getNamedQuery("updatePk")
					.setParameter("pkValue", 0)
					.setParameter("pkName", queryName)
					.executeUpdate();

			if (result instanceof Number) {
				return ((Number) result).intValue();
			}
			return 1001; // Default starting value if null
		} catch (Exception e) {
			throw new HibernateException("Failed to generate sequence key using NamedQuery: " + queryName, e);
		}
	}
}
