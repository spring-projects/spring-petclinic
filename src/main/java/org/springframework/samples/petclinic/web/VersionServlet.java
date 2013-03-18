package org.springframework.samples.petclinic.web;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://stackoverflow.com/questions/14934299/how-to-get-package-version-at-running-tomcat
 * http://stackoverflow.com/questions/2712970/how-to-get-maven-artifact-version-at-runtime
 * http://stackoverflow.com/questions/809775/what-does-the-servlet-load-on-startup-value-of-0-zero-signify
 * 
 * Example of MANIFEST.MF from META-INF/
<pre>
Manifest-Version: 1.0
Implementation-Vendor: Funshion
Implementation-Title: atlas-app
Implementation-Version: 1.1.5.M2.0-SNAPSHOT
Implementation-Vendor-Id: com.funshion.microlens
Built-By: weibl
Build-Jdk: 1.6.0_37
Specification-Vendor: Funshion
Specification-Title: atlas-app
Created-By: Apache Maven 3.0.4
Specification-Version: 1.1.5.M2.0-SNAPSHOT
Archiver-Version: Plexus Archiver
<pre/>
 * @author Paul Verest 
 * Reviewer
 * @since 2013-3-18
 */
public class VersionServlet extends HttpServlet {
	private static final long serialVersionUID = -2805284042253311925L;
	protected static final Logger logger = LoggerFactory.getLogger(VersionServlet.class);

	public static String version = "UNDEFINED";
	
	@Override
	public void init() throws ServletException {
		version  = getClass().getPackage().getImplementationVersion();	
		if (version==null) {
			Properties prop = new Properties();
			try {
				prop.load(getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF"));
				version = prop.getProperty("Implementation-Version");
			} catch (IOException e) {
				logger.error(e.toString());
			}
		}
		logger.info("Starting Spring Pet Clinic application version "+version);
	}
	
	@Override
	public void destroy() {
		logger.info("Stopping Spring Pet Clinic application version "+version);
	}

}
