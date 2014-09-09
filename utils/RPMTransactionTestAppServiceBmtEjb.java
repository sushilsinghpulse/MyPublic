package com.perf.utils;

import java.sql.*;
import java.text.*;
import java.util.Date;

import org.apache.commons.logging.*;

import com.retek.platform.exception.*;
import com.retek.platform.service.*;
import com.retek.rpm.domain.core.service.*;
import com.retek.rpm.domain.task.bo.*;
import com.retek.rpm.domain.task.service.*;

/**
 * @ejb:bean type="Stateless" jndi-name ="com/perf/utils/RPMTransactionTestAppServiceBmt"
 *           local-jndi-name="com/perf/utils/RPMTransactionTestAppServiceBmtLocal" view-type="both"
 *           transaction-type="Bean"
 */
public class RPMTransactionTestAppServiceBmtEjb extends RPMSessionBean implements
        RPMTransactionTestAppServiceBmt {

    private static final long serialVersionUID = -7851538011618572686L;

    private static final Log LOG = LogFactory.getLog(RPMTransactionTestAppServiceBmtEjb.class);
    /**
     * @ejb:create-method
     */
    public void ejbCreate()  {
    }
    /**
     * @ejb:interface-method
     */
    public void testCommit(ClientContext clientContext) throws RetekBusinessException {
        doInsert(clientContext, "RPMTransactionTestAppServiceBmtEjb.testSimpleCommit", true, null);
    }

    /**
     * @ejb:interface-method
     */
    public void testRollback(ClientContext clientContext) throws RetekBusinessException {
        doInsert(clientContext, "RPMTransactionTestAppServiceBmtEjb.testSimpleRollback", false,
                null);
    }

    /**
     * @ejb:interface-method
     */
    public void testCommitCallCmtCommit(ClientContext clientContext) throws RetekBusinessException {
        doInsert(clientContext, "RPMTransactionTestAppServiceBmtEjb.testCommitCallCmtCommit", true,
                new CallCmtCommit(clientContext));
    }

    /**
     * @ejb:interface-method
     */
    public void testCommitCallCmtRollback(ClientContext clientContext)
            throws RetekBusinessException {
        doInsert(clientContext, "RPMTransactionTestAppServiceBmtEjb.testCommitCallCmtRollback",
                true, new CallCmtRollback(clientContext));
    }

    /**
     * @ejb:interface-method
     */
    public void testRollbackCallCmtCommit(ClientContext clientContext)
            throws RetekBusinessException {
        doInsert(clientContext, "RPMTransactionTestAppServiceBmtEjb.testRollbackCallCmtCommit",
                false, new CallCmtCommit(clientContext));
    }

    /**
     * @ejb:interface-method
     */
    public void testRollbackCallCmtRollback(ClientContext clientContext)
            throws RetekBusinessException {
        doInsert(clientContext, "RPMTransactionTestAppServiceBmtEjb.testRollbackCallCmtRollback",
                false, new CallCmtRollback(clientContext));
    }

    /**
     * @ejb:interface-method
     */
    public void testAutoCommit(ClientContext clientContext) throws RetekBusinessException {
        RPMSessionBeanCommand command = new RPMSessionBeanCommand.RPMAutoCommitCommand(
                clientContext) {
            protected Object doExecute() throws RetekBusinessException {
                executeInsertStatement("RPMTransactionTestAppServiceBmtEjb.testAutoCommit");
                return null;
            }
        };

        command.execute();
    }

    /**
     * @ejb:interface-method
     */
    public void testNonXARollbackWithinXA(ClientContext clientContext)
            throws RetekBusinessException {
        final RPMSessionBeanCommand nonXACommand = new RPMSessionBeanCommand.RPMNonXACommand(
                clientContext) {
            protected Object doExecute() throws RetekBusinessException {
                executeInsertStatement("RPMTransactionTestAppServiceBmtEjb.testNonXARollbackWithinXA (non-xa)");
                rollbackTransaction();
                return null;
            }
        };

        RPMSessionBeanCommand xaCommand = new RPMSessionBeanCommand.RPMBmtXACommand(clientContext) {
            protected Object doExecute() throws RetekBusinessException {
                executeInsertStatement("RPMTransactionTestAppServiceBmtEjb.testNonXARollbackWithinXA (xa)");

                nonXACommand.execute();
                return null;
            }
        };

        xaCommand.execute();
    }

    /**
     * @ejb:interface-method
     */
    public void testXARollbackWithinNonXA(ClientContext clientContext)
            throws RetekBusinessException {
        final RPMSessionBeanCommand xaCommand = new RPMSessionBeanCommand.RPMBmtXACommand(
                clientContext) {
            protected Object doExecute() throws RetekBusinessException {
                executeInsertStatement("RPMTransactionTestAppServiceBmtEjb.testXARollbackWithinNonXA (xa)");
                rollbackTransaction();
                return null;
            }
        };

        RPMSessionBeanCommand nonXACommand = new RPMSessionBeanCommand.RPMNonXACommand(
                clientContext) {
            protected Object doExecute() throws RetekBusinessException {
                executeInsertStatement("RPMTransactionTestAppServiceBmtEjb.testXARollbackWithinNonXA (non-xa)");

                xaCommand.execute();

                return null;
            }
        };
        nonXACommand.execute();
    }

    /**
     * @ejb:interface-method
     */
    public void testNonXAWithinXARollback(ClientContext clientContext)
            throws RetekBusinessException {
        final RPMSessionBeanCommand nonXACommand = new RPMSessionBeanCommand.RPMNonXACommand(
                clientContext) {
            protected Object doExecute() throws RetekBusinessException {
                executeInsertStatement("RPMTransactionTestAppServiceBmtEjb.testNonXAWithinXARollback (non-xa)");
                return null;
            }
        };

        RPMSessionBeanCommand xaCommand = new RPMSessionBeanCommand.RPMBmtXACommand(clientContext) {
            protected Object doExecute() throws RetekBusinessException {
                executeInsertStatement("RPMTransactionTestAppServiceBmtEjb.testNonXAWithinXARollback (xa)");

                nonXACommand.execute();

                rollbackTransaction();
                return null;
            }
        };

        xaCommand.execute();
    }

    /**
     * @ejb:interface-method
     */
    public void testXAWithinNonXARollback(ClientContext clientContext)
            throws RetekBusinessException {
        final RPMSessionBeanCommand xaCommand = new RPMSessionBeanCommand.RPMBmtXACommand(
                clientContext) {
            protected Object doExecute() throws RetekBusinessException {
                executeInsertStatement("RPMTransactionTestAppServiceBmtEjb.testXAWithinNonXARollback (xa)");
                return null;
            }
        };

        RPMSessionBeanCommand nonXACommand = new RPMSessionBeanCommand.RPMNonXACommand(
                clientContext) {
            protected Object doExecute() throws RetekBusinessException {
                executeInsertStatement("RPMTransactionTestAppServiceBmtEjb.testXAWithinNonXARollback (non-xa)");

                xaCommand.execute();

                rollbackTransaction();
                return null;
            }
        };
        nonXACommand.execute();
    }

    /**
     * @ejb:interface-method
     */
    public void testTaskEngineXACommit(ClientContext clientContext) throws RetekBusinessException {
        RPMSessionBeanCommand command = new RPMSessionBeanCommand.RPMBmtXACommand(clientContext) {
            protected Object doExecute() throws RetekBusinessException {

                String description = "RPMTransactionTestAppServiceBmtEjb.testTaskEngineXACommit";
                executeInsertStatement(description);
                RPMTask task = RPMTransactionTestAppServiceImpl.createTestTask(description);
                publishTask(task);

                return null;
            }
        };

        command.execute();
    }

    /**
     * @ejb:interface-method
     */
    public void testTaskEngineXARollback(ClientContext clientContext) throws RetekBusinessException {
        RPMSessionBeanCommand command = new RPMSessionBeanCommand.RPMBmtXACommand(clientContext) {
            protected Object doExecute() throws RetekBusinessException {

                String description = "RPMTransactionTestAppServiceBmtEjb.testTaskEngineXARollback";
                executeInsertStatement(description);
                RPMTask task = RPMTransactionTestAppServiceImpl.createTestTask(description);
                publishTask(task);

                rollbackTransaction();

                return null;
            }
        };

        command.execute();
    }

    /**
     * @ejb:interface-method
     */
    public void testTaskEngineNonXACommit(ClientContext clientContext)
            throws RetekBusinessException {
        RPMSessionBeanCommand command = new RPMSessionBeanCommand.RPMNonXACommand(clientContext) {
            protected Object doExecute() throws RetekBusinessException {

                String description = "RPMTransactionTestAppServiceBmtEjb.testTaskEngineNonXACommit";
                executeInsertStatement(description);
                RPMTask task = RPMTransactionTestAppServiceImpl.createTestTask(description);
                publishTask(task);

                return null;
            }
        };

        command.execute();
    }

    /**
     * @ejb:interface-method
     */
    public void testTaskEngineNonXARollback(ClientContext clientContext)
            throws RetekBusinessException {
        RPMSessionBeanCommand command = new RPMSessionBeanCommand.RPMNonXACommand(clientContext) {
            protected Object doExecute() throws RetekBusinessException {

                String description = "RPMTransactionTestAppServiceBmtEjb.testTaskEngineNonXARollback";
                executeInsertStatement(description);
                RPMTask task = RPMTransactionTestAppServiceImpl.createTestTask(description);
                publishTask(task);

                rollbackTransaction();

                return null;
            }
        };

        command.execute();
    }

    private String getDateTimeString() {
        return DateFormat.getDateTimeInstance().format(new Date());
    }

    private void executeInsertStatement(String suffix) {
        Connection connection = RPMServerContext.getCurrentServerContext().getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("insert into tinetti (value) values (?)");
            ps.setString(1, getDateTimeString() + " " + suffix);
            ps.execute();
        } catch (SQLException e) {
            throw new RetekUnknownSystemException(e);
        } finally {
            close(ps);
        }
    }

    private void doInsert(ClientContext clientContext, final String suffix, final boolean commit,
            final Runnable extRunnable) throws RetekBusinessException {
        RPMSessionBeanCommand command = new RPMSessionBeanCommand.RPMBmtXACommand(clientContext) {
            public Object doExecute() throws RetekBusinessException {

                executeInsertStatement(suffix);

                if (extRunnable != null) {
                    extRunnable.run();
                }

                if (!commit) {
                    try {
                        RPMServerContext.getCurrentServerContext().rollbackTransaction();
                    } catch (Exception e) {
                        // not much we can do about this
                        LOG.error(e, e);
                    }
                }

                return null;
            }
        };

        command.execute();
    }

    protected void close(Object closable) {
        if (closable != null) {
            try {
                closable.getClass().getMethod("close", null).invoke(closable, null);
            } catch (Throwable t) {
                LOG.warn(t, t);
            }
        }
    }

    private static RPMTransactionTestAppService getTransactionTestAppService() {
        return (RPMTransactionTestAppService) RPMServiceFactoryImpl.getInstance().getService(
                RPMTransactionTestAppService.class);
    }

    private void publishTask(RPMTask task) {
        RPMTaskCoreService taskCoreService = (RPMTaskCoreService) RPMServiceFactoryImpl
                .getInstance().getService(RPMTaskCoreService.class);
        taskCoreService.publishTask(task, false);
    }

    private static class CallCmtCommit implements Runnable {
        private ClientContext clientContext;

        public CallCmtCommit(ClientContext clientContext) {
            this.clientContext = clientContext;
        }

        public void run() {
            try {
                getTransactionTestAppService().testCommit(clientContext);
            } catch (RetekBusinessException e) {
                throw new RetekUnknownSystemException(e);
            }
        }
    }

    private static class CallCmtRollback implements Runnable {
        private ClientContext clientContext;

        public CallCmtRollback(ClientContext clientContext) {
            this.clientContext = clientContext;
        }

        public void run() {
            try {
                getTransactionTestAppService().testRollback(clientContext);
            } catch (RetekBusinessException e) {
                throw new RetekUnknownSystemException(e);
            }
        }
    }

}
