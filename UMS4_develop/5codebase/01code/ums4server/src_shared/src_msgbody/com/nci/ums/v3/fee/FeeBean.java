/*
 * FeeBean.java
 * 
 * Created on 2007-9-27, 15:06:50
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.ums.v3.fee;

import java.io.Serializable;

/**
 * UMS Fee definition, every App Service outside may have different fee
 * definition.
 * 
 * @author Qil.Wong
 */
public class FeeBean implements Serializable {

    private static final long serialVersionUID = -8219225368012230679L;
    private int fee = -1;
    private int fee_user_type = -1; // 0：对目的终端MSISDN计费；	1：对源终端MSISDN计费；2：对SP计费；	3：表示本字段无效，对谁计费参见Fee_terminal_Id字段。 
    private String feeServiceNO = "";
    private String fee_terminal_Id = "";// 被计费方号码（如本字节填空，则表示本字段无效，对谁计费参见Fee_UserType字段，本字段与Fee_UserType字段互斥）
    private String feeDesc = ""; //关于费用的描述
    /**
     * 对目的终端MSISDN计费
     */
    public static int FEE_CHARGE_DEST_TERMINAL = 0;
    /**
     * 对源终端MSISDN计费
     */
    public static int FEE_CHARGE_SRC_TERMINAL = 1;
    /**
     * 对SP计费
     */
    public static int FEE_CHARGE_SP = 2;
    /**
     * 本字段无效，无效时需指定fee_terminal_id
     */
    public static int FEE_CHARGE_NULL = 3;

    public FeeBean() {
    }

    public FeeBean(String feeServiceNO, int fee, int fee_user_type) {
        this.feeServiceNO = feeServiceNO;
        this.fee = fee;
        this.fee_user_type = fee_user_type;
    }

    public FeeBean(String feeServiceNO, int feeUserType, int fee, String feeTerminalID, String feeDdescription) {
        this.feeServiceNO = feeServiceNO;
        this.fee_user_type = feeUserType;
        this.fee = fee;
        this.fee_terminal_Id = feeTerminalID;
        this.feeDesc = feeDdescription;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getFeeType() {
        return fee_user_type;
    }

    public void setFeeType(int fee_user_type) {
        this.fee_user_type = fee_user_type;
    }

    public String getFeeServiceNO() {
        return feeServiceNO;
    }

    public void setFeeServiceNO(String feeServiceNO) {
        this.feeServiceNO = feeServiceNO;
    }

    /**
     * @return the fee_terminal_Id
     */
    public String getFee_terminal_Id() {
        return fee_terminal_Id;
    }

    /**
     * @param fee_terminal_Id the fee_terminal_Id to set
     */
    public void setFee_terminal_Id(String fee_terminal_Id) {
        this.fee_terminal_Id = fee_terminal_Id;
    }

    /**
     * @return the feeDesc
     */
    public String getFeeDesc() {
        return feeDesc;
    }

    /**
     * @param feeDesc the feeDesc to set
     */
    public void setFeeDesc(String feeDesc) {
        this.feeDesc = feeDesc;
    }

    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.fee;
        hash = 41 * hash + this.fee_user_type;
        hash = 41 * hash + (this.feeServiceNO != null ? this.feeServiceNO.hashCode() : 0);
        hash = 41 * hash + (this.fee_terminal_Id != null ? this.fee_terminal_Id.hashCode() : 0);
        hash = 41 * hash + (this.feeDesc != null ? this.feeDesc.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj instanceof FeeBean) {
            return ((FeeBean) obj).getFee() == fee && ((FeeBean) obj).getFeeDesc().equals(feeDesc) && ((FeeBean) obj).getFeeServiceNO().equals(feeServiceNO) && ((FeeBean) obj).getFeeType() == fee_user_type && ((FeeBean) obj).getFee_terminal_Id().equals(fee_terminal_Id);
        }
        return false;
    }

    public String toString() {
        return "[" + feeServiceNO + "]" + feeDesc;
    }
}
