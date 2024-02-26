package domain.profile;

import domain.listing.ListingRepository;
import domain.listing.StarListing;
import infrastructure.result.CustomerStarResult;
import infrastructure.result.CustomerUnStarResult;
import infrastructure.result.DeleteCustomerResult;
import infrastructure.result.UpdateCustomerProfileResult;
import infrastructure.sql.entity.CustomerProfileEntity;
import infrastructure.sql.entity.ListingEntity;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class CustomerProfileServiceTest {
	@Inject
	CustomerProfileService customerProfileService;

	@InjectMock
	ListingRepository listingRepository;

	@InjectMock
	CustomerProfileRepository customerProfileRepository;


	@Test
	public void createProfile_WhenCustomerIsExisted_ReturnOptionalEmpty() {
		CreateCustomerProfile createCustomerProfile = CreateCustomerProfile.builder()
																		   .name("tester")
																		   .email("testeremail@gmail.com")
																		   .phone(Optional.of("123456"))
																		   .latitude(Optional.of(0.0))
																		   .longitude(Optional.of(0.0))
																		   .password("1234567")
																		   .build();
		CustomerProfileEntity existingEntity = CustomerProfileEntity.builder()
																	.email("testeremail@gmail.com")
																	.build();
		when(customerProfileRepository.getCustomerProfileByEmail(anyString())).thenReturn(Optional.of(existingEntity));
		Optional<CustomerProfile> result = customerProfileService.createProfile(createCustomerProfile);
		assertEquals(Optional.empty(), result);
	}

	@Test
	public void createProfile_WhenCustomerNotExisted_ReturnCustomerProfile() {
		CreateCustomerProfile createCustomerProfile = CreateCustomerProfile.builder()
																		   .name("tester")
																		   .email("testeremail@gmail.com")
																		   .phone(Optional.of("123456"))
																		   .latitude(Optional.of(0.0))
																		   .longitude(Optional.of(0.0))
																		   .password("1234567")
																		   .build();
		when(customerProfileRepository.getCustomerProfileByEmail(anyString())).thenReturn(Optional.empty());
		Optional<CustomerProfile> result = customerProfileService.createProfile(createCustomerProfile);
		assertTrue(result.isPresent());
		assertEquals(createCustomerProfile.getEmail(), result.get().getEmail());

	}

	@Test
	public void validateProfile_WithValidData_shouldReturnTrue() {
		CreateCustomerProfile createCustomerProfile = CreateCustomerProfile.builder()
																		   .name("tester")
																		   .email("testeremail@gmail.com")
																		   .phone(Optional.of("123456"))
																		   .latitude(Optional.of(0.0))
																		   .longitude(Optional.of(0.0))
																		   .password("1234567")
																		   .build();
		assertDoesNotThrow(() -> customerProfileService.validateProfile(createCustomerProfile));
	}

	@Test
	public void validateProfile_WithEmptyName_shouldPrintException() {
		CreateCustomerProfile createCustomerProfile = CreateCustomerProfile.builder()
																		   .name("")
																		   .email("testeremail@gmail.com")
																		   .phone(Optional.of("123456"))
																		   .latitude(Optional.of(0.0))
																		   .longitude(Optional.of(0.0))
																		   .password("1234567")
																		   .build();
		Exception exception = assertThrows(BadRequestException.class, () -> customerProfileService.validateProfile(createCustomerProfile));
		assertTrue(exception.getMessage().contains("Email name, and password are required for a profile"));
	}

	@Test
	public void validateProfile_WithNullName_shouldPrintException() {
		CreateCustomerProfile createCustomerProfile = CreateCustomerProfile.builder()
																		   .email("testeremail@gmail.com")
																		   .phone(Optional.of("123456"))
																		   .latitude(Optional.of(0.0))
																		   .longitude(Optional.of(0.0))
																		   .password("1234567")
																		   .build();
		Exception exception = assertThrows(BadRequestException.class, () -> customerProfileService.validateProfile(createCustomerProfile));
		assertTrue(exception.getMessage().contains("Email name, and password are required for a profile"));
	}

	@Test
	public void validateProfile_WithEmptyEmail_shouldPrintException() {
		CreateCustomerProfile createCustomerProfile = CreateCustomerProfile.builder()
																		   .name("tester")
																		   .email("")
																		   .phone(Optional.of("123456"))
																		   .latitude(Optional.of(0.0))
																		   .longitude(Optional.of(0.0))
																		   .password("1234567")
																		   .build();
		Exception exception = assertThrows(BadRequestException.class, () -> customerProfileService.validateProfile(createCustomerProfile));
		assertTrue(exception.getMessage().contains("Email name, and password are required for a profile"));
	}

	@Test
	public void validateProfile_WithNullEmail_shouldPrintException() {
		CreateCustomerProfile createCustomerProfile = CreateCustomerProfile.builder()
																		   .name("tester")
																		   .phone(Optional.of("123456"))
																		   .latitude(Optional.of(0.0))
																		   .longitude(Optional.of(0.0))
																		   .password("1234567")
																		   .build();
		Exception exception = assertThrows(BadRequestException.class, () -> customerProfileService.validateProfile(createCustomerProfile));
		assertTrue(exception.getMessage().contains("Email name, and password are required for a profile"));
	}

	@Test
	public void validateProfile_WithEmptyPassword_shouldPrintException() {
		CreateCustomerProfile createCustomerProfile = CreateCustomerProfile.builder()
																		   .name("tester")
																		   .email("testeremail@gmail.com")
																		   .phone(Optional.of("123456"))
																		   .latitude(Optional.of(0.0))
																		   .longitude(Optional.of(0.0))
																		   .password("")
																		   .build();
		Exception exception = assertThrows(BadRequestException.class, () -> customerProfileService.validateProfile(createCustomerProfile));
		assertTrue(exception.getMessage().contains("Email name, and password are required for a profile"));
	}

	@Test
	public void validateProfile_WithNullPassword_shouldPrintException() {
		CreateCustomerProfile createCustomerProfile = CreateCustomerProfile.builder()
																		   .name("tester")
																		   .email("testeremail@gmail.com")
																		   .phone(Optional.of("123456"))
																		   .latitude(Optional.of(0.0))
																		   .longitude(Optional.of(0.0))
																		   .build();
		Exception exception = assertThrows(BadRequestException.class, () -> customerProfileService.validateProfile(createCustomerProfile));
		assertTrue(exception.getMessage().contains("Email name, and password are required for a profile"));
	}

	@Test
	public void getCustomerProfile_WhenProfileNotExists_ReturnEmptyOptional() {
		when(customerProfileRepository.getCustomerProfile(anyString())).thenReturn(Optional.empty());
		Optional<CustomerProfile> result = customerProfileService.getCustomerProfile("profileId");
		assertFalse(result.isPresent());
	}

	@Test
	public void getCustomerProfile_WhenProfileExists_ReturnCustomerProfile() {
		CustomerProfileEntity customerProfileEntity = CustomerProfileEntity.builder()
																		   .id("profileId")
																		   .email("tester@gmail.com")
																		   .password("1234")
																		   .name("tester")
																		   .avatar("123")
																		   .phone("123456")
																		   .longitude(0.0)
																		   .latitude(0.0)
																		   .build();
		CustomerProfile customerProfile = CustomerProfile.builder()
														 .id("profileId")
														 .email("tester@gmail.com")
														 .password("1234")
														 .name("tester")
														 .avatar("123")
														 .phone(Optional.of("123456"))
														 .longitude(Optional.of(0.0))
														 .latitude(Optional.of(0.0))
														 .postedListingIds(Optional.of(List.of()))
														 .starredListingIds(Optional.of(List.of()))
														 .build();
		when(customerProfileRepository.getCustomerProfile("profileId")).thenReturn(Optional.of(customerProfileEntity));
		Optional<CustomerProfile> result = customerProfileService.getCustomerProfile(("profileId"));
		assertTrue(result.isPresent());
		assertEquals(customerProfile.getId(), result.get().getId());
		assertEquals(customerProfile.getEmail(), result.get().getEmail());
		assertEquals(customerProfile.getPassword(), result.get().getPassword());
	}

	@Test
	public void UpdateCustomerProfile_WhenPasswordWrong_SetValidationError() {
		UpdateCustomerProfile updateCustomerProfile = UpdateCustomerProfile.builder()
																		   .id("profileId")
																		   .email("tester@gmail.com")
																		   .password("1234")
																		   .name("tester")
																		   .avatar(Optional.of("123"))
																		   .phone(Optional.of("123456"))
																		   .longitude(Optional.of(0.0))
																		   .latitude(Optional.of(0.0))
																		   .build();
		when(customerProfileService.requireUser(updateCustomerProfile.getId(),
												updateCustomerProfile.getPassword())).thenReturn(Optional.empty());
		UpdateCustomerProfileResult result = customerProfileService.updateCustomerProfile(updateCustomerProfile);
		assertTrue(result.isValidationError());
		assertTrue(result.getUpdatedCustomerProfile().isEmpty());
	}

	@Test
	public void UpdateCustomerProfile_WhenCustomerNotExists_SetCustomerNotFoundToTrue() {
		UpdateCustomerProfile updateCustomerProfile = UpdateCustomerProfile.builder()
																		   .id("profileId")
																		   .email("tester@gmail.com")
																		   .password("1234")
																		   .name("tester")
																		   .avatar(Optional.of("123"))
																		   .phone(Optional.of("123456"))
																		   .longitude(Optional.of(0.0))
																		   .latitude(Optional.of(0.0))
																		   .build();
		CustomerProfileEntity customerProfileEntity = CustomerProfileEntity.builder()
																		   .id("profileId")
																		   .email("tester@gmail.com")
																		   .password("1234")
																		   .name("tester2")
																		   .avatar("123")
																		   .phone("123456")
																		   .longitude(0.0)
																		   .latitude(0.0)
																		   .build();
		when(customerProfileService.requireUser(updateCustomerProfile.getId(),
												updateCustomerProfile.getPassword())).thenReturn(
				Optional.ofNullable(customerProfileEntity));
		when(customerProfileRepository.updateCustomerProfile(updateCustomerProfile)).thenReturn(Optional.empty());
		UpdateCustomerProfileResult result = customerProfileService.updateCustomerProfile(updateCustomerProfile);
		assertFalse(result.isValidationError());
		assertTrue(result.isCustomerNotFound());
		assertTrue(result.getUpdatedCustomerProfile().isEmpty());
	}

	@Test
	public void UpdateCustomerProfile_WhenCustomerExistsAndValidationPass_ReturnUpdateCustomerProfileResult() {
		UpdateCustomerProfile updateCustomerProfile = UpdateCustomerProfile.builder()
																		   .id("profileId")
																		   .email("tester@gmail.com")
																		   .password("1234")
																		   .name("NewName")
																		   .avatar(Optional.of("123"))
																		   .phone(Optional.of("123456"))
																		   .longitude(Optional.of(0.0))
																		   .latitude(Optional.of(0.0))
																		   .build();
		CustomerProfileEntity oldcustomerProfileEntity = CustomerProfileEntity.builder()
																			  .id("profileId")
																			  .email("tester@gmail.com")
																			  .password("1234")
																			  .name("oldName")
																			  .avatar("123")
																			  .phone("123456")
																			  .longitude(0.0)
																			  .latitude(0.0)
																			  .build();
		CustomerProfileEntity newcustomerProfileEntity = CustomerProfileEntity.builder()
																			  .id("profileId")
																			  .email("tester@gmail.com")
																			  .password("1234")
																			  .name("NewName")
																			  .avatar("123")
																			  .phone("123456")
																			  .longitude(0.0)
																			  .latitude(0.0)
																			  .build();
		when(customerProfileService.requireUser(updateCustomerProfile.getId(),
												updateCustomerProfile.getPassword())).thenReturn(
				Optional.ofNullable(oldcustomerProfileEntity));
		when(customerProfileRepository.updateCustomerProfile(updateCustomerProfile)).thenReturn(Optional.of(newcustomerProfileEntity));
		UpdateCustomerProfileResult result = customerProfileService.updateCustomerProfile(updateCustomerProfile);
		assertFalse(result.isValidationError());
		assertFalse(result.isCustomerNotFound());
		assertTrue(result.getUpdatedCustomerProfile().isPresent());
		assertEquals(updateCustomerProfile.getName(), result.getUpdatedCustomerProfile().get().getName());

	}

	@Test
	public void deleteCustomerProfile_WhenCustomerNotExists_SetHasCreatedListingsToFalse() {
		when(customerProfileService.getCustomerProfile("profileId")).thenReturn(Optional.empty());
		Optional<DeleteCustomerResult> result = Optional.ofNullable(customerProfileService.deleteCustomerProfile("profileId"));
		if (result.isPresent()) {
			assertFalse(result.get().hasCreatedListings());
			assertEquals(Optional.empty(), result.get().deletedCustomerProfile());
		}


	}

	@Test
	public void deleteCustomerProfile_WhenCustomerExistsWithNoListings_ReturnDeleteCustomerResult() {
		CustomerProfile customerProfile = CustomerProfile.builder()
														 .id("profileId")
														 .email("tester@gmail.com")
														 .password("1234")
														 .name("tester")
														 .avatar("123")
														 .phone(Optional.of("123456"))
														 .longitude(Optional.of(0.0))
														 .latitude(Optional.of(0.0))
														 .postedListingIds(Optional.of(List.of()))
														 .starredListingIds(Optional.of(List.of()))
														 .build();
		CustomerProfileEntity customerProfileEntity = CustomerProfileEntity.builder()
																		   .id("profileId")
																		   .email("tester@gmail.com")
																		   .password("1234")
																		   .name("tester")
																		   .avatar("123")
																		   .phone("123456")
																		   .longitude(0.0)
																		   .latitude(0.0)
																		   .postedListings(List.of())
																		   .starredListings(List.of())
																		   .build();
		when(customerProfileRepository.getCustomerProfile("profileId")).thenReturn(Optional.of(customerProfileEntity));
		DeleteCustomerResult result = customerProfileService.deleteCustomerProfile("profileId");
		assertFalse(result.hasCreatedListings());
		assertTrue(result.deletedCustomerProfile().isPresent());
		assertEquals(customerProfile, result.deletedCustomerProfile().get());


	}

	@Test
	public void deleteCustomerProfile_WhenCustomerExistsWithListings_ReturnDeleteCustomerResult() {
		String profileId = "profileId";
		CustomerProfile customerProfile = CustomerProfile.builder()
														 .id(profileId)
														 .email("tester@gmail.com")
														 .password("1234")
														 .name("tester")
														 .avatar("123")
														 .phone(Optional.of("123456"))
														 .longitude(Optional.of(0.0))
														 .latitude(Optional.of(0.0))
														 .postedListingIds(Optional.of(List.of("listingId1", "listingId2")))
														 .starredListingIds(Optional.of(List.of()))
														 .build();
		ListingEntity listingEntity_1 = Mockito.mock(ListingEntity.class);
		ListingEntity listingEntity_2 = Mockito.mock(ListingEntity.class);
		CustomerProfileEntity customerProfileEntity = CustomerProfileEntity.builder()
																		   .id(profileId)
																		   .email("tester@gmail.com")
																		   .password("1234")
																		   .name("tester")
																		   .avatar("123")
																		   .phone("123456")
																		   .longitude(0.0)
																		   .latitude(0.0)
																		   .postedListings(List.of(listingEntity_1, listingEntity_2))
																		   .starredListings(List.of())
																		   .build();
		when(customerProfileRepository.getCustomerProfile(profileId)).thenReturn(Optional.of(customerProfileEntity));
		when(customerProfileRepository.deleteCustomerProfile(any(CustomerProfileEntity.class))).thenReturn(
				Optional.of(customerProfileEntity));
		DeleteCustomerResult result = customerProfileService.deleteCustomerProfile(profileId);
		assertTrue(result.hasCreatedListings());
		assertTrue(result.deletedCustomerProfile().isPresent());
		assertEquals(customerProfile.getId(), result.deletedCustomerProfile().get().getId());
		assertEquals(customerProfile.getEmail(), result.deletedCustomerProfile().get().getEmail());

	}

	@Test
	public void starListing_WhenCustomerNotFound_SetCustomerNotFoundToTrue() {
		StarListing starListing = StarListing.builder()
											 .customerId("customerId")
											 .listingId("listingId")
											 .build();
		when(customerProfileRepository.getCustomerProfile(starListing.getCustomerId())).thenReturn(Optional.empty());
		CustomerStarResult result = customerProfileService.starListing(starListing);
		assertTrue(result.isCustomerNotFound());
		assertFalse(result.isListingNotFound());
		assertFalse(result.getCustomerProfile().isPresent());
	}

	@Test
	public void starListing_WhenListingNotFound_SetListingNotFoundToTrue() {
		StarListing starListing = StarListing.builder()
											 .customerId("customerId")
											 .listingId("listingId")
											 .build();
		when(customerProfileRepository.getCustomerProfile(starListing.getCustomerId())).thenReturn(
				Optional.of(new CustomerProfileEntity()));
		when(listingRepository.getListing(starListing.getListingId())).thenReturn(Optional.empty());
		CustomerStarResult result = customerProfileService.starListing(starListing);
		assertFalse(result.isCustomerNotFound());
		assertTrue(result.isListingNotFound());
		assertFalse(result.getCustomerProfile().isPresent());
	}

	@Test
	public void starListing_WhenListingAndCustomerExist_ReturnCustomerStarListingResult() {
		StarListing starListing = StarListing.builder()
											 .customerId("customerId")
											 .listingId("listingId")
											 .build();
		CustomerProfileEntity customerProfileEntity = CustomerProfileEntity.builder()
																		   .id("profileId")
																		   .email("tester@gmail.com")
																		   .password("1234")
																		   .name("tester")
																		   .avatar("123")
																		   .phone("123456")
																		   .longitude(0.0)
																		   .latitude(0.0)
																		   .postedListings(List.of())
																		   .starredListings(new ArrayList<>())
																		   .build();
		ListingEntity listingEntity_1 = Mockito.mock(ListingEntity.class);
		when(listingEntity_1.getId()).thenReturn("listingId");
		when(customerProfileRepository.getCustomerProfile(starListing.getCustomerId())).thenReturn(Optional.of(customerProfileEntity));
		when(listingRepository.getListing(starListing.getListingId())).thenReturn(Optional.of(listingEntity_1));
		CustomerStarResult result = customerProfileService.starListing(starListing);
		assertFalse(result.isCustomerNotFound());
		assertFalse(result.isListingNotFound());
		assertTrue(result.getCustomerProfile().isPresent());
		CustomerProfile customerProfile = result.getCustomerProfile().get();
		assertTrue(customerProfile.getStarredListingIds().orElse(List.of()).contains(starListing.getListingId()));

	}

	@Test
	public void UnStarListing_WhenCustomerNotFound_SetCustomerNotFoundToTrue() {
		StarListing starListing = StarListing.builder()
											 .customerId("customerId")
											 .listingId("listingId")
											 .build();
		when(customerProfileRepository.getCustomerProfile(starListing.getCustomerId())).thenReturn(Optional.empty());
		CustomerUnStarResult result = customerProfileService.unStarListing(starListing);
		assertTrue(result.isCustomerNotFound());
		assertFalse(result.isListingNotFound());
		assertFalse(result.getCustomerProfile().isPresent());
	}

	@Test
	public void UnStarListing_WhenListingNotFound_SetListingNotFoundToTrue() {
		StarListing starListing = StarListing.builder()
											 .customerId("customerId")
											 .listingId("listingId")
											 .build();
		when(customerProfileRepository.getCustomerProfile(starListing.getCustomerId())).thenReturn(
				Optional.of(new CustomerProfileEntity()));
		when(listingRepository.getListing(starListing.getListingId())).thenReturn(Optional.empty());
		CustomerUnStarResult result = customerProfileService.unStarListing(starListing);
		assertFalse(result.isCustomerNotFound());
		assertTrue(result.isListingNotFound());
		assertFalse(result.getCustomerProfile().isPresent());
	}

	@Test
	public void UnStarListing_WhenListingAndCustomerExist_ReturnCustomerStarListingResult() {
		StarListing starListing = StarListing.builder()
											 .customerId("customerId")
											 .listingId("listingId")
											 .build();
		ListingEntity listingEntity_1 = Mockito.mock(ListingEntity.class);
		when(listingEntity_1.getId()).thenReturn("listingId");
		List<ListingEntity> initialStarredListings = new ArrayList<>();
		initialStarredListings.add(listingEntity_1);
		CustomerProfileEntity customerProfileEntity = CustomerProfileEntity.builder()
																		   .id("profileId")
																		   .email("tester@gmail.com")
																		   .password("1234")
																		   .name("tester")
																		   .avatar("123")
																		   .phone("123456")
																		   .longitude(0.0)
																		   .latitude(0.0)
																		   .postedListings(List.of())
																		   .starredListings(initialStarredListings)
																		   .build();
		when(customerProfileRepository.getCustomerProfile(starListing.getCustomerId())).thenReturn(Optional.of(customerProfileEntity));
		when(listingRepository.getListing(starListing.getListingId())).thenReturn(Optional.of(listingEntity_1));
		CustomerUnStarResult result = customerProfileService.unStarListing(starListing);
		assertFalse(result.isCustomerNotFound());
		assertFalse(result.isListingNotFound());
		assertTrue(result.getCustomerProfile().isPresent());
		CustomerProfile updatedCustomerProfile = result.getCustomerProfile().get();
		assertFalse(updatedCustomerProfile.getStarredListingIds().orElse(List.of()).contains("listingId"));
	}

	@Test
	public void requireUser_UserNotFound_ReturnEmptyCustomerProfileEntity() {
		when(customerProfileRepository.getCustomerProfile("nonExistentId")).thenReturn(Optional.empty());
		Optional<CustomerProfileEntity> result = customerProfileService.requireUser("nonExistentId", "testPassword");
		assertFalse(result.isPresent());
	}

	@Test
	public void requireUser_UserFoundButWithWrongPassword_ReturnEmptyCustomerProfileEntity() {
		CustomerProfileEntity customerProfileEntity = new CustomerProfileEntity();
		customerProfileEntity.setPassword("CorrectPassword");
		when(customerProfileRepository.getCustomerProfile("ExistentId")).thenReturn(Optional.of(customerProfileEntity));
		Optional<CustomerProfileEntity> result = customerProfileService.requireUser("ExistentId", "WrongPassword");
		assertFalse(result.isPresent());

	}

	@Test
	public void requireUser_UserFoundWithCorrectPassword_ReturnCustomerProfileEntity() {
		CustomerProfileEntity CustomerProfileEntity = new CustomerProfileEntity();
		CustomerProfileEntity.setPassword("CorrectPassword");
		when(customerProfileRepository.getCustomerProfile("ExistId")).thenReturn(Optional.of(CustomerProfileEntity));
		Optional<CustomerProfileEntity> result = customerProfileService.requireUser("ExistId", "CorrectPassword");
		assertTrue(result.isPresent());
		assertEquals("CorrectPassword", result.get().getPassword());
	}

	@Test
	public void getCustomerProfileByPage() {
		int page = 0;
		int pageSize = 2;
		List<CustomerProfileEntity> EntityList = new ArrayList<>();
		CustomerProfileEntity customerProfileEntity_1 = CustomerProfileEntity.builder()
																			 .id("Customer_1")
																			 .email("tester_1@gmail.com")
																			 .password("1234")
																			 .name("tester1")
																			 .avatar("123")
																			 .phone("123456")
																			 .longitude(0.0)
																			 .latitude(0.0)
																			 .postedListings(List.of())
																			 .starredListings(List.of())
																			 .build();
		CustomerProfileEntity customerProfileEntity_2 = CustomerProfileEntity.builder()
																			 .id("Customer_2")
																			 .email("tester_2@gmail.com")
																			 .password("1234")
																			 .name("tester2")
																			 .avatar("123")
																			 .phone("123456")
																			 .longitude(0.0)
																			 .latitude(0.0)
																			 .postedListings(List.of())
																			 .starredListings(List.of())
																			 .build();
		EntityList.add(customerProfileEntity_1);
		EntityList.add(customerProfileEntity_2);
		when(customerProfileRepository.getCustomerProfileByPage(page, pageSize)).thenReturn(EntityList);
		List<CustomerProfile> result = customerProfileService.getCustomerProfileByPage(page, pageSize);
		assertEquals(pageSize, result.size());
		CustomerProfile Customer_1 = result.get(0);
		CustomerProfile Customer_2 = result.get(1);
		assertEquals("Customer_1", Customer_1.getId());
		assertEquals("Customer_2", Customer_2.getId());
		assertEquals("tester_1@gmail.com", Customer_1.getEmail());
		assertEquals("tester_2@gmail.com", Customer_2.getEmail());
	}



	@Test
	public void shouldReturnEmptyWhenNoCustomerExists() {
		Login login = Login.builder()
				.email(Optional.of("email@example.com"))
				.password(Optional.of("password"))
				.build();
		when(customerProfileRepository.getCustomerProfileByEmail(String.valueOf(login.getEmail()))).thenReturn(Optional.empty());

		assertTrue(customerProfileService.customerLogin(login).isEmpty());
	}



	@Test
	public void shouldReturnEmptyWhenCustomerExistsButPasswordDoesNotMatch() {
		String email = "email@example.com";
		String password = "password";
		Login login = new Login(Optional.of(email), Optional.of(password));
		CustomerProfileEntity customerProfileEntity = new CustomerProfileEntity();
		CustomerProfile customerProfile = CustomerProfile.builder()
				.id("profileId")
				.email("email@example.com")
				.password("password")
				.name("tester")
				.avatar("123")
				.phone(Optional.of("123456"))
				.longitude(Optional.of(0.0))
				.latitude(Optional.of(0.0))
				.postedListingIds(Optional.of(List.of()))
				.starredListingIds(Optional.of(List.of()))
				.build();

		when(customerProfileRepository.getCustomerProfileByEmail(email)).thenReturn(Optional.of(customerProfileEntity));


		Optional<CustomerProfile> result = customerProfileService.customerLogin(login);

		assertTrue(result.isEmpty());
	}


	@Test
	public void customerLogin_WithInvalidEmail_ReturnsEmptyOptional() {
		Login login = Login.builder()
				.email(Optional.of("invalid@email.com"))
				.password(Optional.of("password"))
				.build();
		when(customerProfileRepository.getCustomerProfileByEmail(anyString())).thenReturn(Optional.empty());

		Optional<CustomerProfile> result = customerProfileService.customerLogin(login);

		assertFalse(result.isPresent());
	}

	@Test
	public void customerLogin_WithInvalidPassword_ReturnsEmptyOptional() {
		Login login = Login.builder()
				.email(Optional.of("valid@email.com"))
				.password(Optional.of("invalidPassword"))
				.build();
		CustomerProfileEntity customerProfileEntity = Mockito.mock(CustomerProfileEntity.class);


		when(customerProfileRepository.getCustomerProfileByEmail(any())).thenReturn(Optional.of(customerProfileEntity));

		Optional<CustomerProfile> result = customerProfileService.customerLogin(login);

		assertFalse(result.isPresent());
	}

	@Test
	public void customerLogin_WithEmptyCredentials_ReturnsEmptyOptional() {
		Login loginWithEmptyEmail = Login.builder()
				.email(Optional.of(""))
				.password(Optional.of("password"))
				.build();
		Login loginWithEmptyPassword = Login.builder()
				.email(Optional.of("email@email.com"))
				.password(Optional.of(""))
				.build();

		assertTrue(customerProfileService.customerLogin(loginWithEmptyEmail).isEmpty());
		assertTrue(customerProfileService.customerLogin(loginWithEmptyPassword).isEmpty());
	}
}
