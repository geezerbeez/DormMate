package com.itismob.s02.group9.mco.dormmate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A dormitory / condo / bedspace listing.
 * For now this is plain in-memory data (see DormRepository). Later this class can be
 * mapped 1:1 to a Firebase document.
 */
public class Dorm {

    public enum Status { ACTIVE, FULL, HIDDEN }

    /** A nearby landmark shown on the details page (Places API later). */
    public static class Spot {
        public final String name;
        public final String distance;

        public Spot(String name, String distance) {
            this.name = name;
            this.distance = distance;
        }
    }

    public static final String TYPE_SOLO = "Solo";
    public static final String TYPE_SHARED = "Shared";
    public static final String TYPE_BEDSPACE = "Bedspace";

    private String id;
    private String name;
    private String address;
    private String description;
    private String roomType = TYPE_SOLO;
    private int price;          // per month
    private int deposit;
    private int advanceMonths = 1;
    private String lease = "6 - 12 mo";
    private int totalSlots = 1;
    private int availableSlots = 1;
    private int roommates;
    private int beds = 1;
    private int baths = 1;
    private double distanceKm;
    private String walkTime = "-";
    private String transitTime = "-";
    private String driveTime = "-";
    private String contactName = "";
    private String contactPhone = "";
    private String contactEmail = "";
    private Status status = Status.ACTIVE;
    private boolean saved;
    private int inquiries;
    private List<Integer> photos = new ArrayList<>();
    private List<String> amenities = new ArrayList<>();
    private List<String> houseRules = new ArrayList<>();
    private List<Spot> nearbySpots = new ArrayList<>();

    public Dorm() { }

    // ---- helpers -----------------------------------------------------------------------

    public static String formatPeso(int amount) {
        return String.format(Locale.US, "\u20B1%,d", amount);
    }

    public void setContact(String name, String phone, String email) {
        this.contactName = name;
        this.contactPhone = phone;
        this.contactEmail = email;
    }

    public String getPriceLabel() {
        return formatPeso(price);
    }

    public String getDistanceLabel() {
        return String.format(Locale.US, "%.1f km to campus", distanceKm);
    }

    public String getSlotsLabel() {
        if (availableSlots <= 0) return "Fully occupied";
        return availableSlots + (availableSlots == 1 ? " slot left" : " slots left");
    }

    public int getCoverPhoto() {
        return photos.isEmpty() ? R.drawable.dorm_photo_1 : photos.get(0);
    }

    // ---- getters / setters -------------------------------------------------------------

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public int getDeposit() { return deposit; }
    public void setDeposit(int deposit) { this.deposit = deposit; }
    public int getAdvanceMonths() { return advanceMonths; }
    public void setAdvanceMonths(int advanceMonths) { this.advanceMonths = advanceMonths; }
    public String getLease() { return lease; }
    public void setLease(String lease) { this.lease = lease; }
    public int getTotalSlots() { return totalSlots; }
    public void setTotalSlots(int totalSlots) { this.totalSlots = totalSlots; }
    public int getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(int availableSlots) { this.availableSlots = availableSlots; }
    public int getRoommates() { return roommates; }
    public void setRoommates(int roommates) { this.roommates = roommates; }
    public int getBeds() { return beds; }
    public void setBeds(int beds) { this.beds = beds; }
    public int getBaths() { return baths; }
    public void setBaths(int baths) { this.baths = baths; }
    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    public String getWalkTime() { return walkTime; }
    public void setWalkTime(String walkTime) { this.walkTime = walkTime; }
    public String getTransitTime() { return transitTime; }
    public void setTransitTime(String transitTime) { this.transitTime = transitTime; }
    public String getDriveTime() { return driveTime; }
    public void setDriveTime(String driveTime) { this.driveTime = driveTime; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public boolean isSaved() { return saved; }
    public void setSaved(boolean saved) { this.saved = saved; }
    public int getInquiries() { return inquiries; }
    public void setInquiries(int inquiries) { this.inquiries = inquiries; }
    public List<Integer> getPhotos() { return photos; }
    public void setPhotos(List<Integer> photos) { this.photos = photos; }
    public List<String> getAmenities() { return amenities; }
    public void setAmenities(List<String> amenities) { this.amenities = amenities; }
    public List<String> getHouseRules() { return houseRules; }
    public void setHouseRules(List<String> houseRules) { this.houseRules = houseRules; }
    public List<Spot> getNearbySpots() { return nearbySpots; }
    public void setNearbySpots(List<Spot> nearbySpots) { this.nearbySpots = nearbySpots; }
}
