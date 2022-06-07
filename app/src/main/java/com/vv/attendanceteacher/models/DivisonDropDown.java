package com.vv.attendanceteacher.models;

public class DivisonDropDown {
        private String Id;
        private String classId;
        private String classArmName;
        private String isAssigned;
        public String getId() {
            return Id;
        }
        public void setId(String id) {
            this.Id = id;
        }
        public String getclassId(){
            return classId;
        }
        public void setClassId(String classId){
            this.classId = classId;
        }
        public String getClassArmName() {
            return classArmName;
        }
        public void setClassName(String classArmName) {
            this.classArmName = classArmName;
        }

        public String getIsAssigned(){
            return isAssigned;
        }
        public void setIsAssigned(String isAssigned){
            this.isAssigned = isAssigned;
        }

}
