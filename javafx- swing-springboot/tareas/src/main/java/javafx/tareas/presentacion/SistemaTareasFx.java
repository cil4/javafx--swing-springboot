package javafx.tareas.presentacion;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.tareas.TareasApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class SistemaTareasFx extends Application {

    //public static void main(String[] args) {
      //  launch(args);
    //}
    private ConfigurableApplicationContext applicationContext;
    @Override
    public void init(){
        this.applicationContext=  new SpringApplicationBuilder(TareasApplication.class).run();

    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader =
                new FXMLLoader(TareasApplication.class.getResource("/templates/index.fxml"));
        loader.setControllerFactory(applicationContext::getBean);

        Scene escena = new Scene(loader.load());

        stage.setScene(escena);

        stage.show();


    }
    @Override
    public void stop(){
        applicationContext.close();
        Platform.exit();
    }
}
