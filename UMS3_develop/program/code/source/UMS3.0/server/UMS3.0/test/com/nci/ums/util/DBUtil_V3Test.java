/*
 * To change this template, choose Tools | Templates | Licenses | Default License
 * and open the template in the editor.
 */

package com.nci.ums.util;

import com.nci.ums.v3.message.UMSMsg;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import junit.framework.TestCase;

/**
 *
 * @author wu00000bing
 */
public class DBUtil_V3Test extends TestCase {
    
    public DBUtil_V3Test(String testName) {
        super(testName);
    }            

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of createOutMsgInsertSQL method, of class DBUtil_V3.
     */
    public void testCreateOutMsgInsertSQL() {
        System.out.println("createOutMsgInsertSQL");
        int msgStorageTableType = 0;
        String expResult = "";
        String result = DBUtil_V3.createOutMsgInsertSQL(msgStorageTableType);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createInMsgInsertSQL method, of class DBUtil_V3.
     */
    public void testCreateInMsgInsertSQL() {
        System.out.println("createInMsgInsertSQL");
        int msgStorageTableType = 0;
        String expResult = "";
        String result = DBUtil_V3.createInMsgInsertSQL(msgStorageTableType);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createDeleteMsgSQL method, of class DBUtil_V3.
     */
    public void testCreateDeleteMsgSQL() {
        System.out.println("createDeleteMsgSQL");
        int msgStorageTableType = 0;
        String expResult = "";
        String result = DBUtil_V3.createDeleteMsgSQL(msgStorageTableType);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createAttachInsertSQL method, of class DBUtil_V3.
     */
    public void testCreateAttachInsertSQL() {
        System.out.println("createAttachInsertSQL");
        String expResult = "";
        String result = DBUtil_V3.createAttachInsertSQL();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createFailedMsgUpdateSQL method, of class DBUtil_V3.
     */
    public void testCreateFailedMsgUpdateSQL() {
        System.out.println("createFailedMsgUpdateSQL");
        String expResult = "";
        String result = DBUtil_V3.createFailedMsgUpdateSQL();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUMSMainChannelPolicy method, of class DBUtil_V3.
     */
    public void testGetUMSMainChannelPolicy() {
        System.out.println("getUMSMainChannelPolicy");
        int expResult = 0;
        int result = DBUtil_V3.getUMSMainChannelPolicy();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of executeFailedMsgUpdate method, of class DBUtil_V3.
     */
    public void testExecuteFailedMsgUpdate() throws Exception {
        System.out.println("executeFailedMsgUpdate");
        PreparedStatement updatePrep = null;
        UMSMsg msg = null;
        DBUtil_V3 instance = new DBUtil_V3();
        instance.executeFailedMsgUpdate(updatePrep, msg);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of executeOutMsgInsertStatement method, of class DBUtil_V3.
     */
    public void testExecuteOutMsgInsertStatement() throws Exception {
        System.out.println("executeOutMsgInsertStatement");
        PreparedStatement msgPrep = null;
        PreparedStatement attachPrep = null;
        UMSMsg msg = null;
        int insertMessageType = 0;
        DBUtil_V3 instance = new DBUtil_V3();
        instance.executeOutMsgInsertStatement(msgPrep, attachPrep, msg, insertMessageType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of executeInMsgInsertStatement method, of class DBUtil_V3.
     */
    public void testExecuteInMsgInsertStatement() throws Exception {
        System.out.println("executeInMsgInsertStatement");
        PreparedStatement inPrep = null;
        PreparedStatement attachPrep = null;
        UMSMsg msg = null;
        int insertMessageType = 0;
        DBUtil_V3 instance = new DBUtil_V3();
        instance.executeInMsgInsertStatement(inPrep, attachPrep, msg, insertMessageType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of excuteMsgDeleteStatement method, of class DBUtil_V3.
     */
    public void testExcuteMsgDeleteStatement() throws Exception {
        System.out.println("excuteMsgDeleteStatement");
        String batchNO = "";
        int serialNO = 0;
        int sequenceNO = 0;
        PreparedStatement deleteStatement = null;
        DBUtil_V3 instance = new DBUtil_V3();
        instance.excuteMsgDeleteStatement(batchNO, serialNO, sequenceNO, deleteStatement);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of queryOutMsgs method, of class DBUtil_V3.
     */
    public void testQueryOutMsgs() throws Exception {
        System.out.println("queryOutMsgs");
        ResultSet rs = null;
        PreparedStatement attPrep = null;
        DBUtil_V3 instance = new DBUtil_V3();
        ArrayList expResult = null;
        ArrayList result = instance.queryOutMsgs(rs, attPrep);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
