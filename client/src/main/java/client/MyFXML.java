package client;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import com.google.inject.Injector;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import javafx.util.Callback;
import javafx.util.Pair;

public class MyFXML {

    private Injector injector;

    /**
     * FXML injection constructor
     *
     * @param injector - injector parameter.
     */
    public MyFXML(Injector injector) {
        this.injector = injector;
    }

    /**
     * Loading generic method for the loading pair of scenes and it's controllers
     *
     * @param c - class that has to be loaded.
     * @param parts - parts of the URL to get to the correct file.
     * @param <T> - generic type.
     * @return - returns the generic type of the thing used in the function.
     */
    public <T> Pair<T, Parent> load(Class<T> c, String... parts) {
        try {
            var loader = new FXMLLoader(getLocation(parts), null, null, new MyFactory(), StandardCharsets.UTF_8);
            Parent parent = loader.load();
            T ctrl = loader.getController();
            return new Pair<>(ctrl, parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method responsible for getting the location of the file with specific parts provided.
     *
     * @param parts - parts provided in the load method.
     * @return - returns an URL that is composed.
     */
    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }

    private class MyFactory implements BuilderFactory, Callback<Class<?>, Object> {

        /**
         * Getter for the builder thing.
         *
         * @param type - type of the builder.
         * @return - returns a builder.
         */
        @Override
        @SuppressWarnings("rawtypes")
        public Builder<?> getBuilder(Class<?> type) {
            return new Builder() {
                /**
                 * Build method for the specific injector.
                 * @return - returns an object of injector type.
                 */
                @Override
                public Object build() {
                    return injector.getInstance(type);
                }
            };
        }

        /**
         * Call method for the specific injector.
         *
         * @return - returns an object of injector type.
         */
        @Override
        public Object call(Class<?> type) {
            return injector.getInstance(type);
        }
    }
}