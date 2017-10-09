package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.support.v4.view.PagerAdapter;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database related functions
 */
public class DynamoDBManager {

    public static String getUsersTableStatus(String activityName) {

        try {
            AmazonDynamoDBClient ddb = new AmazonDynamoDBClient();
            switch (activityName){
                case "createAccount":
                    ddb = CreateAccountActivity.clientManager.ddb();
                    break;
                case "login":
                    ddb = LoginActivity.clientManager.ddb();
                    break;
                case "newListing":
                    ddb = NewListingFragment.clientManager.ddb();
                    break;
                case "listing":
                    ddb = ListingFragment.clientManager.ddb();
                    break;
                case "search":
                    ddb = SearchResultActivity.clientManager.ddb();
                    break;
                case "reservation":
                    ddb = ReservationFragment.clientManager.ddb();
                    break;
                case "notification":
                    ddb = NotificationFragment.clientManager.ddb();
                    break;
                case "bookmark":
                    ddb = BookmarkFragment.clientManager.ddb();
                    break;
                default:
                    break;
            }

            DescribeTableRequest request = new DescribeTableRequest().withTableName("Users");
            DescribeTableResult result = ddb.describeTable(request);
            String status = result.getTable().getTableStatus();
            return status == null ? "" : status;

        } catch (ResourceNotFoundException e) {}

        return "";
    }

    public static String getListingsTableStatus(String activityName) {

        try {
            AmazonDynamoDBClient ddb = new AmazonDynamoDBClient();
            switch (activityName){
                case "newListing":
                    ddb = NewListingFragment.clientManager.ddb();
                    break;
                case "listing":
                    ddb = ListingFragment.clientManager.ddb();
                    break;
                case "search":
                    ddb = SearchResultActivity.clientManager.ddb();
                    break;
                case "reservation":
                    ddb = ReservationFragment.clientManager.ddb();
                    break;
                case "notification":
                    ddb = NotificationFragment.clientManager.ddb();
                    break;
                case "bookmark":
                    ddb = BookmarkFragment.clientManager.ddb();
                    break;
                default:
                    break;
            }

            DescribeTableRequest request = new DescribeTableRequest().withTableName("Listings");
            DescribeTableResult result = ddb.describeTable(request);
            String status = result.getTable().getTableStatus();
            return status == null ? "" : status;

        } catch (ResourceNotFoundException e) {}

        return "";
    }

    public static int insertUser(String username, String email, String password) {
        AmazonDynamoDBClient ddb = CreateAccountActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        User u;

        u = mapper.load(User.class, username);
        if (u != null){
            return 1;
        }

        u = new User();
        u.setEmail(email);
        DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression();
        queryExpression.setHashKeyValues(u);
        queryExpression.setIndexName("email");
        queryExpression.setConsistentRead(false);
        PaginatedQueryList<User> result = mapper.query(User.class, queryExpression);
        if (result.size() != 0){
            return 2;
        }

        u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(password);
        u.setListings(new ArrayList<String>());
        u.setReservations(new ArrayList<ArrayList<String>>());
        u.setNotifications(new ArrayList<ArrayList<String>>());
        u.setBookmarks(new ArrayList<ArrayList<String>>());

        mapper.save(u);
        return 0;
    }

    public static String loginUser(String email, String password) {
        AmazonDynamoDBClient ddb = LoginActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        User u = new User();
        u.setEmail(email);
        DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression();
        queryExpression.setHashKeyValues(u);
        queryExpression.setIndexName("email");
        queryExpression.setConsistentRead(false);
        PaginatedQueryList<User> result = mapper.query(User.class, queryExpression);
        if (result.size() != 1){
            return null;
        } else if (result.get(0).getPassword().equals(password)){
            return result.get(0).getUsername();
        } else {
            return null;
        }
    }

    public static int insertListing(String username, String address, double lng, double lat, String numSpots, String price, String startTime, String endTime) {
        AmazonDynamoDBClient ddb = NewListingFragment.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        Listing l = new Listing();
        l.setOwner(username);
        l.setAddress(address);
        l.setLongitude(lng);
        l.setLatitude(lat);
        l.setNumSpots(Integer.parseInt(numSpots));
        l.setPrice(Double.parseDouble(price));
        l.setAvailableStarting(startTime);
        l.setAvailableUntil(endTime);
        l.setApproved(new ArrayList<String>());

        mapper.save(l);

        User u = mapper.load(User.class, username);
        if (u == null){
            return 1;
        }
        u.getListings().add(0, l.getId());
        mapper.save(u);
        return 0;
    }

