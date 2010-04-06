package com.nci.svg.equip;

/**
 * �豸���������ʽ�ǵ���-���������-��ѹ�ȼ�&�û�������
 * 
 * @author Qil.Wong
 * 
 */
public class EquipBean {

    private String district = null;// ����
    private String spdType = null;// ���������
    private String baseVol = null;// ��ѹ�ȼ�
    private boolean hasVol = false; // ���豸�Ƿ��е�ѹ�ȼ�
    private String userInputCode = null; // �û�����ı��
    private String equipName = null; //�豸����
    private String equipID = null; //�豸���

    public EquipBean() {
    }

    /**
     * ��ȡ���ڵ�����ֵ
     * @return
     */
    public String getDistrict() {
        return district;
    }

    /**
     * �������ڵ���
     * @param district
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * ��ȡ���������
     * @return
     */
    public String getSpdType() {
        return spdType;
    }

    /**
     * �������������
     * @param spdType
     */
    public void setSpdType(String spdType) {
        this.spdType = spdType;
    }

    /**
     * ��ȡ��ѹ�ȼ�
     * @return
     */
    public String getBaseVol() {
        return baseVol;
    }

    /**
     * ���õ�ѹ�ȼ�
     * @param baseVol
     */
    public void setBaseVol(String baseVol) {
        this.baseVol = baseVol;
    }

    /**
     * ��ȡ�û����������
     * @return 
     */
    public String getUserInputCode() {
        return userInputCode;
    }

    /**
     * �����û����������
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
            if (baseVol != null && !baseVol.trim().equals("")) {// ��ѹ�ȼ����û�У�����һ���ո����

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
     * �ж��Ƿ���е�ѹ�ȼ�
     * @return true�е�ѹ�ȼ���falseû�е�ѹ�ȼ�
     */
    public boolean isHasVol() {
        return hasVol;
    }

    /**
     * �����Ƿ���е�ѹ�ȼ�
     * @param hasVol true�е�ѹ�ȼ���falseû�е�ѹ�ȼ�
     */
    public void setHasVol(boolean hasVol) {
        this.hasVol = hasVol;
    }

    /**
     * ��ȡ�豸����
     * @return
     */
    public String getEquipName() {
        return equipName;
    }

    /**
     * �����豸����
     * @param equipName �豸��
     */
    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    /**
     * ��ȡ�豸����
     * @return �豸����
     */
    public String getEquipID() {
        return equipID;
    }

    /**
     * �����豸����
     * @param equipID �豸����
     */
    public void setEquipID(String equipID) {
        this.equipID = equipID;
    }
}
