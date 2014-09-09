package com.perf.utils;

import javax.ejb.*;

import com.retek.platform.bo.*;
import com.retek.platform.exception.*;
import com.retek.platform.service.*;
import com.retek.rpm.app.core.service.*;
import com.retek.rpm.app.worksheet.service.*;
import com.retek.rpm.domain.core.persistence.*;
import com.retek.rpm.domain.core.service.RPMSessionBean;
import com.retek.rpm.domain.worksheet.bo.*;

/**
 * @ejb:bean type="Stateless" jndi-name
 *                ="com/perf/utils/WorksheetDetailTestService"
 *                local-jndi-name="com/perf/utils/WorksheetDetailTestServiceLocal"
 *                view-type="both" transaction-type="Container"
 * @ejb:transaction type="NotSupported"
 */
public class WorksheetDetailTestServiceEjb extends RPMSessionBean implements
        WorksheetDetailTestService {

    private static final long serialVersionUID = 7033989774578142055L;
    /** @ejb:create-method * */
    public void ejbCreate() throws CreateException {
    }

    /**
     * @ejb:interface-method 
     */
    public void testLoadHeavyWeightWorksheetDetails(ClientContext context) throws RetekBusinessException {
        WorksheetDetailSearchCriteria worksheetDetailSearchCriteria = new WorksheetDetailSearchCriteria();
        
        // Load 5000 details
        IdentifiableReference worksheetStatusReference = new IdentifiableReference(WorksheetStatusImpl.class,
                new LongObjectId(new Long(4)));
        Long departmentId = new Long(1010);
        
        worksheetDetailSearchCriteria.setWorksheetStatusReference(worksheetStatusReference);
        worksheetDetailSearchCriteria.setDepartmentId(departmentId);
        
        PersistenceManagerFactory.getInstance().getPersistenceManager().find(worksheetDetailSearchCriteria);
    }
    
    /**
     * @ejb:interface-method 
     */
    public void testLoadLightWeightWorksheetDetails(ClientContext context) throws RetekBusinessException {
        WorksheetDetailLightWeightForRollupSearchCriteria worksheetDetailSearchCriteria = new WorksheetDetailLightWeightForRollupSearchCriteria();
        
        // Load 5000 details
        IdentifiableReference worksheetStatusReference = new IdentifiableReference(WorksheetStatusImpl.class,
                new LongObjectId(new Long(4)));
        Long departmentId = new Long(1010);
        
        worksheetDetailSearchCriteria.setWorksheetStatusReference(worksheetStatusReference);
        worksheetDetailSearchCriteria.setDepartmentId(departmentId);
        
        PersistenceManagerFactory.getInstance().getPersistenceManager().find(worksheetDetailSearchCriteria);
    }
    
    /**
     * @ejb:interface-method 
     */
    public void testLoad50000LightWeightWorksheetDetails(ClientContext context) throws RetekBusinessException {
        WorksheetDetailLightWeightForRollupSearchCriteria worksheetDetailSearchCriteria = new WorksheetDetailLightWeightForRollupSearchCriteria();
        
        // Load 50000 details
        IdentifiableReference worksheetStatusReference = new IdentifiableReference(WorksheetStatusImpl.class,
                new LongObjectId(new Long(6)));
        Long departmentId = new Long(1010);
        
        worksheetDetailSearchCriteria.setWorksheetStatusReference(worksheetStatusReference);
        worksheetDetailSearchCriteria.setDepartmentId(departmentId);
        
        PersistenceManagerFactory.getInstance().getPersistenceManager().find(worksheetDetailSearchCriteria);
    }

}
