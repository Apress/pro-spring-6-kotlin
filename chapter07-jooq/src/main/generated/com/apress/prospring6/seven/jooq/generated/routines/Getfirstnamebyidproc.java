/*
 * This file is generated by jOOQ.
 */
package com.apress.prospring6.seven.jooq.generated.routines;


import com.apress.prospring6.seven.jooq.generated.Musicdb;

import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Getfirstnamebyidproc extends AbstractRoutine<java.lang.Void> {

    private static final long serialVersionUID = 1L;

    /**
     * The parameter <code>musicdb.getFirstNameByIdProc.in_id</code>.
     */
    public static final Parameter<Integer> IN_ID = Internal.createParameter("in_id", SQLDataType.INTEGER, false, false);

    /**
     * The parameter <code>musicdb.getFirstNameByIdProc.fn_res</code>.
     */
    public static final Parameter<String> FN_RES = Internal.createParameter("fn_res", SQLDataType.VARCHAR(60), false, false);

    /**
     * Create a new routine call instance
     */
    public Getfirstnamebyidproc() {
        super("getFirstNameByIdProc", Musicdb.MUSICDB);

        addInParameter(IN_ID);
        addOutParameter(FN_RES);
    }

    /**
     * Set the <code>in_id</code> parameter IN value to the routine
     */
    public void setInId(Integer value) {
        setValue(IN_ID, value);
    }

    /**
     * Get the <code>fn_res</code> parameter OUT value from the routine
     */
    public String getFnRes() {
        return get(FN_RES);
    }
}