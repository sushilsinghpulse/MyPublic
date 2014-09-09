package com.perf.utils;

import com.retek.platform.exception.*;
import com.retek.platform.service.*;


public interface WorksheetDetailTestService extends Service {
    
    public void testLoadHeavyWeightWorksheetDetails(ClientContext context) throws RetekBusinessException;
    public void testLoadLightWeightWorksheetDetails(ClientContext context) throws RetekBusinessException;
    public void testLoad50000LightWeightWorksheetDetails(ClientContext context) throws RetekBusinessException;
}
