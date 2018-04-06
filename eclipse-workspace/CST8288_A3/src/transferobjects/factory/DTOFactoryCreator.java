package transferobjects.factory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DTOFactoryCreator {
	
	/*public static <T> Factory<T> getFactory(String factoryName) {
		if(factoryName.equalsIgnoreCase("EMPLOYEE")) {
			return (Factory<T>) new EmployeeFactory();
		}
		
		return null;
	}*/
	
	private static final String PACKAGE = "transferobjects.factory.";
    private static final String FACTORY = "Factory";

    private DTOFactoryCreator() {
    }

    @SuppressWarnings("unchecked")
    public static <T> Factory<T> getFactory(String factoryName) {
        Factory<T> factory = null;
        try {
            factory = (Factory< T>) Class.forName(PACKAGE + factoryName + FACTORY).newInstance();
        } catch (Exception ex) {
            Logger.getLogger(DTOFactoryCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return factory;
    }

    public static <T> Factory<T> createBuilder(Class<T> type) {
        return getFactory(type.getSimpleName());
    }
}
