package com.perf.utils;

import com.retek.platform.exception.*;
import com.retek.platform.service.*;

public interface RPMTransactionTestAppService extends Service {

    /**
     * insert a row (1 new row)
     */
    void testCommit(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row and roll back (0 new rows)
     */
    void testRollback(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row / call bmt to insert a row (2 new rows)
     */
    void testCommitCallBmtCommit(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row / call bmt to insert a row and roll back (1 new row)
     * 
     * IMPORTANT: calling a bmt which rolls back WILL NOT roll back the cmt
     */
    void testCommitCallBmtRollback(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row and roll back / call bmt to insert a row (1 new row)
     * 
     * IMPORTANT: rolling back the cmt WILL NOT roll back any bmt's called from it
     */
    void testRollbackCallBmtCommit(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row and roll back / call bmt to insert a row and roll back (0 new rows)
     */
    void testRollbackCallBmtRollback(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row using auto-commit (1 new row)
     */
    void testAutoCommit(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row using non-xa and roll back / insert a row using xa (1 new row)
     */
    void testNonXARollbackWithinXA(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row using non-xa / insert a row using xa and roll back (1 new row)
     */
    void testNonXAWithinXARollback(ClientContext clientContext) throws RetekBusinessException;

    /**
     * publish a message to the jms task queue
     */
    void testTaskEngineCommit(ClientContext clientContext) throws RetekBusinessException;

    /**
     * publish a message to the jms task queue and roll back
     */
    void testTaskEngineRollback(ClientContext clientContext) throws RetekBusinessException;

    /**
     * get the number of rows in the test table
     */
    Integer getTestTableRowCount() throws RetekBusinessException;

    /**
     * delete all rows from test table
     */
    void truncateTestTable() throws RetekBusinessException;

    /**
     * create the test table
     */
    void createTestTable() throws RetekBusinessException;

}
