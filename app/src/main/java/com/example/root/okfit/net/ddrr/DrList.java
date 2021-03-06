package com.example.root.okfit.net.ddrr;

import com.fei.crnetwork.dataformat.AList;
import com.fei.crnetwork.dataformat.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by PengFeifei on 17-8-1.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DrList<T extends Entity> implements Entity {
    private static final long serialVersionUID = 1L;
    private AList<T> list;
    private long totalRecords;

    public long getTotalRecords() {
        return totalRecords;
    }

    public AList<T> getList() {
        return list;
    }
}