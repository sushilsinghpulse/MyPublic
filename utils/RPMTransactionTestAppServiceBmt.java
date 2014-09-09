package com.perf.utils;

import com.retek.platform.exception.*;
import com.retek.platform.service.*;

public interface RPMTransactionTestAppServiceBmt extends Service {
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
    void testCommitCallCmtCommit(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row / call bmt to insert a row and roll back (0 new rows)
     * 
     * IMPORTANT: calling a cmt which rolls back WILL roll back the bmt
     */
    void testCommitCallCmtRollback(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row and roll back / call bmt to insert a row (0 new rows)
     * 
     * IMPORTANT: rolling back the bmt WILL roll back any cmt's called from it
     */
    void testRollbackCallCmtCommit(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row and roll back / call bmt to insert a row and roll back (0 new rows)
     */
    void testRollbackCallCmtRollback(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row using auto-commit (1 new row)
     */
    void testAutoCommit(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row using non-xa and roll back / insert a row using xa (1 new row)
     */
    void testNonXARollbackWithinXA(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row using xa and roll back / insert a row using non-xa (1 new row)
     */
    void testXARollbackWithinNonXA(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row using non-xa / insert a row using xa and roll back (1 new row)
     */
    void testNonXAWithinXARollback(ClientContext clientContext) throws RetekBusinessException;

    /**
     * insert a row using xa / insert a row using non-xa and roll back (1 new row)
     */
    void testXAWithinNonXARollback(ClientContext clientContext) throws RetekBusinessException;

    /**
     * publish a message to the jms task queue
     */
    void testTaskEngineXACommit(ClientContext clientContext) throws RetekBusinessException;

    /**
     * publish a message to the jms task queue and roll back
     */
    void testTaskEngineXARollback(ClientContext clientContext) throws RetekBusinessException;

    /**
     * publish a message to the jms task queue
     */
    void testTaskEngineNonXACommit(ClientContext clientContext) throws RetekBusinessException;

    /**
     * publish a message to the jms task queue and roll back
     */
    void testTaskEngineNonXARollback(ClientContext clientContext) throws RetekBusinessException;

}
