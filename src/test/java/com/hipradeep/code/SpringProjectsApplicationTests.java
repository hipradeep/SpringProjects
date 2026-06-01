package com.hipradeep.code;

import com.hipradeep.code.model.PrefixedEntity;
import com.hipradeep.code.repository.HsttBankMstRepository;
import com.hipradeep.code.repository.PrefixedEntityRepository;
import com.hipradeep.code.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringProjectsApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private HsttBankMstRepository bankRepository;

	@Autowired
	private PrefixedEntityRepository prefixedEntityRepository;

	@Test
	void contextLoads() {
		assertThat(productRepository).isNotNull();
		assertThat(bankRepository).isNotNull();
		assertThat(prefixedEntityRepository).isNotNull();
	}

	@Test
	void testProductRESTEndpoints() throws Exception {
		// 1. Create a Product
		String productJson = "{\"name\":\"Snowflake Notebook\",\"price\":1299.00}";

		String responseContent = mockMvc.perform(post("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.name", is("Snowflake Notebook")))
				.andExpect(jsonPath("$.price", is(1299.00)))
				.andReturn()
				.getResponse()
				.getContentAsString();

		// Parse ID from JSON
		String idStr = responseContent.split("\"id\":")[1].split(",")[0].trim();
		long generatedId = Long.parseLong(idStr);

		// Assert Snowflake ID characteristics (positive long)
		assertThat(generatedId).isPositive();

		// 2. List all Products
		mockMvc.perform(get("/api/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

		// 3. Get single Product by custom Snowflake ID
		mockMvc.perform(get("/api/products/" + generatedId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(generatedId)))
				.andExpect(jsonPath("$.name", is("Snowflake Notebook")))
				.andExpect(jsonPath("$.price", is(1299.00)));
	}

	@Test
	void testDvdmsBankRESTEndpoints() throws Exception {
		// 1. Create HsttBankMst entity
		String bankJson = "{\"gnumHospitalCode\":100,\"gstrBankName\":\"State Bank of Assam\",\"gstrBankShortName\":\"SBA\",\"gnumIsvalid\":1}";

		mockMvc.perform(post("/api/banks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(bankJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gnumBankId", is(1001))) // Initial sequence value is coalesce(max, 1000)+1 -> 1001
				.andExpect(jsonPath("$.gnumHospitalCode", is(100)))
				.andExpect(jsonPath("$.gstrBankName", is("State Bank of Assam")));

		// 2. Create second bank with same hospital code to assert NamedQuery max() sequence increment
		String secondBankJson = "{\"gnumHospitalCode\":100,\"gstrBankName\":\"Assam Regional Bank\",\"gstrBankShortName\":\"ARB\",\"gnumIsvalid\":1}";

		mockMvc.perform(post("/api/banks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(secondBankJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gnumBankId", is(1002))) // Incremented sequence value
				.andExpect(jsonPath("$.gnumHospitalCode", is(100)))
				.andExpect(jsonPath("$.gstrBankName", is("Assam Regional Bank")));

		// 3. Create a bank with a DIFFERENT hospital code (HQL NamedQuery scopes by hospital code!)
		String thirdBankJson = "{\"gnumHospitalCode\":200,\"gstrBankName\":\"Guwahati Merchant Bank\",\"gstrBankShortName\":\"GMB\",\"gnumIsvalid\":1}";

		mockMvc.perform(post("/api/banks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(thirdBankJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gnumBankId", is(1001))) // Starts at 1001 since gnumHospitalCode 200 has no previous records!
				.andExpect(jsonPath("$.gnumHospitalCode", is(200)))
				.andExpect(jsonPath("$.gstrBankName", is("Guwahati Merchant Bank")));

		// 4. List all banks
		mockMvc.perform(get("/api/banks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
	}

	@Test
	void testPrefixedEntityRESTEndpoints() throws Exception {
		// 1. Create a PrefixedEntity
		String entityJson = "{\"name\":\"Prefixed Test Notebook\"}";

		String responseContent = mockMvc.perform(post("/api/prefixed-entities")
				.contentType(MediaType.APPLICATION_JSON)
				.content(entityJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.name", is("Prefixed Test Notebook")))
				.andReturn()
				.getResponse()
				.getContentAsString();

		// Parse ID from JSON
		String generatedId = responseContent.split("\"id\":")[1].split(",")[0].replace("\"", "").replace("}", "").trim();

		// Assert custom Prefixed UUID characteristics
		assertThat(generatedId).startsWith("PROD_PRE_");
		assertThat(generatedId.length()).isEqualTo(41); // PROD_ (5 chars) + PRE_ (4 chars) + 32 UUID chars = 41

		// 2. List all PrefixedEntities
		mockMvc.perform(get("/api/prefixed-entities"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

		// 3. Get single PrefixedEntity by custom Prefixed UUID
		mockMvc.perform(get("/api/prefixed-entities/" + generatedId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(generatedId)))
				.andExpect(jsonPath("$.name", is("Prefixed Test Notebook")));
	}

	@Test
	void testOrderRESTEndpoints() throws Exception {
		// 1. Create first Order for itemId 9876543210
		String orderJson1 = "{\"itemId\":9876543210,\"customerName\":\"John Doe\",\"quantity\":2}";

		mockMvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(orderJson1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("ODER_321000")))
				.andExpect(jsonPath("$.itemId", is(9876543210L)))
				.andExpect(jsonPath("$.customerName", is("John Doe")))
				.andExpect(jsonPath("$.quantity", is(2)));

		// 2. Create second Order for the SAME itemId to verify dynamic increment
		String orderJson2 = "{\"itemId\":9876543210,\"customerName\":\"Alice Smith\",\"quantity\":5}";

		mockMvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(orderJson2))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("ODER_321001")))
				.andExpect(jsonPath("$.itemId", is(9876543210L)))
				.andExpect(jsonPath("$.customerName", is("Alice Smith")))
				.andExpect(jsonPath("$.quantity", is(5)));

		// 3. Get single Order by generated ID
		mockMvc.perform(get("/api/orders/ODER_321001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("ODER_321001")))
				.andExpect(jsonPath("$.customerName", is("Alice Smith")));

		// 4. List all Orders
		mockMvc.perform(get("/api/orders"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
	}
}
