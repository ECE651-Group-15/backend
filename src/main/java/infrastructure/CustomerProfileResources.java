package infrastructure;

import domain.profile.CustomerProfileService;
import infrastructure.dto.in.profile.CreateCustomerProfileDto;
import infrastructure.dto.in.profile.UpdateCustomerProfileDto;
import infrastructure.dto.out.profile.CustomerProfilesDetailsDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

import java.util.Optional;

@ApplicationScoped
@Path("v1/api/profile")
@Produces("application/json")
public class CustomerProfileResources {
    @Inject
    CustomerProfileService customerProfileService;

    @POST
    @Path("/create-profile")
    public CustomerProfilesDetailsDto createCustomerProfile(CreateCustomerProfileDto createCustomerProfileDto) {
        return CustomerProfilesDetailsDto.fromDomain(customerProfileService.createProfile(createCustomerProfileDto.toDomain()));
    }

    @POST
    @Path("/get-profile/{customerId}")
    public Optional<CustomerProfilesDetailsDto> getCustomerProfile(@PathParam("customerId") String customerId) {
        return Optional.ofNullable(CustomerProfilesDetailsDto.fromDomain(customerProfileService.getCustomerProfile(customerId)
                                                                                               .orElseThrow(() -> new RuntimeException(
                                                                                                       "Cannot find listing with id " + customerId + "."))));
    }

    @POST
    @Path("/update-profile")
    public Optional<CustomerProfilesDetailsDto> updateCustomerProfile(UpdateCustomerProfileDto updateCustomerProfileDto) {
        return Optional.ofNullable(CustomerProfilesDetailsDto.fromDomain(
                customerProfileService.updateCustomerProfile(updateCustomerProfileDto.toDomain())
                                      .orElseThrow(() -> new RuntimeException(
                                              "Cannot update customer with id " + updateCustomerProfileDto.id() + "."))));
    }

    @POST
    @Path("/delete-profile/{customerId}")
    public Optional<CustomerProfilesDetailsDto> deleteCustomerProfile(@PathParam("customerId") String customerId) {
        return Optional.ofNullable(CustomerProfilesDetailsDto.fromDomain(customerProfileService.deleteCustomerProfile(customerId)
                                                                                               .orElseThrow(() -> new RuntimeException(
                                                                                                       "Cannot find customer with id " + customerId + "."))));
    }

}
