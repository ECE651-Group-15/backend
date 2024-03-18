package infrastructure;

import domain.listing.ListingDetails;
import domain.listing.ListingService;
import domain.listing.ListingStatus;
import domain.profile.CustomerProfile;
import domain.profile.CustomerProfileService;
import infrastructure.dto.ApiResponse;
import infrastructure.dto.in.listing.PageDto;
import infrastructure.dto.in.listing.StarListingDto;
import infrastructure.dto.in.profile.GetCustomerListingsDto;
import infrastructure.dto.out.listing.CompletedListingPageDto;
import infrastructure.dto.out.listing.ListingDetailsDto;
import infrastructure.dto.out.listing.ListingPageDetailsDto;
import infrastructure.dto.out.listing.ListingWithCustomerInfoDto;
import infrastructure.dto.out.listing.PostedListingPageDto;
import infrastructure.dto.out.listing.StarredListingPageDto;
import infrastructure.result.CustomerStarResult;
import infrastructure.result.CustomerUnStarResult;
import infrastructure.result.ListingStarResult;
import infrastructure.result.ListingUnStarResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Path("v1/api/listing-profile")
@Produces("application/json")
public class CustomerListingResources {
    @Inject
    CustomerProfileService customerProfileService;

    @Inject
    ListingService listingService;

    @POST
    @Path("/star-listing")
    public Response starListingForCustomer(StarListingDto starListingDto) {
        ApiResponse<ListingWithCustomerInfoDto> response = new ApiResponse<>(Optional.empty(), 200, Optional.empty());

        ListingStarResult listingStarResult = listingService.starListing(starListingDto.toDomain());
		CustomerStarResult customerStarResult = customerProfileService.starListing(starListingDto.toDomain());
        if (customerStarResult.isCustomerNotFound() || listingStarResult.isCustomerNotFound()) {
            response.setMessage(Optional.of("Cannot find customer with id " + starListingDto.customerId() + "."));
            response.setCode(4001);
            return Response.ok(response).build();
        }
		if (customerStarResult.isListingNotFound() || listingStarResult.isListingNotFound()) {
			response.setMessage(Optional.of("Cannot find listing with id " + starListingDto.listingId() + "."));
			response.setCode(4001);
			return Response.ok(response).build();
		}

        response.setData(Optional.of(ListingWithCustomerInfoDto.fromDomain(listingStarResult.getListingDetails().get(),
																		   customerStarResult.getCustomerProfile().get())));
        return Response.ok(response).build();
    }

	@POST
	@Path("/unstar-listing")
	public Response unStarListingForCustomer(StarListingDto starListingDto) {
		ApiResponse<ListingWithCustomerInfoDto> response = new ApiResponse<>(Optional.empty(), 200, Optional.empty());

		ListingUnStarResult listingUnStarResult = listingService.unStarListing(starListingDto.toDomain());
		CustomerUnStarResult customerUnStarResult = customerProfileService.unStarListing(starListingDto.toDomain());
		if (listingUnStarResult.isCustomerNotFound()) {
			response.setMessage(Optional.of("Cannot find customer with id " + starListingDto.customerId() + "."));
			response.setCode(4001);
			return Response.ok(response).build();
		}
		if (customerUnStarResult.isListingNotFound()) {
			response.setMessage(Optional.of("Cannot find listing with id " + starListingDto.listingId() + "."));
			response.setCode(4001);
			return Response.ok(response).build();
		}

		response.setData(Optional.of(ListingWithCustomerInfoDto.fromDomain(listingUnStarResult.getListingDetails().get(),
																		   customerUnStarResult.getCustomerProfile().get())));
		return Response.ok(response).build();
	}

    @POST
    @Path("/get-customer-posted-listings")
    public Response getCustomerPostedListings(GetCustomerListingsDto getCustomerListingsDto) {
        Optional<CustomerProfile> fetchedCustomerProfile = getCustomerProfile(getCustomerListingsDto.customerId());
        ApiResponse<PostedListingPageDto> response = new ApiResponse<>(Optional.empty(), 200, Optional.empty());

        if (getCustomerListingsDto.page() < 0) {
            response.setMessage(Optional.of("Page number cannot be negative."));
            response.setCode(4001);
            return Response.ok(response).build();
        }
        if (fetchedCustomerProfile.isPresent()) {
            List<ListingDetails> postedListings = listingService.getListingPage(getCustomerListingsDto.page(),
                                                                                getCustomerListingsDto.pageSize(),
                                                                                fetchedCustomerProfile.get().getPostedListingIds());
            response.setData(Optional.of(PostedListingPageDto.builder()
                                                             .postedListings(
																	 postedListings.stream()
																				   .filter(listingDetails -> listingDetails.getStatus()
																														   .equals(ListingStatus.ACTIVE))
																				   .map(ListingDetailsDto::fromDomain).toList())
                                                             .build()));
        } else {
            response.setMessage(Optional.of("Cannot find customer with id " + getCustomerListingsDto.customerId() + "."));
            response.setCode(4001);
        }

        return Response.ok(response).build();
    }

