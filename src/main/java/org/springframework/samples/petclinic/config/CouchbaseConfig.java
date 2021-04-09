package org.springframework.samples.petclinic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

	@Override
	public String getConnectionString() {
		return "couchbase://127.0.0.1";
	}

	@Override
	public String getUserName() {
		return "Administrator";
	}

	@Override
	public String getPassword() {
		return "password";
	}

	@Override
	public String getBucketName() {
		return "default";
	}

	// NOTE: Optional - If not specified the default attribute name will be "_class"
	public String typeKey() {
		return "type";
	}

	// NOTE Use only on index
	@Override
	protected boolean autoIndexCreation() {
		return true;
	}

}
