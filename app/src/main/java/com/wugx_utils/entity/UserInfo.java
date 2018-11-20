package com.wugx_utils.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangxing on 2018/3/12.
 */

public class UserInfo implements Parcelable {
    /**
     * memberList : {"startTime":null,"endTime":null,"memberId":2,"loginName":"13501138453","nickname":"xxyoung","cellPhoneNum":"13501138453","password":"a8cde61a71340cd50bb33d5f1e52b2eb","email":null,"realName":null,"sex":1,"memberType":0,"birthday":null,"idCard":null,"isAuthed":null,"province":null,"city":null,"country":null,"address":null,"headImgUrl":"","qrCodeUrl":null,"longitude":null,"latitude":null,"isStaff":null,"numSort":null,"status":1,"inviterId":null,"checkerId":null,"checkTime":null,"checkContent":null,"createrId":null,"createTime":1520593834000,"updaterId":null,"updateTime":1520658734000}
     */

    private MemberListBean memberList;
    private String code;
    private String msg;

    public MemberListBean getMemberList() {
        return memberList;
    }

    public void setMemberList(MemberListBean memberList) {
        this.memberList = memberList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class MemberListBean implements Parcelable {
        /**
         * startTime : null
         * endTime : null
         * memberId : 2
         * loginName : 13501138453
         * nickname : xxyoung
         * cellPhoneNum : 13501138453
         * password : a8cde61a71340cd50bb33d5f1e52b2eb
         * email : null
         * realName : null
         * sex : 1
         * memberType : 0
         * birthday : null
         * idCard : null
         * isAuthed : null
         * province : null
         * city : null
         * country : null
         * address : null
         * headImgUrl :
         * qrCodeUrl : null
         * longitude : null
         * latitude : null
         * isStaff : null
         * numSort : null
         * status : 1
         * inviterId : null
         * checkerId : null
         * checkTime : null
         * checkContent : null
         * createrId : null
         * createTime : 1520593834000
         * updaterId : null
         * updateTime : 1520658734000
         */

        private String startTime;
        private String endTime;
        private String memberId;
        private String loginName;
        private String nickname;
        private String cellPhoneNum;
        private String password;
        private String email;
        private String realName;
        private String sex;
        private String memberType;
        private String birthday;
        private String idCard;
        private String isAuthed;
        private String province;
        private String city;
        private String country;
        private String address;
        private String headImgUrl;
        private String qrCodeUrl;
        private String longitude;
        private String latitude;
        private String isStaff;
        private String numSort;
        private String status;
        private String inviterId;
        private String checkerId;
        private String checkTime;
        private String checkContent;
        private String createrId;
        private String createTime;
        private String updaterId;
        private String updateTime;
        private String wx;
        private String qq;

        public String getWx() {
            return wx;
        }

        public void setWx(String wx) {
            this.wx = wx;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCellPhoneNum() {
            return cellPhoneNum;
        }

        public void setCellPhoneNum(String cellPhoneNum) {
            this.cellPhoneNum = cellPhoneNum;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getMemberType() {
            return memberType;
        }

        public void setMemberType(String memberType) {
            this.memberType = memberType;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getIsAuthed() {
            return isAuthed;
        }

        public void setIsAuthed(String isAuthed) {
            this.isAuthed = isAuthed;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getIsStaff() {
            return isStaff;
        }

        public void setIsStaff(String isStaff) {
            this.isStaff = isStaff;
        }

        public String getNumSort() {
            return numSort;
        }

        public void setNumSort(String numSort) {
            this.numSort = numSort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getInviterId() {
            return inviterId;
        }

        public void setInviterId(String inviterId) {
            this.inviterId = inviterId;
        }

        public String getCheckerId() {
            return checkerId;
        }

        public void setCheckerId(String checkerId) {
            this.checkerId = checkerId;
        }

        public String getCheckTime() {
            return checkTime;
        }

        public void setCheckTime(String checkTime) {
            this.checkTime = checkTime;
        }

        public String getCheckContent() {
            return checkContent;
        }

        public void setCheckContent(String checkContent) {
            this.checkContent = checkContent;
        }

        public String getCreaterId() {
            return createrId;
        }

        public void setCreaterId(String createrId) {
            this.createrId = createrId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdaterId() {
            return updaterId;
        }

        public void setUpdaterId(String updaterId) {
            this.updaterId = updaterId;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        @Override
        public String toString() {
            return "MemberListBean{" +
                    "startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", memberId='" + memberId + '\'' +
                    ", loginName='" + loginName + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", cellPhoneNum='" + cellPhoneNum + '\'' +
                    ", password='" + password + '\'' +
                    ", email='" + email + '\'' +
                    ", realName='" + realName + '\'' +
                    ", sex='" + sex + '\'' +
                    ", memberType='" + memberType + '\'' +
                    ", birthday='" + birthday + '\'' +
                    ", idCard='" + idCard + '\'' +
                    ", isAuthed='" + isAuthed + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", country='" + country + '\'' +
                    ", address='" + address + '\'' +
                    ", headImgUrl='" + headImgUrl + '\'' +
                    ", qrCodeUrl='" + qrCodeUrl + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", isStaff='" + isStaff + '\'' +
                    ", numSort='" + numSort + '\'' +
                    ", status='" + status + '\'' +
                    ", inviterId='" + inviterId + '\'' +
                    ", checkerId='" + checkerId + '\'' +
                    ", checkTime='" + checkTime + '\'' +
                    ", checkContent='" + checkContent + '\'' +
                    ", createrId='" + createrId + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", updaterId='" + updaterId + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    ", wx='" + wx + '\'' +
                    ", qq='" + qq + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.startTime);
            dest.writeString(this.endTime);
            dest.writeString(this.memberId);
            dest.writeString(this.loginName);
            dest.writeString(this.nickname);
            dest.writeString(this.cellPhoneNum);
            dest.writeString(this.password);
            dest.writeString(this.email);
            dest.writeString(this.realName);
            dest.writeString(this.sex);
            dest.writeString(this.memberType);
            dest.writeString(this.birthday);
            dest.writeString(this.idCard);
            dest.writeString(this.isAuthed);
            dest.writeString(this.province);
            dest.writeString(this.city);
            dest.writeString(this.country);
            dest.writeString(this.address);
            dest.writeString(this.headImgUrl);
            dest.writeString(this.qrCodeUrl);
            dest.writeString(this.longitude);
            dest.writeString(this.latitude);
            dest.writeString(this.isStaff);
            dest.writeString(this.numSort);
            dest.writeString(this.status);
            dest.writeString(this.inviterId);
            dest.writeString(this.checkerId);
            dest.writeString(this.checkTime);
            dest.writeString(this.checkContent);
            dest.writeString(this.createrId);
            dest.writeString(this.createTime);
            dest.writeString(this.updaterId);
            dest.writeString(this.updateTime);
            dest.writeString(this.wx);
            dest.writeString(this.qq);
        }

        public MemberListBean() {
        }

        protected MemberListBean(Parcel in) {
            this.startTime = in.readString();
            this.endTime = in.readString();
            this.memberId = in.readString();
            this.loginName = in.readString();
            this.nickname = in.readString();
            this.cellPhoneNum = in.readString();
            this.password = in.readString();
            this.email = in.readString();
            this.realName = in.readString();
            this.sex = in.readString();
            this.memberType = in.readString();
            this.birthday = in.readString();
            this.idCard = in.readString();
            this.isAuthed = in.readString();
            this.province = in.readString();
            this.city = in.readString();
            this.country = in.readString();
            this.address = in.readString();
            this.headImgUrl = in.readString();
            this.qrCodeUrl = in.readString();
            this.longitude = in.readString();
            this.latitude = in.readString();
            this.isStaff = in.readString();
            this.numSort = in.readString();
            this.status = in.readString();
            this.inviterId = in.readString();
            this.checkerId = in.readString();
            this.checkTime = in.readString();
            this.checkContent = in.readString();
            this.createrId = in.readString();
            this.createTime = in.readString();
            this.updaterId = in.readString();
            this.updateTime = in.readString();
            this.wx = in.readString();
            this.qq = in.readString();
        }

        public static final Creator<MemberListBean> CREATOR = new Creator<MemberListBean>() {
            @Override
            public MemberListBean createFromParcel(Parcel source) {
                return new MemberListBean(source);
            }

            @Override
            public MemberListBean[] newArray(int size) {
                return new MemberListBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.memberList, flags);
        dest.writeString(this.code);
        dest.writeString(this.msg);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.memberList = in.readParcelable(MemberListBean.class.getClassLoader());
        this.code = in.readString();
        this.msg = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Override
    public String toString() {
        return "UserInfo{" +
                "memberList=" + memberList +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}