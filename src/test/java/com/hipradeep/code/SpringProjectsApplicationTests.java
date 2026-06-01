package com.hipradeep.code;

import com.hipradeep.code.model.PrefixedEntity;
import com.hipradeep.code.model.Bank;
import com.hipradeep.code.repository.BankRepository;
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
	private BankRepository bankRepository;

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
	void testBankRESTEndpoints() throws Exception {
		// 1. Create Bank entity
		String bankJson = "{\"bankName\":\"State Bank of Assam\",\"bankShortName\":\"SBA\"}";

		mockMvc.perform(post("/api/banks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(bankJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.bankId", is(1001))) // Initial sequence value is coalesce(max, 1000)+1 -> 1001
				.andExpect(jsonPath("$.bankName", is("State Bank of Assam")));

		// 2. Create second bank to assert NamedQuery max() sequence increment
		String secondBankJson = "{\"bankName\":\"Assam Regional Bank\",\"bankShortName\":\"ARB\"}";

		mockMvc.perform(post("/api/banks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(secondBankJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.bankId", is(1002))) // Incremented sequence value
				.andExpect(jsonPath("$.bankName", is("Assam Regional Bank")));

		// 3. List all banks
		mockMvc.perform(get("/api/banks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
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
