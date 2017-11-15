package com.yonggang.liyangyang.businesscard.Entry;

/**
 * Created by liyangyang on 2017/9/21.
 */

public class Product {

    /**
     * CategoryID : 1
     * CategoryName : 凉菜
     * DishInfo : {"DishesID":"2","DishesNo":"卤猪蹄","DishesName":null,"Unit":"份","Specifications":"","SalePrice":10}
     */

    private String CategoryID;
    private String CategoryName;
    private DishInfoBean DishInfo;

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String CategoryID) {
        this.CategoryID = CategoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public DishInfoBean getDishInfo() {
        return DishInfo;
    }

    public void setDishInfo(DishInfoBean DishInfo) {
        this.DishInfo = DishInfo;
    }

    public static class DishInfoBean {
        /**
         * DishesID : 2
         * DishesNo : 卤猪蹄
         * DishesName : null
         * Unit : 份
         * Specifications :
         * SalePrice : 10.0
         */

        private String DishesID;
        private String DishesNo;
        private Object DishesName;
        private String Unit;
        private String Specifications;
        private double SalePrice;

        public String getDishesID() {
            return DishesID;
        }

        public void setDishesID(String DishesID) {
            this.DishesID = DishesID;
        }

        public String getDishesNo() {
            return DishesNo;
        }

        public void setDishesNo(String DishesNo) {
            this.DishesNo = DishesNo;
        }

        public Object getDishesName() {
            return DishesName;
        }

        public void setDishesName(Object DishesName) {
            this.DishesName = DishesName;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }

        public String getSpecifications() {
            return Specifications;
        }

        public void setSpecifications(String Specifications) {
            this.Specifications = Specifications;
        }

        public double getSalePrice() {
            return SalePrice;
        }

        public void setSalePrice(double SalePrice) {
            this.SalePrice = SalePrice;
        }
    }
}
