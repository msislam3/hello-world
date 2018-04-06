package transferobjects.factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AbstractFactory<T> implements Factory<T> {

	@Override
	public T createFromResultSet(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> createListFromResultSet(ResultSet rs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T createFromMap(Map<String, String[]> map) {
		// TODO Auto-generated method stub
		return null;
	}

}
