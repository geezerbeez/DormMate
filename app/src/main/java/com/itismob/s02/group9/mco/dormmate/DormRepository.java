package com.itismob.s02.group9.mco.dormmate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * In-memory sample data shared by the student side (Explore / Details) and the
 * dorm-owner side (My Listings / Add / Edit).
 *
 * TODO: replace with Firebase (Firestore) calls. The rest of the app only talks to this
 * class, so swapping the data source later should not touch the screens.
 */
public class DormRepository {

    public static final String[] ALL_AMENITIES = {
            "WiFi", "Water", "Aircon", "Study Area", "24/7 Security", "Laundry",
            "Kitchen", "Gym", "Parking", "Elevator"
    };

    private static DormRepository instance;
    private final List<Dorm> dorms = new ArrayList<>();
    private int nextId = 100;

    public static synchronized DormRepository getInstance() {
        if (instance == null) {
            instance = new DormRepository();
            instance.seed();
        }
        return instance;
    }

    private DormRepository() { }

    // ---- queries -----------------------------------------------------------------------

    public List<Dorm> getAll() {
        return new ArrayList<>(dorms);
    }

    public List<Dorm> search(DormFilter filter) {
        List<Dorm> out = new ArrayList<>();
        for (Dorm d : dorms) {
            if (filter.matches(d)) out.add(d);
        }
        return out;
    }

    public Dorm getById(String id) {
        for (Dorm d : dorms) {
            if (d.getId().equals(id)) return d;
        }
        return null;
    }

    // ---- landlord actions --------------------------------------------------------------

    public void add(Dorm d) {
        d.setId("dorm_" + (nextId++));
        dorms.add(0, d);
    }

    public void delete(Dorm d) {
        dorms.remove(d);
    }

    // ---- sample data -------------------------------------------------------------------

