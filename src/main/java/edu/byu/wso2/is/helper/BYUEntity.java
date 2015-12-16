package edu.byu.wso2.is.helper;

public class BYUEntity {
    String netId = null;
    String personId = null;
    String byuId = null;
    String surname = null;
    String restOfName = null;
    String surnamePosition = null;
    String prefix = null;
    String suffix = null;
    String sortName = null;
    String preferredFirstName = null;


    public BYUEntity() {
    }

    public BYUEntity(String netId, String personId, String byuId, String surname, String restOfName, String surnamePosition,
                     String prefix, String suffix, String sortName, String preferredFirstName) {
        this.netId = netId;
        this.personId = personId;
        this.byuId = byuId;
        this.surname = surname;
        this.restOfName = restOfName;
        this.surnamePosition = surnamePosition;
        this.prefix = prefix;
        this.suffix = suffix;
        this.sortName = sortName;
        this.preferredFirstName = preferredFirstName;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getByuId() {
        return byuId;
    }

    public void setByuId(String byuId) {
        this.byuId = byuId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRestOfName() {
        return restOfName;
    }

    public void setRestOfName(String restOfName) {
        this.restOfName = restOfName;
    }

    public String getSurnamePosition() {
        return surnamePosition;
    }

    public void setSurnamePosition(String surnamePosition) {
        this.surnamePosition = surnamePosition;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getPreferredFirstName() {
        return preferredFirstName;
    }

    public void setPreferredFirstName(String preferredFirstName) {
        this.preferredFirstName = preferredFirstName;
    }
}
