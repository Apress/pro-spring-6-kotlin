/*
 * This file is generated by jOOQ.
 */
package com.apress.prospring6.seven.jooq.generated.tables.pojos;


import java.io.Serializable;
import java.time.LocalDate;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer age;
    private String licensePlate;
    private LocalDate manufactureDate;
    private String manufacturer;
    private Integer version;

    public Car() {}

    public Car(Car value) {
        this.id = value.id;
        this.age = value.age;
        this.licensePlate = value.licensePlate;
        this.manufactureDate = value.manufactureDate;
        this.manufacturer = value.manufacturer;
        this.version = value.version;
    }

    public Car(
        Long id,
        Integer age,
        String licensePlate,
        LocalDate manufactureDate,
        String manufacturer,
        Integer version
    ) {
        this.id = id;
        this.age = age;
        this.licensePlate = licensePlate;
        this.manufactureDate = manufactureDate;
        this.manufacturer = manufacturer;
        this.version = version;
    }

    /**
     * Getter for <code>musicdb.CAR.ID</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>musicdb.CAR.ID</code>.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>musicdb.CAR.AGE</code>.
     */
    public Integer getAge() {
        return this.age;
    }

    /**
     * Setter for <code>musicdb.CAR.AGE</code>.
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * Getter for <code>musicdb.CAR.LICENSE_PLATE</code>.
     */
    public String getLicensePlate() {
        return this.licensePlate;
    }

    /**
     * Setter for <code>musicdb.CAR.LICENSE_PLATE</code>.
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * Getter for <code>musicdb.CAR.MANUFACTURE_DATE</code>.
     */
    public LocalDate getManufactureDate() {
        return this.manufactureDate;
    }

    /**
     * Setter for <code>musicdb.CAR.MANUFACTURE_DATE</code>.
     */
    public void setManufactureDate(LocalDate manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    /**
     * Getter for <code>musicdb.CAR.MANUFACTURER</code>.
     */
    public String getManufacturer() {
        return this.manufacturer;
    }

    /**
     * Setter for <code>musicdb.CAR.MANUFACTURER</code>.
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Getter for <code>musicdb.CAR.version</code>.
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * Setter for <code>musicdb.CAR.version</code>.
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Car (");

        sb.append(id);
        sb.append(", ").append(age);
        sb.append(", ").append(licensePlate);
        sb.append(", ").append(manufactureDate);
        sb.append(", ").append(manufacturer);
        sb.append(", ").append(version);

        sb.append(")");
        return sb.toString();
    }
}