    private void seed() {
        Dorm a = base("dorm_1", "1 Bedroom with Skyline View", "SM Green Residences, Taft, Manila",
                Dorm.TYPE_SOLO, 8500, 8500, 1, "8 - 12 mo", 1, 1, 0.4,
                new int[]{R.drawable.dorm_photo_1, R.drawable.dorm_photo_7, R.drawable.dorm_photo_5},
                "Bright 1-bedroom unit on a high floor with a city skyline view. Fully furnished with a bed, "
                        + "study desk, and wardrobe. Ideal for a student who wants a quiet space close to campus.",
                new String[]{"WiFi", "Study Area", "24/7 Security", "Gym", "Aircon", "Water"},
                new String[]{"No smoking inside the unit", "Visitors until 9:00 PM", "Quiet hours 10 PM - 6 AM", "Keep common areas clean"},
                "5 min", "8 min", "4 min");
        a.setRoommates(0); a.setBeds(1); a.setBaths(1);
        a.getNearbySpots().addAll(Arrays.asList(
                new Dorm.Spot("LRT Vito Cruz Station", "3 minutes walk (250 m)"),
                new Dorm.Spot("Kuya J Restaurant", "4 minutes walk (300 m)"),
                new Dorm.Spot("McDonald's Taft", "1 minute walk (60 m)"),
                new Dorm.Spot("Jollibee Taft", "1 minute walk (80 m)")));
        a.setContact("Maria Santos", "09171234567", "maria.santos@example.com");
        a.setInquiries(7); a.setSaved(true);

        Dorm b = base("dorm_2", "Casa Verde Dormitory", "Fidel Reyes St., Malate, Manila",
                Dorm.TYPE_BEDSPACE, 4200, 4200, 1, "6 - 12 mo", 8, 3, 0.8,
                new int[]{R.drawable.dorm_photo_2, R.drawable.dorm_photo_4, R.drawable.dorm_photo_6},
                "Affordable bedspaces in a student-only dormitory. Bunk beds with personal lockers, shared "
                        + "kitchen, and a common study lounge. Curfew is observed for everyone's safety.",
                new String[]{"WiFi", "Water", "Study Area", "Laundry", "Kitchen", "24/7 Security"},
                new String[]{"Curfew at 11:00 PM", "No overnight guests", "No cooking after 9:00 PM", "Lights out by midnight"},
                "12 min", "15 min", "7 min");
        b.setRoommates(5); b.setBeds(6); b.setBaths(2);
        b.getNearbySpots().addAll(Arrays.asList(
                new Dorm.Spot("LRT Quirino Station", "6 minutes walk (480 m)"),
                new Dorm.Spot("Robinsons Place Manila", "9 minutes walk (700 m)"),
                new Dorm.Spot("Mang Inasal Malate", "3 minutes walk (200 m)")));
        b.setContact("Mang Rodel", "09281234567", "casaverde.dorm@example.com");
        b.setInquiries(12);

        Dorm c = base("dorm_3", "Archer's Place Shared Room", "Agno St., Taft, Manila",
                Dorm.TYPE_SHARED, 6500, 6500, 2, "10 - 12 mo", 4, 2, 0.3,
                new int[]{R.drawable.dorm_photo_4, R.drawable.dorm_photo_3, R.drawable.dorm_photo_2},
                "Shared room for two with separate beds, personal desks, and an aircon. A quiet, study-friendly "
                        + "building a short walk from the university gate.",
                new String[]{"WiFi", "Aircon", "Water", "Study Area", "Laundry"},
                new String[]{"No pets", "No smoking", "Visitors in the lobby only", "Respect quiet hours"},
                "4 min", "6 min", "3 min");
        c.setRoommates(1); c.setBeds(2); c.setBaths(1);
        c.getNearbySpots().addAll(Arrays.asList(
                new Dorm.Spot("LRT Vito Cruz Station", "5 minutes walk (400 m)"),
                new Dorm.Spot("7-Eleven Agno", "1 minute walk (50 m)"),
                new Dorm.Spot("Coffee Project Taft", "3 minutes walk (240 m)")));
        c.setContact("Ana Villanueva", "09191234567", "archers.place@example.com");
        c.setInquiries(4);

        Dorm d = base("dorm_4", "Hillcrest Residences Studio", "Pablo Ocampo St., Malate, Manila",
                Dorm.TYPE_SOLO, 11800, 23600, 2, "12 mo", 1, 1, 1.2,
                new int[]{R.drawable.dorm_photo_6, R.drawable.dorm_photo_7, R.drawable.dorm_photo_1},
                "Modern studio condo with a kitchenette, balcony, and building amenities. Great for students "
                        + "or interns who prefer full privacy.",
                new String[]{"WiFi", "Aircon", "Water", "Kitchen", "Gym", "Parking", "Elevator", "24/7 Security"},
                new String[]{"No parties", "No smoking inside", "Max 2 occupants", "Pets not allowed"},
                "18 min", "12 min", "6 min");
        d.setRoommates(0); d.setBeds(1); d.setBaths(1);
        d.getNearbySpots().addAll(Arrays.asList(
                new Dorm.Spot("LRT Pedro Gil Station", "5 minutes walk (400 m)"),
                new Dorm.Spot("Robinsons Place Manila", "7 minutes walk (550 m)")));
        d.setContact("Hillcrest Leasing", "09351234567", "leasing.hillcrest@example.com");
        d.setInquiries(9);

        Dorm e = base("dorm_5", "Malate Garden Suites", "Mabini St., Malate, Manila",
                Dorm.TYPE_SHARED, 5200, 5200, 1, "6 mo", 6, 3, 1.5,
                new int[]{R.drawable.dorm_photo_5, R.drawable.dorm_photo_4},
                "Cozy shared rooms in a renovated apartment building with a rooftop garden. Friendly caretaker "
                        + "on site, and jeepney routes just outside.",
                new String[]{"WiFi", "Water", "Kitchen", "Laundry", "Aircon"},
                new String[]{"Quiet hours 10 PM - 6 AM", "Visitors until 8:00 PM", "Clean as you go"},
                "22 min", "15 min", "9 min");
        e.setRoommates(3); e.setBeds(4); e.setBaths(2);
        e.getNearbySpots().addAll(Arrays.asList(
                new Dorm.Spot("Jeepney stop (Mabini)", "1 minute walk (40 m)"),
                new Dorm.Spot("Mercury Drug", "2 minutes walk (150 m)")));
        e.setContact("Lola Cora", "09451234567", "malategarden@example.com");
        e.setInquiries(3);

        Dorm f = base("dorm_6", "Pioneer Student Lodge", "Leon Guinto St., Malate, Manila",
                Dorm.TYPE_BEDSPACE, 3500, 3500, 1, "6 - 12 mo", 12, 5, 0.9,
                new int[]{R.drawable.dorm_photo_2, R.drawable.dorm_photo_6},
                "Budget-friendly bedspaces with free WiFi and a study hall open 24 hours. Separate floors for "
                        + "male and female residents.",
                new String[]{"WiFi", "Water", "Study Area", "24/7 Security", "Laundry"},
                new String[]{"Separate floors by gender", "Curfew at 11:30 PM", "No cooking in rooms"},
                "14 min", "10 min", "5 min");
        f.setRoommates(7); f.setBeds(8); f.setBaths(3);
        f.getNearbySpots().addAll(Arrays.asList(
                new Dorm.Spot("LRT Vito Cruz Station", "9 minutes walk (700 m)"),
                new Dorm.Spot("Ayala Malls Manila Bay", "12 minutes walk (950 m)")));
        f.setContact("Pioneer Admin Office", "09561234567", "pioneer.lodge@example.com");
        f.setInquiries(15);

        dorms.addAll(Arrays.asList(a, b, c, d, e, f));
    }

    private Dorm base(String id, String name, String address, String type, int price, int deposit,
                      int advance, String lease, int total, int available, double km,
                      int[] photos, String description, String[] amenities, String[] rules,
                      String walk, String transit, String drive) {
        Dorm d = new Dorm();
        d.setId(id);
        d.setName(name);
        d.setAddress(address);
        d.setRoomType(type);
        d.setPrice(price);
        d.setDeposit(deposit);
        d.setAdvanceMonths(advance);
        d.setLease(lease);
        d.setTotalSlots(total);
        d.setAvailableSlots(available);
        d.setDistanceKm(km);
        d.setDescription(description);
        for (int p : photos) d.getPhotos().add(p);
        d.getAmenities().addAll(Arrays.asList(amenities));
        d.getHouseRules().addAll(Arrays.asList(rules));
        d.setWalkTime(walk);
        d.setTransitTime(transit);
        d.setDriveTime(drive);
        return d;
    }
}
