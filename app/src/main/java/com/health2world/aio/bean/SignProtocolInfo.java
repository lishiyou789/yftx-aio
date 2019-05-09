package com.health2world.aio.bean;

import java.util.List;

/**
 * Created by ylsy on 2018/3/6 0006.
 */

public class SignProtocolInfo {
    String doctorName;
    String doctortelphone;
    String institution;
    String signPersonName;

    String signPersonPhone;
    List<SignMemberBean> signList;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctortelphone() {
        return doctortelphone;
    }

    public void setDoctortelphone(String doctortelphone) {
        this.doctortelphone = doctortelphone;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getSignPersonName() {
        return signPersonName;
    }

    public void setSignPersonName(String signPersonName) {
        this.signPersonName = signPersonName;
    }

    public String getSignPersonPhone() {
        return signPersonPhone;
    }

    public void setSignPersonPhone(String signPersonPhone) {
        this.signPersonPhone = signPersonPhone;
    }

    public List<SignMemberBean> getSignList() {
        return signList;
    }

    public void setSignList(List<SignMemberBean> signList) {
        this.signList = signList;
    }

    public static class SignMemberBean{
        String familyName;
        String idcard;
        String relation;
        String serviceNames;
        String totalPrice;

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getServiceNames() {
            return serviceNames;
        }

        public void setServiceNames(String serviceNames) {
            this.serviceNames = serviceNames;
        }
    }
}
