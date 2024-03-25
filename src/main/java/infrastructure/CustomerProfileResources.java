package infrastructure;

import domain.profile.CustomerProfile;
import domain.profile.CustomerProfileService;
import infrastructure.dto.ApiResponse;
import infrastructure.dto.in.listing.PageDto;
import infrastructure.dto.in.profile.CheckEmailDto;
import infrastructure.dto.in.profile.CreateCustomerProfileDto;
import infrastructure.dto.in.profile.LoginDto;
import infrastructure.dto.in.profile.UpdateCustomerProfileDto;
import infrastructure.dto.out.profile.CustomerProfileDetailsInternalDto;
import infrastructure.dto.out.profile.CustomerProfilePageDto;
import infrastructure.dto.out.profile.CustomerProfilesDetailsDto;
import infrastructure.result.DeleteCustomerResult;
import infrastructure.result.UpdateCustomerProfileResult;
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
@Path("v1/api/profile")
@Produces("application/json")
public class CustomerProfileResources {
	@Inject
	CustomerProfileService customerProfileService;

	@POST
	@Path("/create-profile")
	public Response createCustomerProfile(CreateCustomerProfileDto createCustomerProfileDto) {
		ApiResponse<CustomerProfilesDetailsDto> response = new ApiResponse<>(Optional.empty(),
																			 200,
																			 Optional.empty());
		Optional<CustomerProfile> createdCustomerProfile =
				customerProfileService.createProfile(createCustomerProfileDto.toDomain());
		if (createdCustomerProfile.isEmpty()) {
			response.setMessage(Optional.of("Customer with email " + createCustomerProfileDto.email() + " already exists."));
			response.setCode(4001);
		} else {
			response.setData(Optional.of(CustomerProfilesDetailsDto.fromDomain(createdCustomerProfile.get())));
		}
		return Response.ok(response).build();
	}

	@POST
	@Path("/get-profile/{customerId}")
	public Response getCustomerProfile(@PathParam("customerId") String customerId) {
		Optional<CustomerProfile> fetchedCustomerProfile =
				customerProfileService.getCustomerProfile(customerId);

		ApiResponse<CustomerProfilesDetailsDto> response = new ApiResponse<>(Optional.empty(),
																			 200,
																			 Optional.empty());

		if (fetchedCustomerProfile.isPresent()) {
			response.setData(Optional.of(CustomerProfilesDetailsDto.fromDomain(
					fetchedCustomerProfile.get())));
		} else {
			response.setMessage(Optional.of("Cannot find customer with id " + customerId + "."));
			response.setCode(4001);
		}

		return Response.ok(response).build();
	}

	@POST
	@Path("/update-profile")
	public Response updateCustomerProfile(UpdateCustomerProfileDto updateCustomerProfileDto) {
		UpdateCustomerProfileResult updateCustomerProfileResult =
				customerProfileService.updateCustomerProfile(updateCustomerProfileDto.toDomain());
		ApiResponse<CustomerProfilesDetailsDto> response = new ApiResponse<>(Optional.empty(),
																			 200,
																			 Optional.empty());
		if (updateCustomerProfileResult.isCustomerNotFound()) {
			response.setMessage(Optional.of("Cannot find customer with id " + updateCustomerProfileDto.id() + "."));
			response.setCode(4001);
		} else if (updateCustomerProfileResult.isValidationError()) {
			response.setMessage(Optional.of("Your provided information (id and password) is not valid. Please check and try again."));
			response.setCode(4001);
		} else {
			response.setData(
					Optional.of(CustomerProfilesDetailsDto.fromDomain(updateCustomerProfileResult.getUpdatedCustomerProfile().get())));
		}
		return Response.ok(response).build();
	}

