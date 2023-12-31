/*
 * This file is generated by jOOQ.
 */
package com.apress.prospring6.seven.jooq.generated;


import com.apress.prospring6.seven.jooq.generated.tables.Album;
import com.apress.prospring6.seven.jooq.generated.tables.Car;
import com.apress.prospring6.seven.jooq.generated.tables.Instrument;
import com.apress.prospring6.seven.jooq.generated.tables.Singer;
import com.apress.prospring6.seven.jooq.generated.tables.SingerInstrument;
import com.apress.prospring6.seven.jooq.generated.tables.records.AlbumRecord;
import com.apress.prospring6.seven.jooq.generated.tables.records.CarRecord;
import com.apress.prospring6.seven.jooq.generated.tables.records.InstrumentRecord;
import com.apress.prospring6.seven.jooq.generated.tables.records.SingerInstrumentRecord;
import com.apress.prospring6.seven.jooq.generated.tables.records.SingerRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * musicdb.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AlbumRecord> KEY_ALBUM_PRIMARY = Internal.createUniqueKey(Album.ALBUM, DSL.name("KEY_ALBUM_PRIMARY"), new TableField[] { Album.ALBUM.ID }, true);
    public static final UniqueKey<AlbumRecord> KEY_ALBUM_SINGER_ID = Internal.createUniqueKey(Album.ALBUM, DSL.name("KEY_ALBUM_SINGER_ID"), new TableField[] { Album.ALBUM.SINGER_ID, Album.ALBUM.TITLE }, true);
    public static final UniqueKey<CarRecord> KEY_CAR_PRIMARY = Internal.createUniqueKey(Car.CAR, DSL.name("KEY_CAR_PRIMARY"), new TableField[] { Car.CAR.ID }, true);
    public static final UniqueKey<InstrumentRecord> KEY_INSTRUMENT_PRIMARY = Internal.createUniqueKey(Instrument.INSTRUMENT, DSL.name("KEY_INSTRUMENT_PRIMARY"), new TableField[] { Instrument.INSTRUMENT.INSTRUMENT_ID }, true);
    public static final UniqueKey<SingerRecord> KEY_SINGER_FIRST_NAME = Internal.createUniqueKey(Singer.SINGER, DSL.name("KEY_SINGER_FIRST_NAME"), new TableField[] { Singer.SINGER.FIRST_NAME, Singer.SINGER.LAST_NAME }, true);
    public static final UniqueKey<SingerRecord> KEY_SINGER_PRIMARY = Internal.createUniqueKey(Singer.SINGER, DSL.name("KEY_SINGER_PRIMARY"), new TableField[] { Singer.SINGER.ID }, true);
    public static final UniqueKey<SingerInstrumentRecord> KEY_SINGER_INSTRUMENT_PRIMARY = Internal.createUniqueKey(SingerInstrument.SINGER_INSTRUMENT, DSL.name("KEY_SINGER_INSTRUMENT_PRIMARY"), new TableField[] { SingerInstrument.SINGER_INSTRUMENT.SINGER_ID, SingerInstrument.SINGER_INSTRUMENT.INSTRUMENT_ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<AlbumRecord, SingerRecord> FK_ALBUM = Internal.createForeignKey(Album.ALBUM, DSL.name("FK_ALBUM"), new TableField[] { Album.ALBUM.SINGER_ID }, Keys.KEY_SINGER_PRIMARY, new TableField[] { Singer.SINGER.ID }, true);
    public static final ForeignKey<SingerInstrumentRecord, SingerRecord> FK_SINGER_INSTRUMENT_1 = Internal.createForeignKey(SingerInstrument.SINGER_INSTRUMENT, DSL.name("FK_SINGER_INSTRUMENT_1"), new TableField[] { SingerInstrument.SINGER_INSTRUMENT.SINGER_ID }, Keys.KEY_SINGER_PRIMARY, new TableField[] { Singer.SINGER.ID }, true);
    public static final ForeignKey<SingerInstrumentRecord, InstrumentRecord> FK_SINGER_INSTRUMENT_2 = Internal.createForeignKey(SingerInstrument.SINGER_INSTRUMENT, DSL.name("FK_SINGER_INSTRUMENT_2"), new TableField[] { SingerInstrument.SINGER_INSTRUMENT.INSTRUMENT_ID }, Keys.KEY_INSTRUMENT_PRIMARY, new TableField[] { Instrument.INSTRUMENT.INSTRUMENT_ID }, true);
}
