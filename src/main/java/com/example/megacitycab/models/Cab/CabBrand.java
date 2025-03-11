package com.example.megacitycab.models.Cab;

public class CabBrand {
    private final int id;
    private final String brandName;

    private CabBrand(CabBrandBuilder builder) {
        this.id = builder.id;
        this.brandName = builder.brandName;
    }

    public int getId() {
        return id;
    }

    public String getBrandName() {
        return brandName;
    }

    public static class CabBrandBuilder {
        private int id;
        private String brandName;

        public CabBrandBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public CabBrandBuilder setBrandName(String brandName) {
            this.brandName = brandName;
            return this;
        }

        public CabBrand build() {
            return new CabBrand(this);
        }
    }
}
