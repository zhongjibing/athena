package com.icezhg.athena.vo;

/**
 * Created by zhongjibing on 2022/09/17.
 */
public class FuzzyQuery implements Query {
    private boolean isFuzzyQuery = true;

    public void setFuzzyQuery(boolean fuzzyQuery) {
        isFuzzyQuery = fuzzyQuery;
    }

    @Override
    public boolean isFuzzyQuery() {
        return isFuzzyQuery;
    }
}
