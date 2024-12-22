package dog.rescue.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import dog.rescue.controller.model.LocationData;
import dog.rescue.entity.Location;

public class RescueServiceTestSupport {
	
	private static final String DOG_TABLE = "dog";
	private static final String DOG_BREED_TABLE = "dog_breed";
	private static final String BREED_TABLE = "breed";
	private static final String LOCATION_TABLE = "location";

	private static final String INSERT_DOG_1_SQL = """
			INSERT INTO dog 
			(age, color, name, location_id)
			VALUES (4, 'Brown and white', 'Ralphy', 1)
			""";

	private static final String INSERT_DOG_2_SQL = """
			INSERT INTO dog 
			(age, color, name, location_id)
			VALUES (6, 'Gray and black', 'Murdock', 1)
			""";

	private static final String INSERT_BREEDS_1_SQL = """
			INSERT INTO dog_breed 
			(dog_id, breed_id)
			values(1, 3), (1, 13)
			""";

	private static final String INSERT_BREEDS_2_SQL = """
			INSERT INTO dog_breed 
			(dog_id, breed_id)
			values(2, 5), (2, 16)
			""";

	// @formatter:off
	private LocationData insertAddress1 = new LocationData(
			1L,
			"North Hills Rescue Society",
			"52 Pine Street",
			"Abdingdon",
			"Maryland",
			"21009",
			"(410) 459-3200"
	);
	
	private LocationData insertAddress2 = new LocationData(
			2L,
			"Navarre Rescure",
			"42 Valley Farms Street",
			"Navarre",
			"Florida",
			"32556",
			"(850) 204-9485"
	);
	
	private LocationData updateAddress1 = new LocationData(
			1L,
			"Glenlake Dog Rescue Society",
			"8 East Glenlake Drive",
			"Wadsworth",
			"Ohio",
			"82341",
			"(330) 336-2105"
			);
	
	
	
	// @formatter:on
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private RescueController rescueController;
	
	protected LocationData buildInsertLocation(int which) {
		
		return which == 1 ? insertAddress1 : insertAddress2;
	}
	
	protected int rowsInLocationTable() {
		
		
		return JdbcTestUtils.countRowsInTable(jdbcTemplate, LOCATION_TABLE);
	}

	protected LocationData insertLocation(LocationData locationData) {
		Location location = locationData.toLocation();
		LocationData clone = new LocationData(location);
		
		clone.setLocationId(null);
		
		return rescueController.createLocation(clone);
	}
	
	protected LocationData retrieveLocation(Long locationId) {
		return rescueController.retrieveLocation(locationId);
	}
	
	protected List<LocationData> insertTwoLocations() {
		LocationData location1 = insertLocation(buildInsertLocation(1));
		LocationData location2 = insertLocation(buildInsertLocation(2));
		
		return List.of(location1, location2);
	}

	protected List<LocationData> retrieveAllLocations() {
		return rescueController.retrieveAllLocations();
	}

	protected List<LocationData> sorted(List<LocationData> list) {
		List<LocationData> data = new LinkedList<>(list);
		
		data.sort(
				(loc1, loc2) -> (int)(loc1.getLocationId() - (loc2.getLocationId())));
		return data;
	}


	protected LocationData updateLocation(LocationData locationData) {
		return rescueController.updateLocation(locationData.getLocationId(),locationData);
	}

	protected LocationData buildUpdateLocation() {
		return updateAddress1;
	}
	
	protected void insertDog(int which) {
		String dogSql = which == 1 ? INSERT_DOG_1_SQL : INSERT_DOG_2_SQL;
		String breedSql = which == 1 ? INSERT_BREEDS_1_SQL : INSERT_BREEDS_2_SQL;
		
		jdbcTemplate.update(dogSql);
		jdbcTemplate.update(breedSql);
	}
	
	protected int rowsInBreedTable() {
		return JdbcTestUtils.countRowsInTable(jdbcTemplate, BREED_TABLE);
	}

	protected int rowsInDogBreedTable() {
		return JdbcTestUtils.countRowsInTable(jdbcTemplate, DOG_BREED_TABLE);
	}

	protected int rowsInDogTable() {
		return JdbcTestUtils.countRowsInTable(jdbcTemplate, DOG_TABLE);
	}


	protected void deleteLocation(long locationId) {
		rescueController.deleteLocation(locationId);
	}

}
