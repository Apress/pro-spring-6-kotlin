/*
 * This file is generated by jOOQ.
 */
package com.apress.prospring6.seven.jooq.generated.tables.daos;


import com.apress.prospring6.seven.jooq.generated.tables.SingerInstrument;
import com.apress.prospring6.seven.jooq.generated.tables.records.SingerInstrumentRecord;

import java.util.List;

import org.jooq.Configuration;
import org.jooq.Record2;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SingerInstrumentDao extends DAOImpl<SingerInstrumentRecord, com.apress.prospring6.seven.jooq.generated.tables.pojos.SingerInstrument, Record2<Integer, String>> {

    /**
     * Create a new SingerInstrumentDao without any configuration
     */
    public SingerInstrumentDao() {
        super(SingerInstrument.SINGER_INSTRUMENT, com.apress.prospring6.seven.jooq.generated.tables.pojos.SingerInstrument.class);
    }

    /**
     * Create a new SingerInstrumentDao with an attached configuration
     */
    public SingerInstrumentDao(Configuration configuration) {
        super(SingerInstrument.SINGER_INSTRUMENT, com.apress.prospring6.seven.jooq.generated.tables.pojos.SingerInstrument.class, configuration);
    }

    @Override
    public Record2<Integer, String> getId(com.apress.prospring6.seven.jooq.generated.tables.pojos.SingerInstrument object) {
        return compositeKeyRecord(object.getSingerId(), object.getInstrumentId());
    }

    /**
     * Fetch records that have <code>SINGER_ID BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.apress.prospring6.seven.jooq.generated.tables.pojos.SingerInstrument> fetchRangeOfSingerId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(SingerInstrument.SINGER_INSTRUMENT.SINGER_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>SINGER_ID IN (values)</code>
     */
    public List<com.apress.prospring6.seven.jooq.generated.tables.pojos.SingerInstrument> fetchBySingerId(Integer... values) {
        return fetch(SingerInstrument.SINGER_INSTRUMENT.SINGER_ID, values);
    }

    /**
     * Fetch records that have <code>INSTRUMENT_ID BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.apress.prospring6.seven.jooq.generated.tables.pojos.SingerInstrument> fetchRangeOfInstrumentId(String lowerInclusive, String upperInclusive) {
        return fetchRange(SingerInstrument.SINGER_INSTRUMENT.INSTRUMENT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>INSTRUMENT_ID IN (values)</code>
     */
    public List<com.apress.prospring6.seven.jooq.generated.tables.pojos.SingerInstrument> fetchByInstrumentId(String... values) {
        return fetch(SingerInstrument.SINGER_INSTRUMENT.INSTRUMENT_ID, values);
    }
}
