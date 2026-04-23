package u.tallerin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import u.tallerin.domain.entity.FixedPriceService;
import u.tallerin.domain.entity.VariablePriceService;
import u.tallerin.domain.entity.Customer;
import u.tallerin.domain.enums.OrderStatus;
import u.tallerin.domain.enums.ServiceType;
import u.tallerin.domain.enums.VehicleType;
import u.tallerin.repository.CustomerRepository;
import u.tallerin.repository.ServiceRepository;

@SpringBootTest(classes = TallerInApplication.class, properties = {
        "spring.datasource.url=jdbc:h2:mem:carwash;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=true"
})
@AutoConfigureMockMvc
class EndpointFlowIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    private Long fixedServiceId;
    private Long variableServiceId;

    @BeforeEach
    void seedServices() {
        serviceRepository.deleteAll();
        customerRepository.deleteAll();

        FixedPriceService fixed = new FixedPriceService();
        fixed.setName("Basic Wash");
        fixed.setDescription("Fixed price wash");
        fixed.setBasePrice(new BigDecimal("25.00"));
        fixed.setServiceType(ServiceType.BASIC_WASH);
        fixed = serviceRepository.save(fixed);

        VariablePriceService variable = new VariablePriceService();
        variable.setName("Premium Wash");
        variable.setDescription("Variable price wash");
        variable.setBasePrice(new BigDecimal("20.00"));
        variable.setServiceType(ServiceType.PREMIUM_WASH);
        Map<VehicleType, BigDecimal> factors = new EnumMap<>(VehicleType.class);
        factors.put(VehicleType.CAR, new BigDecimal("1.50"));
        factors.put(VehicleType.TRUCK, new BigDecimal("2.00"));
        variable.setFactorByVehicleType(factors);
        variable = serviceRepository.save(variable);

        fixedServiceId = fixed.getId();
        variableServiceId = variable.getId();
    }

    @Test
    void fullEndpointFlowWorksAndBusinessRulesHold() throws Exception {
        Long customerId = createCustomer();
        Long vehicleId = createVehicle(customerId);

        mockMvc.perform(get("/api/v1/vehicles/{id}", vehicleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicleId.intValue()))
                .andExpect(jsonPath("$.customerId").value(customerId.intValue()));

        mockMvc.perform(get("/api/v1/customers/{customerId}/vehicles", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(vehicleId.intValue()));

        mockMvc.perform(get("/api/v1/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        mockMvc.perform(get("/api/v1/services/{id}", fixedServiceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fixedServiceId.intValue()));

        Long orderId = createOrder(customerId, vehicleId);

        mockMvc.perform(post("/api/v1/orders/{id}/services", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serviceId\":" + fixedServiceId + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceIds.length()").value(1))
                .andExpect(jsonPath("$.total").value(25.00));

        mockMvc.perform(post("/api/v1/orders/{id}/services", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serviceId\":" + variableServiceId + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceIds.length()").value(2))
                .andExpect(jsonPath("$.total").value(55.00));

        mockMvc.perform(patch("/api/v1/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        mockMvc.perform(patch("/api/v1/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"COMPLETED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.fullyPaid").value(false));

        Long paymentId = registerCashPayment(orderId);

        mockMvc.perform(get("/api/v1/payments/{id}", paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentId.intValue()))
                .andExpect(jsonPath("$.paymentMethod").value("CASH"))
                .andExpect(jsonPath("$.change").value(0.00));

        mockMvc.perform(get("/api/v1/payments/order/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentId.intValue()));

        mockMvc.perform(get("/api/v1/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.fullyPaid").value(true));

        mockMvc.perform(patch("/api/v1/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"DELIVERED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));

        mockMvc.perform(patch("/api/v1/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CANCELLED\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void canCreateAdditionalServicesThroughApi() throws Exception {
        mockMvc.perform(post("/api/v1/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"Interior Disinfection",
                                  "description":"Added through API",
                                  "basePrice":35.00,
                                  "serviceType":"INTERIOR_DISINFECTION"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Interior Disinfection"))
                .andExpect(jsonPath("$.pricingMode").value("FIXED"));

        mockMvc.perform(post("/api/v1/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"Truck Premium Wash",
                                  "description":"Variable by vehicle",
                                  "basePrice":40.00,
                                  "serviceType":"PREMIUM_WASH",
                                  "factorByVehicleType":{
                                    "TRUCK":2.50,
                                    "CAR":1.25
                                  }
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.pricingMode").value("VARIABLE"))
                .andExpect(jsonPath("$.factorByVehicleType.TRUCK").value(2.50));

        mockMvc.perform(get("/api/v1/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    private Long createCustomer() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"Ana",
                                  "lastName":"Gomez",
                                  "phone":"3001234567",
                                  "email":"ana.%s@example.com",
                                  "address":"Cra 1 #2-3"
                                }
                                """.formatted(System.nanoTime())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        return readId(result);
    }

    private Long createVehicle(Long customerId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "plate":"AAA%s",
                                  "brand":"Toyota",
                                  "model":"Corolla",
                                  "year":2022,
                                  "type":"CAR",
                                  "customerId":%d
                                }
                                """.formatted(System.nanoTime(), customerId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.customerId").value(customerId.intValue()))
                .andReturn();
        return readId(result);
    }

    private Long createOrder(Long customerId, Long vehicleId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId":%d,
                                  "vehicleId":%d
                                }
                                """.formatted(customerId, vehicleId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("REGISTERED"))
                .andReturn();
        return readId(result);
    }

    private Long registerCashPayment(Long orderId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "orderId":%d,
                                  "paymentMethod":"CASH",
                                  "amountPaid":55.00
                                }
                                """.formatted(orderId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentMethod").value("CASH"))
                .andExpect(jsonPath("$.change").value(0.00))
                .andReturn();
        return readId(result);
    }

    private Long readId(MvcResult result) throws Exception {
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("id").asLong();
    }
}
