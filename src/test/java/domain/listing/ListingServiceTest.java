package domain.listing;

import domain.profile.CustomerProfile;
import domain.profile.CustomerProfileRepository;
import infrastructure.result.ListingStarResult;
import infrastructure.result.ListingUnStarResult;
import infrastructure.result.UpdateListingResult;
import infrastructure.sql.entity.CustomerProfileEntity;
import infrastructure.sql.entity.ListingEntity;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ListingServiceTest {
	@InjectMock
	ListingRepository listingRepository;

	@InjectMock
	CustomerProfileRepository customerProfileRepository;

	@Inject
	ListingService listingService;

	@Test
	public void createListing_whenCreateListing_profileNotExist_returnEmpty() {
		CreateListing createListing = CreateListing.builder()
												   .title("title")
												   .description("description")
												   .price(Optional.of(0.0))
												   .longitude(0.0)
												   .latitude(0.0)
												   .category(Category.BOOKS)
												   .customerId("customerID")
												   .status(ListingStatus.ACTIVE)
												   .images(Collections.emptyList())
												   .createdAt(System.currentTimeMillis())
												   .updatedAt(System.currentTimeMillis())
												   .build();

		when(customerProfileRepository.getCustomerProfile("customerID")).thenReturn(Optional.empty());
		Optional<ListingDetails> result = listingService.createListing(createListing);
		assertEquals(Optional.empty(), result);
	}

	@Test
	public void createListing_whenCreateListing_profileExist_returnListingDetails() {

		CreateListing createListing = CreateListing.builder()
												   .title("title")
												   .description("description")
												   .price(Optional.of(0.0))
												   .longitude(0.0)
												   .latitude(0.0)
												   .category(Category.BOOKS)
												   .customerId("customerID")
												   .status(ListingStatus.ACTIVE)
												   .images(Collections.emptyList())
												   .createdAt(System.currentTimeMillis())
												   .updatedAt(System.currentTimeMillis())
												   .build();

		CustomerProfileEntity SampleProfile = new CustomerProfileEntity();
		SampleProfile.setId("customerID");

		CustomerProfileEntity mockProfile = Mockito.mock(CustomerProfileEntity.class);
		when(mockProfile.getPostedListings()).thenReturn(new ArrayList<>());
		when(customerProfileRepository.getCustomerProfile("customerID")).thenReturn(Optional.of(mockProfile));

		Optional<ListingDetails> result = listingService.createListing(createListing);
		if (result.isPresent()) {
			ListingDetails actuallistingdetails = result.get();
			assertEquals("title", actuallistingdetails.getTitle());
		}

	}

	@Test
	public void validateListing_withValidData_shouldReturnTrue() {
		CreateListing createListing = CreateListing.builder()
												   .customerId("validCustomerId")
												   .title("Valid Title")
												   .description("Valid Description")
												   .price(Optional.of(0.0))
												   .longitude(1.0)
												   .latitude(1.0)
												   .category(Category.BOOKS)
												   .status(ListingStatus.ACTIVE)
												   .images(Collections.singletonList("image.jpg"))
												   .createdAt(System.currentTimeMillis())
												   .updatedAt(System.currentTimeMillis())
												   .build();

		assertDoesNotThrow(() -> listingService.validateListing(createListing));
	}

	@Test
	public void validateListing_WithEmptyUserID_ShouldPrintException() {
		CreateListing createListing = CreateListing.builder()
												   .customerId("")
												   .title("Valid Title")
												   .description("Valid Description")
												   .price(Optional.of(0.0))
												   .longitude(1.0)
												   .latitude(1.0)
												   .category(Category.BOOKS)
												   .status(ListingStatus.ACTIVE)
												   .images(Collections.singletonList("image.jpg"))
												   .createdAt(System.currentTimeMillis())
												   .updatedAt(System.currentTimeMillis())
												   .build();

		Exception exception = assertThrows(BadRequestException.class, () -> listingService.validateListing(createListing));
		assertTrue(exception.getMessage().contains("User ID is required"));
	}

	@Test
	public void validateListing_WithNullUserID_ShouldPrintException() {
		CreateListing createListing = CreateListing.builder()
												   .customerId(null)
												   .title("Valid Title")
												   .description("Valid Description")
												   .price(Optional.of(0.0))
												   .longitude(1.0)
												   .latitude(1.0)
												   .category(Category.BOOKS)
												   .status(ListingStatus.ACTIVE)
												   .images(Collections.singletonList("image.jpg"))
												   .createdAt(System.currentTimeMillis())
												   .updatedAt(System.currentTimeMillis())
												   .build();

		Exception exception = assertThrows(BadRequestException.class, () -> listingService.validateListing(createListing));
		assertTrue(exception.getMessage().contains("User ID is required"));
	}

	@Test
	public void validateListing_WithEmptyTitle_ShouldPrintException() {
		CreateListing createListing = CreateListing.builder()
												   .customerId("validID")
												   .title("")
												   .description("Valid Description")
												   .price(Optional.of(0.0))
												   .longitude(1.0)
												   .latitude(1.0)
												   .category(Category.BOOKS)
												   .status(ListingStatus.ACTIVE)
												   .images(Collections.singletonList("image.jpg"))
												   .createdAt(System.currentTimeMillis())
												   .updatedAt(System.currentTimeMillis())
												   .build();

		Exception exception = assertThrows(BadRequestException.class, () -> listingService.validateListing(createListing));
		assertTrue(exception.getMessage().contains("Title is required"));
	}

	@Test
	public void validateListing_WithNullTitle_ShouldPrintException() {
		CreateListing createListing = CreateListing.builder()
												   .customerId("validID")
												   .title(null)
												   .description("Valid Description")
												   .price(Optional.of(0.0))
												   .longitude(1.0)
												   .latitude(1.0)
												   .category(Category.BOOKS)
												   .status(ListingStatus.ACTIVE)
												   .images(Collections.singletonList("image.jpg"))
												   .createdAt(System.currentTimeMillis())
												   .updatedAt(System.currentTimeMillis())
												   .build();

		Exception exception = assertThrows(BadRequestException.class, () -> listingService.validateListing(createListing));
		assertTrue(exception.getMessage().contains("Title is required"));
	}

	@Test
	public void validateListing_WithEmptyDescription_ShouldPrintException() {
		CreateListing createListing = CreateListing.builder()
												   .customerId("validID")
												   .title("validtitle")
												   .description("")
												   .price(Optional.of(0.0))
												   .longitude(1.0)
												   .latitude(1.0)
												   .category(Category.BOOKS)
												   .status(ListingStatus.ACTIVE)
												   .images(Collections.singletonList("image.jpg"))
												   .createdAt(System.currentTimeMillis())
												   .updatedAt(System.currentTimeMillis())
												   .build();

		Exception exception = assertThrows(BadRequestException.class, () -> listingService.validateListing(createListing));
		assertTrue(exception.getMessage().contains("Description is required"));
	}

	@Test
	public void validateListing_WithNullDescription_ShouldPrintexception() {
		CreateListing createListing = CreateListing.builder()
												   .customerId("validID")
												   .title("validtitle")
												   .description(null)
												   .price(Optional.of(0.0))
												   .longitude(1.0)
												   .latitude(1.0)
												   .category(Category.BOOKS)
												   .status(ListingStatus.ACTIVE)
												   .images(Collections.singletonList("image.jpg"))
												   .createdAt(System.currentTimeMillis())
												   .updatedAt(System.currentTimeMillis())
												   .build();

		Exception exception = assertThrows(BadRequestException.class, () -> listingService.validateListing(createListing));
		assertTrue(exception.getMessage().contains("Description is required"));
	}

	@Test
	public void getListing_WhenGetListing_ListingNotExist_ReturnEmptyOptional() {
		when(listingRepository.getListing(anyString())).thenReturn(Optional.empty());
		Optional<ListingDetails> result = listingService.getListing("listingID");
		assertFalse(result.isPresent());

	}

	@Test
	public void getListing_WhenGetListing_ListingExist_ReturnListingDetails() {
		String listingID = "listingID";
		ListingEntity mockedListingEntity = Mockito.mock(ListingEntity.class);
		ListingDetails listingDetails = ListingDetails.builder()
													  .id("listingID")
													  .customerId("validID")
													  .title("title")
													  .description(null)
													  .price(Optional.of(0.0))
													  .longitude(1.0)
													  .latitude(1.0)
													  .category(Category.BOOKS)
													  .status(ListingStatus.ACTIVE)
													  .images(Collections.singletonList("image.jpg"))
													  .createdAt(System.currentTimeMillis())
													  .updatedAt(System.currentTimeMillis())
													  .customersWhoStarred(new ArrayList<>())
													  .build();

		when(listingRepository.getListing(listingID)).thenReturn(Optional.of(mockedListingEntity));
		Optional<ListingDetails> result = listingService.getListing(listingID);
		if (result.isPresent()) {
			assertEquals(listingDetails, result.get());
		}

	}

	@Test
	public void updateListing_WhenUpdateListing_ProfileNotExist_ReturnEmpty() {
		UpdateListing updateListing = UpdateListing.builder()
												   .id("listingID")
												   .customerId("customerID")
												   .status(ListingStatus.ACTIVE)
												   .images(Collections.singletonList("image.jpg"))
												   .category(Category.BOOKS)
												   .title("title")
												   .description("description")
												   .price(Optional.of(0.0))
												   .latitude(0.0)
												   .longitude(0.0)
												   .build();
		Mockito.when(customerProfileRepository.getCustomerProfile("customerID")).thenReturn(Optional.empty());
		UpdateListingResult result = listingService.updateListing(updateListing);
		assertEquals(true, result.isCustomerNotFound());
	}

	@Test
	public void updateListing_WhenUpdateListing_ListingNotExist_ReturnEmpty() {
		UpdateListing updateListing = UpdateListing.builder()
												   .id("listingID")
												   .customerId("customerID")
												   .status(ListingStatus.ACTIVE)
												   .images(Collections.singletonList("image.jpg"))
												   .category(Category.BOOKS)
												   .title("title")
												   .description("description")
												   .price(Optional.of(0.0))
												   .latitude(0.0)
												   .longitude(0.0)
												   .build();
		when(customerProfileRepository.getCustomerProfile("customerId")).thenReturn(Optional.of(new CustomerProfileEntity()));
		when(listingService.getListing("listingId")).thenReturn(Optional.empty());
		UpdateListingResult result = listingService.updateListing(updateListing);
		assertEquals(true, result.isListingNotFound());
	}

	@Test
	public void updateListing_WhenUpdateListing_SuccessfulUpdate_ReturnUpdatedListingDetails() {
		UpdateListing updateListing = UpdateListing.builder()
												   .id("listingID")
												   .customerId("customerID")
												   .status(ListingStatus.ACTIVE)
												   .images(Collections.singletonList("image.jpg"))
												   .category(Category.BOOKS)
												   .title("newtitle")
												   .description("newdescription")
												   .price(Optional.of(0.0))
												   .latitude(0.0)
												   .longitude(0.0)
												   .build();

		ListingDetails currentlistingDetails = ListingDetails.builder()
															 .id("listingID")
															 .customerId("customerID")
															 .status(ListingStatus.ACTIVE)
															 .images(Collections.singletonList("image.jpg"))
															 .category(Category.BOOKS)
															 .title("oldtitle")
															 .description("olddescription")
															 .price(Optional.of(0.0))
															 .latitude(0.0)
															 .longitude(0.0)
															 .build();

		ListingDetails updatedlistingDetails = ListingDetails.builder()
															 .id("listingID")
															 .customerId("customerID")
															 .status(ListingStatus.ACTIVE)
															 .images(Collections.singletonList("image.jpg"))
															 .category(Category.BOOKS)
															 .title("newtitle")
															 .description("newdescription")
															 .price(Optional.of(0.0))
															 .latitude(0.0)
															 .longitude(0.0)
															 .build();

		CustomerProfileEntity mockProfile = Mockito.mock(CustomerProfileEntity.class);
		when(customerProfileRepository.getCustomerProfile("customerId")).thenReturn(Optional.of(mockProfile));
		when(listingService.getListing("listingId")).thenReturn(Optional.of(currentlistingDetails));
		UpdateListingResult result = listingService.updateListing(updateListing);
		if (result.getUpdatedListing().isPresent()) {
			assertEquals(updatedlistingDetails, result.getUpdatedListing().get());
		}

	}

	@Test
	public void deleteListing_WhenDeleteListing_ListingNotExist_ReturnEmpty() {
		String listingID = "listingID";
		when(listingService.getListing("listingID")).thenReturn(Optional.empty());
		Optional<ListingDetails> result = listingService.deleteListing(listingID);
		assertEquals(Optional.empty(), result);
	}

	@Test
	public void deleteListing_WhenDeleteListing_CustomerProfileNotExist_ReturnEmpty() {
		String listingID = "listingID";
		ListingDetails currentlistingDetails = ListingDetails.builder()
															 .id("listingID")
															 .customerId("customerID")
															 .status(ListingStatus.ACTIVE)
															 .images(Collections.singletonList("image.jpg"))
															 .category(Category.BOOKS)
															 .title("oldtitle")
															 .description("olddescription")
															 .price(Optional.of(0.0))
															 .latitude(0.0)
															 .longitude(0.0)
															 .build();
		ListingEntity mockedListingEntity = Mockito.mock(ListingEntity.class);
		when(listingRepository.getListing(listingID)).thenReturn(Optional.of(mockedListingEntity));
		when(mockedListingEntity.toDomain()).thenReturn(currentlistingDetails);


		when(customerProfileRepository.getCustomerProfile(currentlistingDetails.customerId)).thenReturn(Optional.empty());
		Optional<ListingDetails> result = listingService.deleteListing(listingID);
		assertEquals(Optional.empty(), result);
	}

	@Test
	public void DeleteListing_WhenDeleteListing_SuccessfulDeletion_ReturnDeletedListingDetails() {
		String listingID = "ListingID1";

		ListingDetails currentlistingDetails = ListingDetails.builder()
															 .id("listingID1")
															 .customerId("customerID")
															 .status(ListingStatus.ACTIVE)
															 .images(Collections.singletonList("image.jpg"))
															 .category(Category.BOOKS)
															 .title("title")
															 .description("description")
															 .price(Optional.of(0.0))
															 .latitude(0.0)
															 .longitude(0.0)
															 .build();

		CustomerProfileEntity mockCustomerProfileEntity = Mockito.mock(CustomerProfileEntity.class);
		List<ListingEntity> mockPostedListings = new ArrayList<>();

		ListingEntity mockListingEntity = Mockito.mock(ListingEntity.class);
		when(mockListingEntity.getId()).thenReturn(listingID);
		when(mockListingEntity.toDomain()).thenReturn(currentlistingDetails);
		mockPostedListings.add(mockListingEntity);
		when(mockCustomerProfileEntity.getPostedListings()).thenReturn(mockPostedListings);
		when(customerProfileRepository.getCustomerProfile("customerID")).thenReturn(Optional.of(mockCustomerProfileEntity));
		when(listingRepository.getListing(listingID)).thenReturn(Optional.of(mockListingEntity));
		when(listingRepository.deleteListing(any(ListingEntity.class))).thenReturn(Optional.of(mockListingEntity));


		Optional<ListingDetails> result = listingService.deleteListing(listingID);


		if (result.isPresent()) {
			assertEquals(currentlistingDetails, result.get());
		}
		assertTrue(mockPostedListings.isEmpty());
	}

	@Test
	public void starListing_whenCustomerProfileNotExists_ReturnEmpty() {
		StarListing starListing = StarListing.builder()
											 .listingId("listingID")
											 .customerId("customerID")
											 .build();
		when(listingRepository.getListing(starListing.getListingId())).thenReturn(Optional.empty());
		when(customerProfileRepository.getCustomerProfile(starListing.getCustomerId())).thenReturn(Optional.empty());
		ListingStarResult result = listingService.starListing(starListing);
		assertEquals(result.getListingDetails(), Optional.empty());
		assert(result.isCustomerNotFound());
	}

	@Test
	public void unstarListing_whenCustomerProfileNotExists_ReturnEmpty() {
		StarListing starListing = StarListing.builder()
											 .listingId("listingID")
											 .customerId("customerID")
											 .build();
		when(listingRepository.getListing(starListing.getListingId())).thenReturn(Optional.empty());
		when(customerProfileRepository.getCustomerProfile(starListing.getCustomerId())).thenReturn(Optional.empty());
		ListingUnStarResult result = listingService.unStarListing(starListing);
		assertEquals(result.getListingDetails(), Optional.empty());
		assert(result.isCustomerNotFound());
	}

	@Test
	public void starListing_whenCustomerProfile_and_listingEntityExist_returnListingDetails() {
		StarListing starListing = StarListing.builder()
											 .listingId("listingID")
											 .customerId("customerID")
											 .build();
		ListingEntity listingEntity = Mockito.mock(ListingEntity.class);
		CustomerProfileEntity customerProfileEntity = Mockito.mock(CustomerProfileEntity.class);
		when(listingRepository.getListing(starListing.getListingId())).thenReturn(Optional.of(listingEntity));
		when(customerProfileRepository.getCustomerProfile(starListing.getCustomerId())).thenReturn(Optional.of(customerProfileEntity));
		List<CustomerProfileEntity> customersWhoStarred = new ArrayList<>();

		when(listingEntity.getCustomersWhoStarred()).thenReturn(customersWhoStarred);

		ListingDetails listingDetails = Mockito.mock(ListingDetails.class);
		when(listingEntity.toDomain()).thenReturn(listingDetails);


		ListingStarResult result = listingService.starListing(starListing);
		if (result.getListingDetails().isPresent()) {
			assertEquals(listingDetails, result.getListingDetails().get());
		}
	}

	@Test
	public void unStarListing_whenCustomerProfile_and_listingEntityExist_returnListingDetails() {
		StarListing starListing = StarListing.builder()
											 .listingId("listingID")
											 .customerId("customerID")
											 .build();
		ListingEntity listingEntity = Mockito.mock(ListingEntity.class);
		CustomerProfileEntity customerProfileEntity = Mockito.mock(CustomerProfileEntity.class);
		when(listingRepository.getListing(starListing.getListingId())).thenReturn(Optional.of(listingEntity));
		when(customerProfileRepository.getCustomerProfile(starListing.getCustomerId())).thenReturn(Optional.of(customerProfileEntity));
		List<CustomerProfileEntity> customersWhoStarred = new ArrayList<>();

		when(listingEntity.getCustomersWhoStarred()).thenReturn(customersWhoStarred);

		ListingDetails listingDetails = Mockito.mock(ListingDetails.class);
		when(listingEntity.toDomain()).thenReturn(listingDetails);


		ListingUnStarResult result = listingService.unStarListing(starListing);
		assertTrue(result.getListingDetails().isPresent());
		assertEquals(listingDetails, result.getListingDetails().get());
	}

	@Test
	public void getListingPage_WhenGettingListingPage_listingIDsEmpty_ReturnEmpty() {
		List<ListingDetails> result = listingService.getListingPage(1, Optional.of(20), Optional.empty());
		assertTrue(result.isEmpty());
	}

	@Test
	public void getListingPage_whenGettingListingPage_LessListingIDsThanPageSize_ReturnList() {
		List<String> listingIds = List.of("123", "345", "456");
		int page = 0;
		ListingEntity listingEntity_1 = Mockito.mock(ListingEntity.class);
		ListingEntity listingEntity_2 = Mockito.mock(ListingEntity.class);
		ListingEntity listingEntity_3 = Mockito.mock(ListingEntity.class);
		ListingDetails listingDetails_1 = Mockito.mock(ListingDetails.class);
		listingDetails_1.setId("123");
		ListingDetails listingDetails_2 = Mockito.mock(ListingDetails.class);
		listingDetails_2.setId("345");
		ListingDetails listingDetails_3 = Mockito.mock(ListingDetails.class);
		listingDetails_3.setId("456");


		when(listingRepository.getListing(eq("123"))).thenReturn(Optional.of(listingEntity_1));
		when(listingRepository.getListing(eq("345"))).thenReturn(Optional.of(listingEntity_2));
		when(listingRepository.getListing(eq("456"))).thenReturn(Optional.of(listingEntity_3));


		when(listingEntity_1.toDomain()).thenReturn(listingDetails_1);
		when(listingEntity_2.toDomain()).thenReturn(listingDetails_2);
		when(listingEntity_3.toDomain()).thenReturn(listingDetails_3);

		Optional<Integer> pageSize = Optional.of(20);
		List<ListingDetails> result = listingService.getListingPage(page, pageSize, Optional.of(listingIds));
		assertFalse(result.isEmpty());
		assertEquals(3, result.size());
	}

	@Test
	public void getListingPage_whenGettingListingPage_MoreListingIDsThanPageSize_ReturnList() {
		List<String> listingIds = List.of("123", "345", "456");
		int page = 1;
		ListingEntity listingEntity_1 = Mockito.mock(ListingEntity.class);
		ListingEntity listingEntity_2 = Mockito.mock(ListingEntity.class);
		ListingEntity listingEntity_3 = Mockito.mock(ListingEntity.class);
		ListingDetails listingDetails_1 = Mockito.mock(ListingDetails.class);
		listingDetails_1.setId("123");
		ListingDetails listingDetails_2 = Mockito.mock(ListingDetails.class);
		listingDetails_2.setId("345");
		ListingDetails listingDetails_3 = Mockito.mock(ListingDetails.class);
		listingDetails_3.setId("456");


		when(listingRepository.getListing(eq("123"))).thenReturn(Optional.of(listingEntity_1));
		when(listingRepository.getListing(eq("345"))).thenReturn(Optional.of(listingEntity_2));
		when(listingRepository.getListing(eq("456"))).thenReturn(Optional.of(listingEntity_3));


		when(listingEntity_1.toDomain()).thenReturn(listingDetails_1);
		when(listingEntity_2.toDomain()).thenReturn(listingDetails_2);
		when(listingEntity_3.toDomain()).thenReturn(listingDetails_3);

		Optional<Integer> pageSize = Optional.of(2);
		List<ListingDetails> result = listingService.getListingPage(page, pageSize, Optional.of(listingIds));
		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
	}

	@Test
	public void getListingAndCustomerByPage_WhenPageAndPageSizeValid_ReturnListingWithCustomerInfo() {
		int page = 0;
		int pageSize = 2;
		ListingEntity mockListingEntity = mock(ListingEntity.class);
		CustomerProfileEntity mockCustomerProfileEntity = mock(CustomerProfileEntity.class);
		when(listingRepository.getListingPage(page, pageSize)).thenReturn(List.of(mockListingEntity));

		when(mockListingEntity.getCustomerProfile()).thenReturn(mockCustomerProfileEntity);
		when(mockCustomerProfileEntity.getId()).thenReturn("customerId");
		when(customerProfileRepository.getCustomerProfile("customerId")).thenReturn(Optional.of(mockCustomerProfileEntity));

		ListingDetails mockListingDetails = Mockito.mock(ListingDetails.class);
		CustomerProfile customerProfile = CustomerProfile.builder()
														 .phone(Optional.of("1234"))
														 .email("123@gmail")
														 .name("tester")
														 .starredListingIds(Optional.of(List.of("123", "345")))
														 .postedListingIds(Optional.of(List.of("456", "567")))
														 .password("78910")
														 .latitude(Optional.of(0.0))
														 .longitude(Optional.of(0.0))
														 .id("customerID")
														 .build();
		when(mockListingEntity.toDomain()).thenReturn(mockListingDetails);
		when(mockCustomerProfileEntity.toDomain()).thenReturn(customerProfile);

		List<ListingWithCustomerInfo> result = listingService.getListingAndCustomerByPage(page, pageSize);
		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		ListingWithCustomerInfo Info = result.get(0);
		assertEquals(mockListingDetails, Info.listingDetails());
		assertEquals(customerProfile, Info.customerProfile());
	}


}

