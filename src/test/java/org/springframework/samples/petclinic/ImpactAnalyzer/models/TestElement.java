package org.springframework.samples.petclinic.ImpactAnalyzer.models;

public class TestElement {
    private String testClassName;
    private String testName;
    private String url;
    private String driverName;
    private Position startingPosition;
    private String accessMethod;
    private String accessMethodValue;
    private String actionMethod;
    private String actionMethodValue;

    public TestElement() {
    }

    public TestElement(String testClassName, String testName, String url, String driverName, Position startingPosition,
            String accessMethod, String accessMethodValue) {
        this.testClassName = testClassName;
        this.testName = testName;
        this.url = url;
        this.driverName = driverName;
        this.startingPosition = startingPosition;
        this.accessMethod = accessMethod;
        this.accessMethodValue = accessMethodValue;
    }

    public String getTestClassName() {
        return testClassName;
    }

    public void setTestClassName(String className) {
        this.testClassName = className;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String Url) {
        this.url = Url;
    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(Position startingPosition) {
        this.startingPosition = startingPosition;
    }

    public String getAccessMethod() {
        return accessMethod;
    }

    public void setAccessMethod(String accessMethod) {
        this.accessMethod = accessMethod;
    }

    public String getAccessMethodValue() {
        return accessMethodValue;
    }

    public void setAccessMethodValue(String accessMethodValue) {
        this.accessMethodValue = accessMethodValue;
    }

    public String getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }

    public String getActionMethodValue() {
        return actionMethodValue;
    }

    public void setActionMethodValue(String actionMethodValue) {
        this.actionMethodValue = actionMethodValue;
    }

}
