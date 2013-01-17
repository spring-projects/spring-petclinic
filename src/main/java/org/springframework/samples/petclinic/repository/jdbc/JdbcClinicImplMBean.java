package org.springframework.samples.petclinic.repository.jdbc;

/**
 * Interface that defines a cache refresh operation.
 * To be exposed for management via JMX.
 * 
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @see JdbcClinicImpl
 */
public interface JdbcClinicImplMBean {

	/**
	 * Refresh the cache of Vets that the ClinicService is holding.
	 * @see org.springframework.samples.petclinic.service.ClinicService#getVets()
	 * @see JdbcClinicImpl#refreshVetsCache()
	 */
	void refreshVetsCache();

}
