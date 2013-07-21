package org.vlg.linghu.mybatis.bean;

import java.io.Serializable;

public class MmsSendMessageWithBLOBs extends MmsSendMessage implements Serializable {
    private String sendText;

    private String sendSmil;

    private static final long serialVersionUID = 1L;

    public String getSendText() {
        return sendText;
    }

    public void setSendText(String sendText) {
        this.sendText = sendText == null ? null : sendText.trim();
    }

    public String getSendSmil() {
        return sendSmil;
    }

    public void setSendSmil(String sendSmil) {
        this.sendSmil = sendSmil == null ? null : sendSmil.trim();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MmsSendMessageWithBLOBs other = (MmsSendMessageWithBLOBs) that;
        return (this.getSendId() == null ? other.getSendId() == null : this.getSendId().equals(other.getSendId()))
            && (this.getSendTitle() == null ? other.getSendTitle() == null : this.getSendTitle().equals(other.getSendTitle()))
            && (this.getSendAddtime() == null ? other.getSendAddtime() == null : this.getSendAddtime().equals(other.getSendAddtime()))
            && (this.getSendPic() == null ? other.getSendPic() == null : this.getSendPic().equals(other.getSendPic()))
            && (this.getSendMusic() == null ? other.getSendMusic() == null : this.getSendMusic().equals(other.getSendMusic()))
            && (this.getSendSmilParnum() == null ? other.getSendSmilParnum() == null : this.getSendSmilParnum().equals(other.getSendSmilParnum()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getUserMobile() == null ? other.getUserMobile() == null : this.getUserMobile().equals(other.getUserMobile()))
            && (this.getSendScore() == null ? other.getSendScore() == null : this.getSendScore().equals(other.getSendScore()))
            && (this.getSendMobile() == null ? other.getSendMobile() == null : this.getSendMobile().equals(other.getSendMobile()))
            && (this.getSendStatus() == null ? other.getSendStatus() == null : this.getSendStatus().equals(other.getSendStatus()))
            && (this.getSendDowntime() == null ? other.getSendDowntime() == null : this.getSendDowntime().equals(other.getSendDowntime()))
            && (this.getSendCode() == null ? other.getSendCode() == null : this.getSendCode().equals(other.getSendCode()))
            && (this.getMsgid() == null ? other.getMsgid() == null : this.getMsgid().equals(other.getMsgid()))
            && (this.getLinkid() == null ? other.getLinkid() == null : this.getLinkid().equals(other.getLinkid()))
            && (this.getBackMsg() == null ? other.getBackMsg() == null : this.getBackMsg().equals(other.getBackMsg()))
            && (this.getSendIsdel() == null ? other.getSendIsdel() == null : this.getSendIsdel().equals(other.getSendIsdel()))
            && (this.getIndexId() == null ? other.getIndexId() == null : this.getIndexId().equals(other.getIndexId()))
            && (this.getServiceid() == null ? other.getServiceid() == null : this.getServiceid().equals(other.getServiceid()))
            && (this.getSendText() == null ? other.getSendText() == null : this.getSendText().equals(other.getSendText()))
            && (this.getSendSmil() == null ? other.getSendSmil() == null : this.getSendSmil().equals(other.getSendSmil()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSendId() == null) ? 0 : getSendId().hashCode());
        result = prime * result + ((getSendTitle() == null) ? 0 : getSendTitle().hashCode());
        result = prime * result + ((getSendAddtime() == null) ? 0 : getSendAddtime().hashCode());
        result = prime * result + ((getSendPic() == null) ? 0 : getSendPic().hashCode());
        result = prime * result + ((getSendMusic() == null) ? 0 : getSendMusic().hashCode());
        result = prime * result + ((getSendSmilParnum() == null) ? 0 : getSendSmilParnum().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserMobile() == null) ? 0 : getUserMobile().hashCode());
        result = prime * result + ((getSendScore() == null) ? 0 : getSendScore().hashCode());
        result = prime * result + ((getSendMobile() == null) ? 0 : getSendMobile().hashCode());
        result = prime * result + ((getSendStatus() == null) ? 0 : getSendStatus().hashCode());
        result = prime * result + ((getSendDowntime() == null) ? 0 : getSendDowntime().hashCode());
        result = prime * result + ((getSendCode() == null) ? 0 : getSendCode().hashCode());
        result = prime * result + ((getMsgid() == null) ? 0 : getMsgid().hashCode());
        result = prime * result + ((getLinkid() == null) ? 0 : getLinkid().hashCode());
        result = prime * result + ((getBackMsg() == null) ? 0 : getBackMsg().hashCode());
        result = prime * result + ((getSendIsdel() == null) ? 0 : getSendIsdel().hashCode());
        result = prime * result + ((getIndexId() == null) ? 0 : getIndexId().hashCode());
        result = prime * result + ((getServiceid() == null) ? 0 : getServiceid().hashCode());
        result = prime * result + ((getSendText() == null) ? 0 : getSendText().hashCode());
        result = prime * result + ((getSendSmil() == null) ? 0 : getSendSmil().hashCode());
        return result;
    }
}