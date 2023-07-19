/*
 * This file is generated by jOOQ.
 */
package com.apress.prospring6.seven.jooq.generated.tables.records;


import com.apress.prospring6.seven.jooq.generated.tables.Car;

import java.time.LocalDate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CarRecord extends UpdatableRecordImpl<CarRecord> implements Record6<Long, Integer, String, LocalDate, String, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>musicdb.CAR.ID</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>musicdb.CAR.ID</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>musicdb.CAR.AGE</code>.
     */
    public void setAge(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>musicdb.CAR.AGE</code>.
     */
    public Integer getAge() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>musicdb.CAR.LICENSE_PLATE</code>.
     */
    public void setLicensePlate(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>musicdb.CAR.LICENSE_PLATE</code>.
     */
    public String getLicensePlate() {
        return (String) get(2);
    }

    /**
     * Setter for <code>musicdb.CAR.MANUFACTURE_DATE</code>.
     */
    public void setManufactureDate(LocalDate value) {
        set(3, value);
    }

    /**
     * Getter for <code>musicdb.CAR.MANUFACTURE_DATE</code>.
     */
    public LocalDate getManufactureDate() {
        return (LocalDate) get(3);
    }

    /**
     * Setter for <code>musicdb.CAR.MANUFACTURER</code>.
     */
    public void setManufacturer(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>musicdb.CAR.MANUFACTURER</code>.
     */
    public String getManufacturer() {
        return (String) get(4);
    }

    /**
     * Setter for <code>musicdb.CAR.version</code>.
     */
    public void setVersion(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>musicdb.CAR.version</code>.
     */
    public Integer getVersion() {
        return (Integer) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, Integer, String, LocalDate, String, Integer> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<Long, Integer, String, LocalDate, String, Integer> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Car.CAR.ID;
    }

    @Override
    public Field<Integer> field2() {
        return Car.CAR.AGE;
    }

    @Override
    public Field<String> field3() {
        return Car.CAR.LICENSE_PLATE;
    }

    @Override
    public Field<LocalDate> field4() {
        return Car.CAR.MANUFACTURE_DATE;
    }

    @Override
    public Field<String> field5() {
        return Car.CAR.MANUFACTURER;
    }

    @Override
    public Field<Integer> field6() {
        return Car.CAR.VERSION;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Integer component2() {
        return getAge();
    }

    @Override
    public String component3() {
        return getLicensePlate();
    }

    @Override
    public LocalDate component4() {
        return getManufactureDate();
    }

    @Override
    public String component5() {
        return getManufacturer();
    }

    @Override
    public Integer component6() {
        return getVersion();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Integer value2() {
        return getAge();
    }

    @Override
    public String value3() {
        return getLicensePlate();
    }

    @Override
    public LocalDate value4() {
        return getManufactureDate();
    }

    @Override
    public String value5() {
        return getManufacturer();
    }

    @Override
    public Integer value6() {
        return getVersion();
    }

    @Override
    public CarRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public CarRecord value2(Integer value) {
        setAge(value);
        return this;
    }

    @Override
    public CarRecord value3(String value) {
        setLicensePlate(value);
        return this;
    }

    @Override
    public CarRecord value4(LocalDate value) {
        setManufactureDate(value);
        return this;
    }

    @Override
    public CarRecord value5(String value) {
        setManufacturer(value);
        return this;
    }

    @Override
    public CarRecord value6(Integer value) {
        setVersion(value);
        return this;
    }

    @Override
    public CarRecord values(Long value1, Integer value2, String value3, LocalDate value4, String value5, Integer value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CarRecord
     */
    public CarRecord() {
        super(Car.CAR);
    }

    /**
     * Create a detached, initialised CarRecord
     */
    public CarRecord(Long id, Integer age, String licensePlate, LocalDate manufactureDate, String manufacturer, Integer version) {
        super(Car.CAR);

        setId(id);
        setAge(age);
        setLicensePlate(licensePlate);
        setManufactureDate(manufactureDate);
        setManufacturer(manufacturer);
        setVersion(version);
    }

    /**
     * Create a detached, initialised CarRecord
     */
    public CarRecord(com.apress.prospring6.seven.jooq.generated.tables.pojos.Car value) {
        super(Car.CAR);

        if (value != null) {
            setId(value.getId());
            setAge(value.getAge());
            setLicensePlate(value.getLicensePlate());
            setManufactureDate(value.getManufactureDate());
            setManufacturer(value.getManufacturer());
            setVersion(value.getVersion());
        }
    }
}