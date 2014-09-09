package com.perf.utils;

import com.retek.platform.exception.*;
import com.retek.platform.service.*;

public interface UserTransactionTestService extends Service {
    
    public void testUserTransaction(ClientContext context) throws RetekBusinessException;
    public void testSimulateNoTransaction(ClientContext context) throws RetekBusinessException;
    public Boolean isTraceOn(ClientContext context);
    public Boolean setTraceState(ClientContext context, Boolean setTraceOn) throws Exception;

}
