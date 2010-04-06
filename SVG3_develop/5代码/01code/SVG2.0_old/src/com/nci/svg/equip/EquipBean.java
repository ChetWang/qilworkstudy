package com.nci.svg.equip;

/**
 * 设备编码对象，形式是地区-输配变类型-电压等级&用户输入编号
 * 
 * @author Qil.Wong
 * 
 */
public class EquipBean {

    private String district = null;// 地区
    private String spdType = null;// 输配变类型
    private String baseVol = null;// 电压等级
    private boolean hasVol = false; // 该设备是否有电压等级
    private String userInputCode = null; // 用户输入的编号
    private String equipName = null; //设备名称
    private String equipID = null; //设备编号

    public EquipBean() {
    }

    /**
     * 获取所在地区的值
     * @return
     */
    public String getDistrict() {
        return district;
    }

    /**
     * 设置所在地区
     * @param district
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * 获取输配变类型
     * @return
     */
    public String getSpdType() {
        return spdType;
    }

    /**
     * 设置输配变类型
     * @param spdType
     */
    public void setSpdType(String spdType) {
        this.spdType = spdType;
    }

    /**
     * 获取电压等级
     * @return
     */
    public String getBaseVol() {
        return baseVol;
    }

    /**
     * 设置电压等级
     * @param baseVol
     */
    public void setBaseVol(String baseVol) {
        this.baseVol = baseVol;
    }

    /**
     * 获取用户输入的内容
     * @return 
     */
    public String getUserInputCode() {
        return userInputCode;
    }

    /**
     * 设置用户输入的内容
     * @param userInputCode
     */
    public void setUserInputCode(String userInputCode) {
        this.userInputCode = userInputCode;
    }

    @Override
    public String toString() {
        if (getEquipName() != null)
            return getEquipName();
        StringBuffer sb = new StringBuffer();
        if (district != null && !district.trim().equals("")) {
            sb.append(district);
        }

        if (spdType != null && !spdType.trim().equals("")) {
            sb.append("-").append(spdType);
        }

        if (isHasVol()) {
            if (baseVol != null && !baseVol.trim().equals("")) {// 电压等级如果没有，就用一个空格代替

                sb.append("-").append(baseVol);
            }
        } else {
            sb.append("- ");
        }
        if (userInputCode != null && !userInputCode.trim().equals("")) {
            sb.append(userInputCode);
        }
        return sb.toString();
    }

    /**
     * 判断是否具有电压等级
     * @return true有电压等级，false没有电压等级
     */
    public boolean isHasVol() {
        return hasVol;
    }

    /**
     * 设置是否具有电压等级
     * @param hasVol true有电压等级，false没有电压等级
     */
    public void setHasVol(boolean hasVol) {
        this.hasVol = hasVol;
    }

    /**
     * 获取设备名称
     * @return
     */
    public String getEquipName() {
        return equipName;
    }

    /**
     * 设置设备名称
     * @param equipName 设备名
     */
    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    /**
     * 获取设备编码
     * @return 设备编码
     */
    public String getEquipID() {
        return equipID;
    }

    /**
     * 设置设备编码
     * @param equipID 设备编码
     */
    public void setEquipID(String equipID) {
        this.equipID = equipID;
    }
}
