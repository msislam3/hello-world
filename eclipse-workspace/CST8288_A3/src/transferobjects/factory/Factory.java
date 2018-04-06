package transferobjects.factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Factory<T> {

	T createFromResultSet(ResultSet rs) throws SQLException;
	List<T> createListFromResultSet(ResultSet rs);
	T createFromMap(Map<String, String[]> map);
}