    public static ArrayList<Listing> retrieveListing (String username) {
        AmazonDynamoDBClient ddb = ListingFragment.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        User u = mapper.load(User.class, username);
        if (u == null){
            return null;
        }
        ArrayList<String> s = u.getListings();
        ArrayList<String> new_s = new ArrayList<String>();
        ArrayList<Listing> result = new ArrayList<Listing>();

        for (String id:s) {
            Listing l = mapper.load(Listing.class, id);
            if (l != null){
                result.add(l);
                new_s.add(id);
            }
        }

        u.setListings(new_s);
        mapper.save(u);
        return result;
    }

    public static int removeListing (String username, String listingid) {
        AmazonDynamoDBClient ddb = ListingFragment.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        User u = mapper.load(User.class, username);
        if (u == null){
            return 1;
        }

        Listing l = mapper.load(Listing.class, listingid);
        if (l == null){
            return 1;
        }

        u.getListings().remove(listingid);
        mapper.save(u);
        mapper.delete(l);

        return 0;
    }

    public static ArrayList<Listing> searchListing (String username, double longitude, double latitude, String startTime, String endTime) {
        AmazonDynamoDBClient ddb = SearchResultActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        ArrayList<Listing> result = new ArrayList<Listing>();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);

            double radius = 3958.761;   // earth's radius in miles
            double distance = 1;
            double maxLat = latitude + Math.toDegrees(distance/radius);
            double minLat = latitude - Math.toDegrees(distance/radius);
            double maxLng = longitude + Math.toDegrees(distance/radius) / Math.cos(Math.toRadians(latitude));
            double minLng = longitude - Math.toDegrees(distance/radius) / Math.cos(Math.toRadians(latitude));

            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":val1", new AttributeValue().withN(Double.toString(minLat)));
            eav.put(":val2", new AttributeValue().withN(Double.toString(maxLat)));
            eav.put(":val3", new AttributeValue().withN(Double.toString(minLng)));
            eav.put(":val4", new AttributeValue().withN(Double.toString(maxLng)));

            DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
                    .withFilterExpression("latitude between :val1 and :val2 and longitude between :val3 and :val4")
                    .withExpressionAttributeValues(eav);

            List<Listing> ls = mapper.scan(Listing.class, queryExpression);
            for (Listing l : ls){
                Date l_start = sdf.parse(l.getAvailableStarting());
                Date l_end = sdf.parse(l.getAvailableUntil());
                if ((start.compareTo(l_start) >= 0) && (end.compareTo(l_end) <= 0)){
                    result.add(l);
                }
            }

        }catch(ParseException ex){
            ex.printStackTrace();
        }

        return result;
    }

    public static int reserveListing (String username, String listingid, String startTime, String endTime, String fragmentName) {
        try {
            AmazonDynamoDBClient ddb = new AmazonDynamoDBClient();
            switch (fragmentName) {
                case "search":
                    ddb = SearchResultActivity.clientManager.ddb();
                    break;
                case "bookmark":
                    ddb = BookmarkFragment.clientManager.ddb();
                    break;
            }
            DynamoDBMapper mapper = new DynamoDBMapper(ddb);

            Listing l = mapper.load(Listing.class, listingid);
            if (l == null){
                return 1;
            }
            String owner = l.getOwner();
            String address = l.getAddress();
            mapper.save(l);

            User u = mapper.load(User.class, username);
            String email = u.getEmail();
            ArrayList<ArrayList<String>> rs = u.getReservations();
            ArrayList<String> r = new ArrayList<String>();
            r.add(listingid);
            r.add(startTime);
            r.add(endTime);
            r.add("Confirmation Pending");
            rs.add(0, r);
            u.setReservations(rs);
            mapper.save(u);

            User u2 = mapper.load(User.class, owner);
            ArrayList<String> n = new ArrayList<String>();
            n.add(listingid);
            n.add(username);
            n.add(startTime);
            n.add(endTime);
            n.add("Someone is interested in renting your parking space at "+address+" from "+startTime+" to "+endTime+". Contact information: "+email);
            u2.getNotifications().add(0, n);
            mapper.save(u2);

            return 0;

        }catch(Exception ex){
            ex.printStackTrace();
        }

        return 1;
    }

    public static int bookmarkListing (String username, String listingid, String startTime, String endTime) {
        AmazonDynamoDBClient ddb = SearchResultActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        User u = mapper.load(User.class, username);
        if (u == null){
            return 1;
        }

        ArrayList<ArrayList<String>> bs = u.getBookmarks();
        ArrayList<String> b = new ArrayList<String>();
        b.add(listingid);
        b.add(startTime);
        b.add(endTime);
        bs.add(0, b);
        u.setBookmarks(bs);

        mapper.save(u);
        return 0;
    }

    public static ArrayList<ArrayList<String>> retrieveReservation (String username) {
        AmazonDynamoDBClient ddb = ReservationFragment.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        User u = mapper.load(User.class, username);
        if (u == null){
            return null;
        }
        ArrayList<ArrayList<String>> rs = u.getReservations();
        ArrayList<ArrayList<String>> new_rs = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        for (ArrayList<String> r : rs) {
            String id = r.get(0);
            Listing l = mapper.load(Listing.class, id);
            if (l != null){
                ArrayList<String> info = new ArrayList<String>();
                info.add(l.getAddress());
                info.add(Double.toString(l.getPrice()));
                info.add(r.get(1));
                info.add(r.get(2));
                info.add(r.get(3));
                result.add(info);
                new_rs.add(r);
            }
        }
        u.setReservations(new_rs);
        mapper.save(u);
        return result;
    }

    public static ArrayList<ArrayList<String>> retrieveNotification (String username) {
        AmazonDynamoDBClient ddb = NotificationFragment.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        User u = mapper.load(User.class, username);
        if (u == null){
            return null;
        }
        ArrayList<ArrayList<String>> ns = u.getNotifications();
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        for (ArrayList<String> n : ns) {
            String id = n.get(0);
            Listing l = mapper.load(Listing.class, id);
            if (l != null){
                result.add(n);
            }
        }
        u.setNotifications(result);
        mapper.save(u);
        return result;
    }

    public static int removeNotification (String owner, int removePosition, String listingid, String username, String startTime, String endTime) {
        AmazonDynamoDBClient ddb = NotificationFragment.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        User u = mapper.load(User.class, owner);
        if (u == null){
            return 1;
        }

        u.getNotifications().remove(removePosition);
        mapper.save(u);

        User u2 = mapper.load(User.class, username);
        if (u2 == null){
            return 1;
        }

        ArrayList<ArrayList<String>> rs = u2.getReservations();
        for (ArrayList<String> r : rs) {
            String r_listingid = r.get(0);
            String r_startTime = r.get(1);
            String r_endTime = r.get(2);
            if (r_listingid.equals(listingid) && r_startTime.equals(startTime) && r_endTime.equals(endTime)){
                r.set(3, "Rejected by owner");
                break;
            }
        }
        mapper.save(u2);

        return 0;
    }

    public static int approveRequest (String owner, int approvePosition, String listingid, String username, String startTime, String endTime) {
        AmazonDynamoDBClient ddb = NotificationFragment.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        Listing l = mapper.load(Listing.class, listingid);
        if (l == null){
            return 1;
        }

        User u = mapper.load(User.class, owner);
        if (u == null){
            return 1;
        }
        u.getNotifications().remove(approvePosition);
        mapper.save(u);

        User u2 = mapper.load(User.class, username);
        ArrayList<ArrayList<String>> rs = u2.getReservations();
        for (ArrayList<String> r : rs) {
            String r_listingid = r.get(0);
            String r_startTime = r.get(1);
            String r_endTime = r.get(2);
            if (r_listingid.equals(listingid) && r_startTime.equals(startTime) && r_endTime.equals(endTime)){
                r.set(3, "Approved by owner");
                break;
            }
        }
        mapper.save(u2);

        l.getApproved().add(u2.getEmail());
        mapper.save(l);

        return 0;
    }

    public static ArrayList<ArrayList<String>> retrieveBookmark (String username) {
        AmazonDynamoDBClient ddb = BookmarkFragment.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        User u = mapper.load(User.class, username);
        if (u == null){
            return null;
        }
        ArrayList<ArrayList<String>> bs = u.getBookmarks();
        ArrayList<ArrayList<String>> new_bs = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        for (ArrayList<String> b : bs) {
            String id = b.get(0);
            Listing l = mapper.load(Listing.class, id);
            if (l != null){
                ArrayList<String> info = new ArrayList<String>();
                info.add(l.getAddress());
                info.add(Double.toString(l.getPrice()));
                info.add(b.get(1));
                info.add(b.get(2));
                info.add(l.getId());
                result.add(info);
                new_bs.add(b);
            }
        }
        u.setBookmarks(new_bs);
        mapper.save(u);
        return result;
    }

    public static int removeBookmark (String username, int removePosition) {
        AmazonDynamoDBClient ddb = BookmarkFragment.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        User u = mapper.load(User.class, username);
        if (u == null){
            return 1;
        }

        u.getBookmarks().remove(removePosition);
        mapper.save(u);

        return 0;
    }
}
