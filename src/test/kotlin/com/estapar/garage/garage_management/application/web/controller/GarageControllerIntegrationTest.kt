import com.estapar.garage.garage_management.GarageManagementApplication
import com.estapar.garage.garage_management.config.extension.postgres.PostgresEmbeddedExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [GarageManagementApplication::class]
)
@ActiveProfiles("test")
@ExtendWith(PostgresEmbeddedExtension::class)
@AutoConfigureMockMvc
class GarageControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @LocalServerPort
    private var port: Int = 0

    @Test
    fun `should save entry status`() {
        initialSetup()
        val jsonRequest = """
            {
                "license_plate": "ABC1234",
                "entry_time": "2023-10-01T10:00:00",
                "event_type": "ENTRY"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/garage/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should save parked status`() {
        initialSetup()
        jdbcTemplate.execute(
            """
            INSERT INTO vehicle_parking_session (spot_id, license_plate, entry_time, parked_time, exit_time, status, total_price, created_at, updated_at)
            VALUES (1, 'ABC1234', '2023-10-01 10:00:00', NULL, NULL, 'ENTRY', NULL, '2023-10-01 10:00:00', '2023-10-01 10:00:00')
        """
        )
        val jsonRequest = """
            {
                "license_plate": "ABC1234",
                "lat": 10.0,
                "lng": 20.0,
                "event_type": "PARKED"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/garage/parked")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should save exit status`() {
        initialSetup()
        jdbcTemplate.execute(
            """
            INSERT INTO vehicle_parking_session (spot_id, license_plate, entry_time, parked_time, exit_time, status, total_price, created_at, updated_at)
            VALUES (1, 'ABC1234', '2023-10-01 10:00:00', '2023-10-01 10:00:00', NULL, 'PARKED', NULL, '2023-10-01 10:00:00', '2023-10-01 10:00:00')
        """
        )
        val jsonRequest = """
            {
                "license_plate": "ABC1234",
                "exit_time": "2023-10-01T12:00:00",
                "event_type": "EXITED"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/garage/exit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should get plate status`() {
        initialSetup()
        jdbcTemplate.execute(
            """
            INSERT INTO vehicle_parking_session (spot_id, license_plate, entry_time, parked_time, exit_time, status, total_price, created_at, updated_at)
            VALUES (1, 'ABC1234', '2023-10-01 10:00:00', NULL, NULL, 'ENTRY', NULL, '2023-10-01 10:00:00', '2023-10-01 10:00:00')
        """
        )
        val jsonRequest = """
            {
                "license_plate": "ABC1234"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/garage/plate-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should get spot status`() {
        initialSetup()
        jdbcTemplate.execute(
            """
            INSERT INTO vehicle_parking_session (spot_id, license_plate, entry_time, parked_time, exit_time, status, total_price, created_at, updated_at)
            VALUES (1, 'ABC1234', '2023-10-01 10:00:00', '2023-10-01 10:00:00', NULL, 'PARKED', NULL, '2023-10-01 10:00:00', '2023-10-01 10:00:00')
        """
        )
        val jsonRequest = """
            {
                "lat": 10.0,
                "lng": 20.0
            }
        """.trimIndent()

        mockMvc.perform(
            post("/garage/spot-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should get revenue`() {
        initialSetup()
        jdbcTemplate.execute(
            """
            INSERT INTO vehicle_parking_session (spot_id, license_plate, entry_time, parked_time, exit_time, status, total_price, created_at, updated_at)
            VALUES (1, 'ABC1234', '2023-10-01 10:00:00', '2023-10-01 10:00:00', '2023-10-01 10:00:00', 'EXITED', 30.0, '2023-10-01 10:00:00', '2023-10-01 10:00:00')
        """
        )
        val jsonRequest = """
            {
                "date": "2023-10-01",
                "sector": "A"
            }
        """.trimIndent()

        mockMvc.perform(
            get("/garage/revenue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should initialize garage setup`() {
        val jsonRequest = """
            {
                "garage": [
                    {
                        "sector": "A",
                        "base_price": 40.50,
                        "max_capacity": 10,
                        "open_hour": "00:00",
                        "close_hour": "23:59",
                        "duration_limit_minutes": 1440
                    }
                ],
                 "spots": [
                   {
                     "id": 1,
                     "sector": "A",
                     "lat": -23.561684,
                     "lng": -46.655981
                   },
                   {
                     "id": 2,
                     "sector": "A",
                     "lat": -23.561674,
                     "lng": -46.655971
                   }
                 ]
            }
        """.trimIndent()

        mockMvc.perform(
            get("/garage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
            .andExpect(status().isOk)
    }

    private fun initialSetup() {
        jdbcTemplate.execute(
            """
            INSERT INTO garage_sector (id, sector_code, base_price, max_capacity, open_hour, close_hour, duration_limit_minutes, available_spots, created_at, updated_at)
            VALUES (1, 'A', 40.50, 10, '00:00', '23:59', 1440, 10, '2021-10-10 10:10:10', '2021-10-10 10:10:10')
        """
        )

        jdbcTemplate.execute(
            """
            INSERT INTO parking_spot (id, sector_id, lat, lng, occupied, created_at, updated_at)
            VALUES (1, 1, 10.0, 20.0, false, '2021-10-10 10:10:10', '2021-10-10 10:10:10')
        """
        )
    }
}