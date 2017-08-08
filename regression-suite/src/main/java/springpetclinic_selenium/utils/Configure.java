package springpetclinic_selenium.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import springpetclinic_selenium.model.Owner;

public class Configure {

    private String petClinicUrl;
    private boolean zapEnabled;
    private String zapIp;
    private int zapPort;
    private String ownerLastNameNotExist;
    private Owner owner;
    private String hubUrl;
    private boolean gridMode;

    public Configure() {
        Properties prop = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(propFileName);
        try {
            if (inputStream != null) {

                prop.load(inputStream);

            } else {
                throw new FileNotFoundException("property file '"
                        + propFileName + "' not found in the classpath");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // get the property value and print it out
        String envPetclinicUrl = System.getProperty("PETCLINIC_URL");
        if (envPetclinicUrl == null)
        {
            envPetclinicUrl = prop.getProperty("spring.petclinic.url");
        }
        String zapEnabled = System.getProperty("ZAP_ENABLED");
        if (zapEnabled == null)
        {
            zapEnabled = prop.getProperty("zap.enabled");
        }
        String zapPort = System.getProperty("ZAP_PORT");
        if (zapPort == null)
        {
            zapPort = prop.getProperty("zap.port");
        }
        String zapIp = System.getProperty("ZAP_IP");
        if (zapIp == null)
        {
            zapIp = prop.getProperty("zap.url");
        }

        setPetClinicUrl(envPetclinicUrl);
        setZapEnabled(Boolean.parseBoolean(zapEnabled));
        setZapIp(zapIp);
        setZapPort(Integer.parseInt(zapPort));
        setOwnerLastNameNotExist(prop.getProperty("test.data.owner1.not.exist.lastName"));
        setHubUrl(prop.getProperty("grid.hub.url"));
        setGridMode(Boolean.parseBoolean(prop.getProperty("grid.enabled")));

        String[] ownerComponants = prop.getProperty("test.data.owner2.add").split(",");
        Owner owner = new Owner(ownerComponants[0],ownerComponants[1],ownerComponants[2],ownerComponants[3],ownerComponants[4]);
        setOwner(owner);

    }

    /**
     * @return surname of user that should not exist.
     */
    public String getOwnerLastNameNotExist(){
        return ownerLastNameNotExist;
    }

    /**
     * Set surname of user that should not exist.
     */
    public void setOwnerLastNameNotExist(String lastName){
        this.ownerLastNameNotExist = lastName;
    }

    /**
     * @return the petClinicUrl
     */
    public String getPetClinicUrl() {
        return petClinicUrl;
    }

    /**
     * @param petClinicUrl the petClinicUrl to set
     */
    public void setPetClinicUrl(String petClinicUrl) {
        this.petClinicUrl = petClinicUrl;
    }

    /**
     * @return the zapEnabled
     */
    public boolean getZapEnabled() {
        return isZapEnabled();
    }

    /**
     * @param zapEnabled the zapEnabled to set
     */
    public void setZapEnabled(Boolean zapEnabled) {
        this.zapEnabled = zapEnabled;
    }

    /**
     * @return the zapIp
     */
    public String getZapIp() {
        return zapIp;
    }

    /**
     * @param zapIp the zapIp to set
     */
    public void setZapIp(String zapIp) {
        this.zapIp = zapIp;
    }

    /**
     * @return the zapPort
     */
    public int getZapPort() {
        return zapPort;
    }

    /**
     * @param zapPort the zapPort to set
     */
    public void setZapPort(int zapPort) {
        this.zapPort = zapPort;
    }

    /**
     * @return the zapEnabled
     */
    private boolean isZapEnabled() {
        return zapEnabled;
    }

    /**
     * @param zapEnabled the zapEnabled to set
     */
    private void setZapEnabled(boolean zapEnabled) {
        this.zapEnabled = zapEnabled;
    }

    /**
     * @return the owner
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

	/**
	 * @return the hubUrl
	 */
	public String getHubUrl() {
		return hubUrl;
	}

	/**
	 * @param hubUrl the hubUrl to set
	 */
	public void setHubUrl(String hubUrl) {
		this.hubUrl = hubUrl;
	}

	/**
	 * @return the gridMode
	 */
	public boolean isGridMode() {
		return gridMode;
	}

	/**
	 * @param gridMode the gridMode to set
	 */
	public void setGridMode(boolean gridMode) {
		this.gridMode = gridMode;
	}

}
