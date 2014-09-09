package com.perf.utils;

import java.sql.*;
import java.util.*;

import javax.ejb.*;
import javax.naming.*;
import javax.sql.*;

import net.sf.ehcache.*;
import net.sf.hibernate.*;

import com.retek.platform.bo.*;
import com.retek.rpm.app.core.service.*;
import com.retek.platform.service.*;
import com.retek.rpm.domain.admin.bo.*;
import com.retek.rpm.domain.core.persistence.*;
import com.retek.rpm.domain.core.service.*;

/**
 * @ejb:bean type="Stateless" jndi-name="com/perf/utils/HibernateEHCacheMonitor"
 *           local-jndi-name="com/perf/utils/HibernateEHCacheMonitorLocal" view-type="both"
 * 
 * @ejb:transaction type="NotSupported"
 */
public class HibernateEHCacheMonitorEjb extends RPMSessionBean implements
        HibernateEHCacheMonitorService {
    

    private static final long serialVersionUID = 4300832382427232988L;

    /** @ejb:create-method * */
    public void ejbCreate() throws CreateException {
    }

    /**
     * @ejb:interface-method
     */
    public String getCacheType(String key) {
        return HibernateEHCacheMonitor.getInstance(key).getCacheType();
    }

    /**
     * @ejb:interface-method
     */
    public List getCachingInformation(String key) {
        return HibernateEHCacheMonitor.getInstance(key).getCachingInformation();
    }

    /**
     * @throws CacheException
     * @ejb:interface-method
     */
    public int getCacheHits(String key, String cacheName) throws CacheException {
        return HibernateEHCacheMonitor.getInstance(key).getHitCount(cacheName);
    }

    /**
     * @ejb:interface-method
     */
    public String getServerName(String key) {
        return HibernateEHCacheMonitor.getInstance(key).getServerName();
    }

    /**
     * @ejb:interface-method
     */
    public String getServerTime(String key) {
        return HibernateEHCacheMonitor.getInstance(key).getServerTime();
    }

    /**
     * @ejb:interface-method
     */
    public String toString(String key) {
        return HibernateEHCacheMonitor.getInstance(key).toString();
    }

    /**
     * @ejb:interface-method
     */
    public void readForTestingPurposes(IdentifiableReference ref) {
        PersistenceManagerFactory.getInstance().getPersistenceManager().readWithoutVersionControl(
                ref);
    }
    
    /**
     * @ejb:interface-method
     */
    public void modifySystemOptions() {
        ServiceContextDispenser.createServiceContext();
        
        try  {
	        PersistenceManager persistenceManager = PersistenceManagerFactory.getInstance().getPersistenceManager();
	        List results = (List) persistenceManager.findFirst(SystemOptions.class);
	        SystemOptions systemOptions = (SystemOptions) results.iterator().next();
	        
	        if (Boolean.FALSE.equals(systemOptions.isClearancePromotionsOverlap()))  {
	            systemOptions.setClearancePromotionsOverlap(Boolean.TRUE);
	        } else {
	            systemOptions.setClearancePromotionsOverlap(Boolean.FALSE);
	        }
	        
	        persistenceManager.save(systemOptions);
	        persistenceManager.flush();
            
        } catch (Throwable t)  {
            System.err.println("ON SERVER ERROR: " + t);
        } finally  {
            ServiceContextDispenser.completeCurrent();
        }
    }
    
    /**
     * @ejb:interface-method
     * @ejb:transaction type="Never"
     */
    public void modifySystemOptionsWithResources(String transactionId) throws Exception {
        RPMServerContext context = RPMServerContext.start(RPMTransactionControllerFactory
                .getInstance().createNonXAController());
        
        Stack resources = (Stack) xaResources.get(transactionId);
        Connection connection = (Connection) resources.pop();
        
        Session session = context.getHibernateSession();
        session.disconnect();
        session.reconnect(connection);
        
        try  {
	        PersistenceManager persistenceManager = PersistenceManagerFactory.getInstance().getPersistenceManager();
	        List results = (List) persistenceManager.findFirst(SystemOptions.class);
	        SystemOptions systemOptions = (SystemOptions) results.iterator().next();
	        
	        if (Boolean.FALSE.equals(systemOptions.isClearancePromotionsOverlap()))  {
	            systemOptions.setClearancePromotionsOverlap(Boolean.TRUE);
	        } else {
	            systemOptions.setClearancePromotionsOverlap(Boolean.FALSE);
	        }
	        
	        persistenceManager.save(systemOptions);
	        persistenceManager.flush();

		} catch (Throwable t)  {
            System.err.println("ON SERVER ERROR: " + t);
        } finally  {
            ServiceContextDispenser.completeCurrent();
        }
    }
    
    /**
     * @throws Exception
     * @ejb:interface-method
     */
    public void enlistResources(String transactionId, int numResources) throws Exception  {
        String jndiName = "java:/jdbc/RPMDataSource";
        DataSource ds = null;
        
        try {
            Hashtable namingEnv = new Hashtable();
            Context context = null;
            
            context = new InitialContext(namingEnv);
			ds = (DataSource) context.lookup(jndiName);
		}
		catch (Exception e) {
			System.err.println( "Could not find datasource: " + jndiName);
			throw e;
		}
		
		// Grabbing it out of the pool enlists it into this transaction
		for (int i = 0; i < numResources; i++) {
		    Connection xaConnection = ds.getConnection();
		    
		    Stack resources = (Stack) xaResources.get(transactionId);
		    if (resources == null)  {
		        resources = new Stack();
		        xaResources.put(transactionId, resources);
		    }
		    
		    resources.push(xaConnection);
        }
    }
    
    private static Map xaResources = new HashMap();

}
