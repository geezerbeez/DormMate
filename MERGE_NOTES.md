# Merge notes: Main + Bernardo (Explore, Details, Dorm-owner pages)

Base = DormMate-main (chat, roommate discovery, sign-up flow, Firebase auth - all untouched).
Added from the Bernardo zip:

JAVA (new):  Dorm, DormFilter, DormRepository, DormAdapter, FilterBottomSheet,
             DormDetailsActivity, LandlordDashboardActivity, LandlordListingAdapter, AddEditListingActivity
JAVA (replaced, main had not changed them): ExploreFragment, LogIn
LAYOUT (replaced, main had not changed them): fragment_explore, activity_log_in
MANIFEST: added DormDetailsActivity, LandlordDashboardActivity, AddEditListingActivity;
          removed 8 duplicated <activity> entries that were already in main's manifest
RES (new): 12 layouts, ~35 icons/shapes, dorm_photo_1..7.jpg, sample_map.jpg,
           colors_dorm.xml, styles_dorm.xml, color/chip_dorm_bg.xml, color/chip_dorm_text.xml

Renamed to avoid clashing with main's own icons:
  my ic_filter   -> ic_dorm_filter
  my ic_location -> ic_dorm_pin

Entry to the owner side: "Own a dorm? Manage your listings" link at the bottom of LogIn.

Build fix: my color selectors were renamed chip_bg -> chip_dorm_bg and chip_text -> chip_dorm_text,
because main's colors.xml already defines a <color> named chip_bg (duplicate resources error).
