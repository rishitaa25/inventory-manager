package org.example.inventory_manager_beta1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.inventory_manager_beta1.Entities.ChatMessage;
import org.example.inventory_manager_beta1.Repositories.ChatMessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.username=sa",
                "spring.datasource.password=password",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
                "spring.jpa.show-sql=false"
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class RishitaSystemTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    private String unique() {
        return String.valueOf(System.currentTimeMillis());
    }

    @Test
    public void testEmployeeSignupFindAndExport() throws Exception {
        String u = unique();

        String employeeJson = """
                {
                  "ssn": "111-22-%s",
                  "firstName": "Rishita",
                  "lastName": "Tester",
                  "userName": "employee%s",
                  "password": "password123",
                  "email": "employee%s@test.com",
                  "phoneNumber": "555-%s"
                }
                """.formatted(u.substring(u.length() - 4), u, u, u.substring(u.length() - 4));

        String response = mockMvc.perform(post("/employee/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", notNullValue()))
                .andExpect(jsonPath("$.firstName").value("Rishita"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        int employeeId = mapper.readTree(response).get("employeeId").asInt();

        mockMvc.perform(get("/export/employees"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("employees.csv")))
                .andExpect(content().string(containsString("employee" + u)));
    }

    @Test
    public void testInventoryAddUpdateChangeHistoryExportAndDelete() throws Exception {
        String u = unique();
        String itemName = "Inventory Test Item " + u;

        String inventoryJson = """
                {
                  "itemName": "%s",
                  "itemDescription": "Backend test inventory item",
                  "amountOfItem": 20
                }
                """.formatted(itemName);

        mockMvc.perform(post("/inventory/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inventoryJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        String inventoryList = mockMvc.perform(get("/inventory"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(itemName)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode inventoryArray = mapper.readTree(inventoryList);
        int sku = -1;

        for (JsonNode item : inventoryArray) {
            if (item.get("itemName").asText().equals(itemName)) {
                sku = item.get("skuNumber").asInt();
                break;
            }
        }

        assertTrue(sku > 0, "SKU should be generated for the new inventory item");

        String updateJson = """
                {
                  "skuNumber": %d,
                  "amountOfItem": 5,
                  "employeeId": 1
                }
                """.formatted(sku);

        mockMvc.perform(put("/inventory/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(get("/inventory/" + sku))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amountOfItem").value(25));

        mockMvc.perform(get("/inventory/changes"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(sku))));

        mockMvc.perform(get("/export/inventory"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("inventory.csv")))
                .andExpect(content().string(containsString(itemName)));

        mockMvc.perform(delete("/inventory/delete/" + sku))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testChatHistoryExportAndClear() throws Exception {
        String room = "Room" + unique();

        chatMessageRepository.save(new ChatMessage("rishita", room, "First test message"));
        chatMessageRepository.save(new ChatMessage("tester", room, "Second test message"));

        mockMvc.perform(get("/chat/history/" + room))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].content").value("First test message"))
                .andExpect(jsonPath("$[1].content").value("Second test message"));

        mockMvc.perform(get("/export/chat/" + room))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("chat_" + room + ".csv")))
                .andExpect(content().string(containsString("First test message")));

        mockMvc.perform(delete("/chat/history/clear/" + room))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("has been cleared")));

        mockMvc.perform(get("/chat/history/" + room))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testCreateAdminCreateGroupChatAddMemberAndGetGroup() throws Exception {
        String u = unique();

        String adminJson = """
                {
                  "ssn": "222-33-%s",
                  "firstName": "Admin",
                  "lastName": "Tester",
                  "userName": "admin%s",
                  "password": "password123",
                  "email": "admin%s@test.com",
                  "phoneNumber": "666-%s",
                  "managementTitle": "SUPERVISOR"
                }
                """.formatted(u.substring(u.length() - 4), u, u, u.substring(u.length() - 4));

        String adminResponse = mockMvc.perform(post("/admin/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        int adminId = mapper.readTree(adminResponse).get("employeeId").asInt();

        String employeeJson = """
                {
                  "ssn": "333-44-%s",
                  "firstName": "Group",
                  "lastName": "Member",
                  "userName": "member%s",
                  "password": "password123",
                  "email": "member%s@test.com",
                  "phoneNumber": "777-%s"
                }
                """.formatted(u.substring(u.length() - 4), u, u, u.substring(u.length() - 4));

        String employeeResponse = mockMvc.perform(post("/employee/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        int employeeId = mapper.readTree(employeeResponse).get("employeeId").asInt();

        String roomName = "groupRoom" + u;

        String groupResponse = mockMvc.perform(post("/groupchat/create")
                        .param("roomName", roomName)
                        .param("adminId", String.valueOf(adminId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomName").value(roomName))
                .andReturn()
                .getResponse()
                .getContentAsString();

        long groupId = mapper.readTree(groupResponse).get("id").asLong();

        mockMvc.perform(post("/groupchat/addMember")
                        .param("groupId", String.valueOf(groupId))
                        .param("employeeId", String.valueOf(employeeId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomName").value(roomName));

    }

    @Test
    public void testChatMessageRepositoryOrdersMessagesByTimestamp() {
        String room = "RepoRoom" + unique();

        chatMessageRepository.save(new ChatMessage("sender1", room, "Message A"));
        chatMessageRepository.save(new ChatMessage("sender2", room, "Message B"));

        var messages = chatMessageRepository.findByRoomOrderByTimestampAsc(room);

        assertEquals(2, messages.size());
        assertEquals("Message A", messages.get(0).getContent());
        assertEquals("Message B", messages.get(1).getContent());
        assertNotNull(messages.get(0).getTimestamp());
    }
}