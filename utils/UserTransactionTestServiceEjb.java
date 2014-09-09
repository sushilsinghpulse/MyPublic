package com.perf.utils;

import java.sql.*;

import javax.ejb.*;

import net.sf.hibernate.*;

import com.retek.platform.exception.*;
import com.retek.platform.service.*;
import com.retek.rpm.app.core.service.*;
import com.retek.rpm.domain.core.dao.*;
import com.retek.rpm.domain.core.dao.sql.*;
import com.retek.rpm.domain.core.persistence.*;
import com.retek.rpm.domain.core.service.*;
import com.retek.rpm.domain.core.service.RPMSessionBeanCommand.*;

/**
 * @ejb:bean type="Stateless" jndi-name
 *                ="com/perf/utils/UserTransactionTestService"
 *                local-jndi-name="com/perf/utils/UserTransactionTestServiceLocal"
 *                view-type="both" transaction-type="Bean"
 */
public class UserTransactionTestServiceEjb extends RPMSessionBean  implements UserTransactionTestService {

    private static final long serialVersionUID = -3141444509486587169L;
    private static boolean traceOn = false;
    
    /** @ejb:create-method * */
    public void ejbCreate() throws CreateException {
    }

    /**
     * @ejb:interface-method 
     */
    public void testUserTransaction(ClientContext context) throws RetekBusinessException {
        RPMSessionBeanCommand command = new RPMNonXACommand(context) {
            protected Object doExecute() throws RetekBusinessException {
                
                SqlStatement sqlStatement = new StaticSqlStatement("update rpm_system_options set sim_ind = 0");
                
		        SqlStatementExecuter.executeUpdate(sqlStatement);
		            
		        commitTransaction();
		        
                
                sqlStatement = new StaticSqlStatement("update rpm_system_options set sim_ind = 0");
                
                SqlStatementExecuter.executeUpdate(sqlStatement);
		            
		        commitTransaction();
		        
		        return null;
            }
            
            
        };
        
        command.execute();
    }
    
    /**
     * @ejb:interface-method 
     */
    public void testSimulateNoTransaction(ClientContext context) throws RetekBusinessException {
        RPMSessionBeanCommand command = new RPMNonXACommand(context) {
            protected Object doExecute() throws RetekBusinessException {
                
                SqlStatement sqlStatement = new StaticSqlStatement("update rpm_system_options set sim_ind = 0");
                
                SqlStatementExecuter.executeUpdate(sqlStatement);
		            
		        commitTransaction();
		        
		        return null;
            }
            
        };
        
        command.execute();
    }
    
    /**
     * @ejb:interface-method 
     */
    public Boolean isTraceOn(ClientContext context) {
        return new Boolean(traceOn);
    }
    
    /**
     * @throws HibernateException
     * @throws SQLException
     * @ejb:interface-method 
     */
    public Boolean setTraceState(ClientContext context, final Boolean setTraceOn) throws Exception {
        RPMSessionBeanCommand command = new RPMNonXACommand(context) {
            protected Object doExecute() throws RetekBusinessException {
                try  {
                    Connection connection = RPMServerContext.getCurrentServerContext().getConnection();
			        
			        if (setTraceOn.booleanValue())  {
			            startTrace(connection);
			            traceOn = true;
			        } else {
			            stopTrace(connection);
			            traceOn = false;
			        }
	        
			        return new Boolean(traceOn);
                } catch (Exception ex)  {
                    throw new IllegalStateException(ex.toString());
                }
            }
        };
        
        return (Boolean) command.execute();
    }
    
    private void startTrace(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        
        statement.execute("alter session set sql_trace = true");
        statement.close();
    }
    
    private void stopTrace(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        
        statement.execute("alter session set sql_trace = false");
        statement.close();
    }

}