	@POST
	@Path("/delete-profile/{customerId}")
	public Response deleteCustomerProfile(@PathParam("customerId") String customerId) {
		ApiResponse<CustomerProfilesDetailsDto> response = new ApiResponse<>(Optional.empty(),
																			 200,
																			 Optional.empty());

		DeleteCustomerResult deleteCustomerResult = customerProfileService.deleteCustomerProfile(customerId);
		if (deleteCustomerResult.deletedCustomerProfile().isEmpty()) {
			response.setMessage(Optional.of("Cannot find customer with id " + customerId + ", as it's not found."));
			response.setCode(4001);
		} else if (deleteCustomerResult.hasCreatedListings()) {
			response.setMessage(Optional.of("Customer with id " + customerId + " has created listings. Cannot delete."));
			response.setCode(4001);
		} else {
			response.setData(Optional.of(CustomerProfilesDetailsDto.fromDomain(deleteCustomerResult.deletedCustomerProfile().get())));
		}

		return Response.ok(response).build();
	}

	@POST
	@Path("/get-profile/page")
	public Response getCustomerProfilesByPage(PageDto pageDto) {
		ApiResponse<CustomerProfilePageDto> response = new ApiResponse<>(Optional.empty(), 200, Optional.empty());

		if (pageDto.page < 0 || (pageDto.pageSize.isPresent() && pageDto.pageSize.get() < 0)) {
			response.setMessage(Optional.of("Page number and page size cannot be negative."));
			response.setCode(4001);
			return Response.ok(response).build();
		}
		List<CustomerProfilesDetailsDto> customerProfilePage
				= customerProfileService.getCustomerProfileByPage(pageDto.page,
																  pageDto.pageSize.orElse(20))
										.stream()
										.map(CustomerProfilesDetailsDto::fromDomain)
										.toList();

		CustomerProfilePageDto customerProfilePageDto
				= CustomerProfilePageDto.builder()
										.customerProfiles(customerProfilePage)
										.build();
		response.setData(Optional.ofNullable(customerProfilePageDto));
		return Response.ok(response).build();
	}

	@POST
	@Path("/login")
	public Response customerLogin(LoginDto loginDto) {
		ApiResponse<CustomerProfilesDetailsDto> response = new ApiResponse<>(Optional.empty(),
																			 200,
																			 Optional.empty());

		Optional<CustomerProfile> fetchedCustomerProfile = customerProfileService.customerLogin(loginDto.toDomain());
		if (fetchedCustomerProfile.isEmpty()) {
			response.setMessage(Optional.of("Cannot find customer with email " + loginDto.email().get() + " and provided password."));
			response.setCode(4001);
		} else {
			response.setData(Optional.of(CustomerProfilesDetailsDto.fromDomain(fetchedCustomerProfile.get())));
		}
		return Response.ok(response).build();
	}

	@POST
	@Path("/check-email")
	public Response checkEmail(CheckEmailDto checkEmailDto) {
		ApiResponse<CustomerProfilesDetailsDto> response = new ApiResponse<>(Optional.empty(),
																			 200,
																			 Optional.empty());

		Optional<CustomerProfile> fetchedCustomerProfile = customerProfileService.checkEmail(checkEmailDto.email());
		if (fetchedCustomerProfile.isEmpty()) {
			response.setMessage(Optional.of("Cannot find customer with email " + checkEmailDto.email() + "."));
			response.setCode(4001);
		} else {
			response.setData(Optional.of(CustomerProfilesDetailsDto.fromDomain(fetchedCustomerProfile.get())));
		}
		return Response.ok(response).build();
	}

	@POST
	@Path("/get-profile/internal/{customerId}")
	public Response getCustomerProfileInternal(@PathParam("customerId") String customerId) {
		Optional<CustomerProfile> fetchedCustomerProfile =
				customerProfileService.getCustomerProfile(customerId);

		ApiResponse<CustomerProfileDetailsInternalDto> response = new ApiResponse<>(Optional.empty(),
																					200,
																					Optional.empty());

		if (fetchedCustomerProfile.isPresent()) {
			response.setData(Optional.of(CustomerProfileDetailsInternalDto.fromDomain(
					fetchedCustomerProfile.get())));
		} else {
			response.setMessage(Optional.of("Cannot find customer with id " + customerId + "."));
			response.setCode(4001);
		}

		return Response.ok(response).build();
	}
}
