package domain;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.FileOutputStream;
import java.util.Base64;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@ApplicationScoped
public class ListingService {
    public ListingDetails createListing(CreateListing createListing) {
        return null;
    }

    public ListingDetails updateListing(UpdateListing updateListing) {
        return null;
    }

    public ListingDetails deleteListing(String listingId) {
        return null;
    }

    public String getListing(String listingId) {
        return "hello";
    }

    public ListingDetails[] getListings() {
        return null;
    }

//    private byte[] decodeImage(String image) {
//        String imageDataString = "your_base64_string_here"; // Replace with your Base64 string
//
//        try {
//            // Converting a Base64 String into Image byte array
//            byte[] imageByteArray = Base64.getDecoder().decode(imageDataString);
//        } catch (FileNotFoundException e) {
//            System.out.println("Image not found" + e);
//        } catch (IOException ioe) {
//            System.out.println("Exception while converting the Image " + ioe);
//        }
//    }

    private String encodeImage(byte[] imageByteArray) {
        return Base64.getEncoder().encodeToString(imageByteArray);
    }
}
