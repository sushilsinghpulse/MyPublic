package com.perf.utils;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

import org.apache.commons.logging.*;

import com.retek.platform.exception.*;
import com.retek.platform.service.*;
import com.retek.rpm.app.core.service.*;
import com.retek.rpm.domain.core.service.*;
import com.retek.rpm.domain.task.bo.*;
import com.retek.rpm.domain.task.service.*;

public class RPMTransactionTestAppServiceImpl extends RPMAppService implements
        RPMTransactionTestAppService {

    private static final long serialVersionUID = 4071916976703868154L;

    private static final Log LOG = LogFactory.getLog(RPMTransactionTestAppServiceImpl.class);

    public void testCommit(ClientContext clientContext) throws RetekBusinessException {
        doInsert("RPMTransactionTestAppServiceImpl.testCommit");
    }

    public void testRollback(ClientContext clientContext) throws RetekBusinessException {
        doInsert("RPMTransactionTestAppServiceImpl.testRollback");
        rollbackCurrentTransaction();
    }

    public void testCommitCallBmtCommit(ClientContext clientContext) throws RetekBusinessException {
        doInsert("RPMTransactionTestAppServiceImpl.testCommitCallBmtCommit");
        callBmtCommit(clientContext);
    }

    public void testCommitCallBmtRollback(ClientContext clientContext)
            throws RetekBusinessException {
        doInsert("RPMTransactionTestAppServiceImpl.testCommitCallBmtRollback");
        callBmtRollback(clientContext);
    }

    public void testRollbackCallBmtCommit(ClientContext clientContext)
            throws RetekBusinessException {
        doInsert("RPMTransactionTestAppServiceImpl.testRollbackCallBmtCommit");
        callBmtCommit(clientContext);
        rollbackCurrentTransaction();
    }

    public void testRollbackCallBmtRollback(ClientContext clientContext)
            throws RetekBusinessException {
        doInsert("RPMTransactionTestAppServiceImpl.testRollbackCallBmtRollback");
        callBmtRollback(clientContext);
        rollbackCurrentTransaction();
    }

    public void testAutoCommit(ClientContext clientContext) throws RetekBusinessException {
        RPMSessionBeanCommand command = new RPMSessionBeanCommand.RPMAutoCommitCommand(
                clientContext) {
            protected Object doExecute() throws RetekBusinessException {
                doInsert("RPMTransactionTestAppServiceBmtEjb.testSimpleLocalTransaction");
                return null;
            }
        };

        command.execute();

        // rollback this transaction to make sure the auto-commit worked
        rollbackCurrentTransaction();
    }

    public void testNonXARollbackWithinXA(ClientContext clientContext)
            throws RetekBusinessException {
        doInsert("RPMTransactionTestAppServiceImpl.testNonXARollbackWithinXA (xa)");

        final RPMSessionBeanCommand nonXACommand = new RPMSessionBeanCommand.RPMNonXACommand(
                clientContext) {
            protected Object doExecute() throws RetekBusinessException {
                doInsert("RPMTransactionTestAppServiceImpl.testNonXARollbackWithinXA (non-xa)");
                rollbackTransaction();
                return null;
            }
        };

        nonXACommand.execute();
    }

    public void testNonXAWithinXARollback(ClientContext clientContext)
            throws RetekBusinessException {
        doInsert("RPMTransactionTestAppServiceImpl.testNonXAWithinXARollback (xa)");

        final RPMSessionBeanCommand nonXACommand = new RPMSessionBeanCommand.RPMNonXACommand(
                clientContext) {
            protected Object doExecute() throws RetekBusinessException {
                doInsert("RPMTransactionTestAppServiceImpl.testNonXAWithinXARollback (non-xa)");
                return null;
            }
        };

        nonXACommand.execute();

        rollbackCurrentTransaction();
    }

    public void testTaskEngineCommit(ClientContext clientContext) throws RetekBusinessException {
        String description = "RPMTransactionTestAppServiceImpl.testTaskEngineCommit";
        doInsert(description);
        RPMTask task = createTestTask(description);
        publishTask(task);
    }

    public void testTaskEngineRollback(ClientContext clientContext) throws RetekBusinessException {
        String description = "RPMTransactionTestAppServiceImpl.testTaskEngineRollback";
        doInsert(description);
        RPMTask task = createTestTask(description);
        publishTask(task);
        rollbackCurrentTransaction();
    }

    private static void doInsert(String suffix) {
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

    private void callBmtCommit(ClientContext clientContext) throws RetekBusinessException {
        getTransactionTestAppServiceBmt().testCommit(clientContext);
    }

    private void callBmtRollback(ClientContext clientContext) throws RetekBusinessException {
        getTransactionTestAppServiceBmt().testRollback(clientContext);
    }

    private RPMTransactionTestAppServiceBmt getTransactionTestAppServiceBmt() {
        return (RPMTransactionTestAppServiceBmt) RPMServiceFactoryImpl.getInstance().getService(
                RPMTransactionTestAppServiceBmt.class);
    }

    private static String getDateTimeString() {
        return DateFormat.getDateTimeInstance().format(new Date());
    }

    protected static void close(Object closable) {
        if (closable != null) {
            try {
                closable.getClass().getMethod("close", null).invoke(closable, null);
            } catch (Throwable t) {
                LOG.warn(t, t);
            }
        }
    }

    // /============================================================

    public Integer getTestTableRowCount() throws RetekBusinessException {
        Connection connection = RPMServerContext.getCurrentServerContext().getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("select count(*) from tinetti");
            ResultSet rs = ps.executeQuery();
            rs.next();
            int i = rs.getInt(1);

            return new Integer(i);

        } catch (SQLException e) {
            throw new RetekUnknownSystemException(e);
        } finally {
            close(ps);
        }
    }

    public void truncateTestTable() throws RetekBusinessException {
        Connection connection = RPMServerContext.getCurrentServerContext().getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("delete from tinetti");
            ps.execute();
        } catch (SQLException e) {
            throw new RetekUnknownSystemException(e);
        } finally {
            close(ps);
        }
    }

    public void createTestTable() throws RetekBusinessException {
        Connection connection = RPMServerContext.getCurrentServerContext().getConnection();
        PreparedStatement ps = null;
        try {
            try {
                ps = connection.prepareStatement("drop table tinetti");
                ps.execute();
            } catch (Exception e) {
                // table may not have existed before.. that's okay.
            }

            ps = connection.prepareStatement("create table tinetti (value varchar2(200) ) ");
            ps.execute();

        } catch (SQLException e) {
            throw new RetekUnknownSystemException(e);
        } finally {
            close(ps);
        }
    }

    private void rollbackCurrentTransaction() {
        RPMServerContext.getCurrentServerContext().rollbackTransaction();
    }

    public static RPMTask createTestTask(String description) {
        RPMTaskCoreService taskService = (RPMTaskCoreService) RPMServiceFactoryImpl.getInstance()
                .getService(RPMTaskCoreService.class);
        RPMCompositeTask task = taskService.createCompositeTask(new HashSet());
        task.setDescription(description);
        task.setTaskCommandClassName(TestTaskCommand.class.getName());

        return task;
    }

    private void publishTask(RPMTask task) {
        RPMTaskCoreService taskCoreService = (RPMTaskCoreService) RPMServiceFactoryImpl
                .getInstance().getService(RPMTaskCoreService.class);
        taskCoreService.publishTask(task, false);
    }

    // public static void sendTaskEngineMessage(IdentifiableReference taskReference) {
    // JmsContext jmsContext = RPMServerContext.getCurrentServerContext().getQueueContext();
    // try {
    // Message message = jmsContext
    // .createTextMessage("RPMTransactionTestAppServiceBmtImpl.sendTaskEngineMessage");
    // message.setLongProperty(RPMTask.ID_PROPERTY, ((Long) ((LongObjectId) taskReference
    // .getObjectId()).getValue()).longValue());
    // message
    // .setStringProperty(RPMTask.TYPE_PROPERTY, taskReference
    // .getReferencedClassName());
    // jmsContext.sendMessage(message, RPMTaskCoreServiceImpl.getRpmTaskQueueName());
    // } catch (Throwable t) {
    // throw new RetekUnknownSystemException(t);
    // }
    // }

    public static class TestTaskCommand implements RPMTaskCommand {
        public void execute(RPMTask task) throws RetekBusinessException {
            doInsert(task.getDescription());
        }
    }

}