	@POST
	@Path("/get-customer-completed-listings")
	public Response getCustomerCompletedListings(GetCustomerListingsDto getCustomerListingsDto) {
		Optional<CustomerProfile> fetchedCustomerProfile = getCustomerProfile(getCustomerListingsDto.customerId());
		ApiResponse<CompletedListingPageDto> response = new ApiResponse<>(Optional.empty(), 200, Optional.empty());

		if (getCustomerListingsDto.page() < 0) {
			response.setMessage(Optional.of("Page number cannot be negative."));
			response.setCode(4001);
			return Response.ok(response).build();
		}
		if (fetchedCustomerProfile.isPresent()) {
			List<ListingDetails> postedListings = listingService.getListingPage(getCustomerListingsDto.page(),
																				getCustomerListingsDto.pageSize(),
																				fetchedCustomerProfile.get().getPostedListingIds());
			response.setData(Optional.of(CompletedListingPageDto.builder()
																.completedListings(
																		postedListings.stream()
																					  .filter(listingDetails -> listingDetails.getStatus()
																															  .equals(ListingStatus.INACTIVE))
																					  .map(ListingDetailsDto::fromDomain).toList())
																.build()));
		} else {
			response.setMessage(Optional.of("Cannot find customer with id " + getCustomerListingsDto.customerId() + "."));
			response.setCode(4001);
		}

		return Response.ok(response).build();
	}

    @POST
    @Path("/starred-listings")
    public Response getStarredListings(GetCustomerListingsDto customerListingsDto) {
        Optional<CustomerProfile> fetchedCustomerProfile = getCustomerProfile(customerListingsDto.customerId());
        ApiResponse<StarredListingPageDto> response = new ApiResponse<>(Optional.empty(), 200, Optional.empty());

        if (customerListingsDto.page() < 0) {
            response.setMessage(Optional.of("Page number cannot be negative."));
            response.setCode(4001);
            return Response.ok(response).build();
        }
        if (fetchedCustomerProfile.isPresent()) {
            List<ListingDetails> starredListings = listingService.getListingPage(customerListingsDto.page(),
                                                                                 customerListingsDto.pageSize(),
                                                                                 fetchedCustomerProfile.get().getStarredListingIds());
            response.setData(Optional.of(StarredListingPageDto.builder()
                                                              .starredListings(
                                                                      starredListings.stream().map(ListingDetailsDto::fromDomain).toList())
                                                              .build()));
        } else {
            response.setMessage(Optional.of("Cannot find customer with id " + customerListingsDto.customerId() + "."));
            response.setCode(4001);
        }

        return Response.ok(response).build();
    }

    @POST
    @Path("/get-listing-page")
    public Response getListingByPage(PageDto pageDto) {
        ApiResponse<ListingPageDetailsDto> response = new ApiResponse<>(Optional.empty(), 200, Optional.empty());

        if (pageDto.page < 0 || (pageDto.pageSize.isPresent() && pageDto.pageSize.get() < 0)) {
            response.setMessage(Optional.of("Page number and page size cannot be negative."));
            response.setCode(4001);
            return Response.ok(response).build();
        }
        List<ListingWithCustomerInfoDto> listingAndCustomerDetails
                = listingService.getListingAndCustomerByPage(pageDto.page,
                                                             pageDto.pageSize.orElse(20))
                                .stream()
                                .map(ListingWithCustomerInfoDto::fromDomain)
                                .toList();
        if (listingAndCustomerDetails.isEmpty()) {
            response.setMessage(Optional.of("No listings found."));
        }
        response.setData(Optional.of(ListingPageDetailsDto.builder().listingDetails(listingAndCustomerDetails).build()));
        return Response.ok(response).build();
    }

    @POST
    @Path("/listing/{listingId}/customer/")
    public Response getListingWithCustomerDetail(@PathParam("listingId") String listingId) {
        ApiResponse<ListingWithCustomerInfoDto> response = new ApiResponse<>(Optional.empty(), 200, Optional.empty());

        Optional<ListingDetails> listingDetails = listingService.getListing(listingId);
        if (listingDetails.isEmpty()) {
            response.setMessage(Optional.of("Cannot find listing with id " + listingId + "."));
            response.setCode(200);
            return Response.ok(response).build();
        }
        Optional<CustomerProfile> customerProfile = customerProfileService.getCustomerProfile(listingDetails.get().getCustomerId());
        if (customerProfile.isEmpty()) {
            response.setMessage(Optional.of("Cannot find customer with id " + listingDetails.get().getCustomerId() + "."));
            response.setCode(200);
            return Response.ok(response).build();
        }
        ListingWithCustomerInfoDto listingAndCustomerDetails = ListingWithCustomerInfoDto.fromDomain(listingDetails.get(), customerProfile.get());
        response.setData(Optional.ofNullable(listingAndCustomerDetails));
        return Response.ok(response).build();
    }

	Optional<CustomerProfile> getCustomerProfile(String customerId) {
		return customerProfileService.getCustomerProfile(customerId);
	}
}
