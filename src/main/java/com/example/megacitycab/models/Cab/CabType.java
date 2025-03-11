package com.example.megacitycab.models.Cab;

public class CabType {
    private final int id;
    private final String typeName;
    private String imageUrl;
    private final int capacity;
    private final double baseFare;
    private final double baseWaitTimeFare;

    private CabType(CabTypeBuilder builder) {
        this.id = builder.id;
        this.typeName = builder.typeName;
        this.imageUrl = builder.imageUrl;
        this.capacity = builder.capacity;
        this.baseFare = builder.baseFare;
        this.baseWaitTimeFare = builder.baseWaitTimeFare;
    }

    public int getId() { return id; }
    public String getTypeName() { return typeName; }
    public String getImageUrl() { return imageUrl; }
    public int getCapacity() { return capacity; }
    public double getBaseFare() { return baseFare; }
    public double getBaseWaitTimeFare() { return baseWaitTimeFare; }
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public static class CabTypeBuilder {
        private int id;
        private String typeName;
        private String imageUrl;
        private int capacity;
        private double baseFare;
        private double baseWaitTimeFare;

        public CabTypeBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public CabTypeBuilder setTypeName(String typeName) {
            this.typeName = typeName;
            return this;
        }

        public CabTypeBuilder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public CabTypeBuilder setCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public CabTypeBuilder setBaseFare(double baseFare) {
            this.baseFare = baseFare;
            return this;
        }

        public CabTypeBuilder setBaseWaitTimeFare(double baseWaitTimeFare) {
            this.baseWaitTimeFare = baseWaitTimeFare;
            return this;
        }

        public CabType build() {
            return new CabType(this);
        }
    }
}
