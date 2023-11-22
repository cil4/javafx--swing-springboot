package javafx.tareas.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.tareas.modelo.Tarea;
import javafx.tareas.servicio.TareaServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class IndexControlador implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    @Autowired
    private TareaServicio tareaServicio;

    @FXML
    private TableView<Tarea> tareaTabla;

    @FXML
    private TableColumn<Tarea, Integer> idTareaColumna;

    @FXML
    private TableColumn<Tarea, String> TareaColumna;

    @FXML
    private TableColumn<Tarea, String> ResponsableColumna;

    @FXML
    private TableColumn<Tarea, String> StatusColumna;

    private final ObservableList<Tarea> tareaList = FXCollections.observableArrayList();


    @FXML
    private TextField nombreTareaTexto;

    @FXML
    private TextField responsableTexto;

    @FXML
    private TextField statusTexto;

    private Integer idTareaInterno;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    tareaTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    configurarColumnas();

    listarTareas();
    }

    private void configurarColumnas(){
        idTareaColumna.setCellValueFactory(new PropertyValueFactory<>("idTarea"));
        TareaColumna.setCellValueFactory(new PropertyValueFactory<>("nombreTarea"));
        ResponsableColumna.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        StatusColumna.setCellValueFactory(new PropertyValueFactory<>("status"));



    }

    private void listarTareas(){
        logger.info("Ejecutando listado de tareas");
        tareaList.clear();
        tareaList.addAll(tareaServicio.listarTareas());
        tareaTabla.setItems(tareaList);

    }

    public void agregarTarea(){
        if(nombreTareaTexto.getText().isEmpty()){
            mostrarMensaje("Error de validación", "Debe proporcionar una tarea");
            nombreTareaTexto.requestFocus();
            return;
        }else{
            var tarea = new Tarea();
            recolectarDatosForm(tarea);
            tarea.setIdTarea(null);
            tareaServicio.guardarTarea(tarea);
            mostrarMensaje("Información", "Tarea añadida");
            limpiarFormulario();
            listarTareas();
        }
    }
public void cargarTareaForm(){
        var tarea = tareaTabla.getSelectionModel().getSelectedItem();
        if(tarea != null){
           idTareaInterno = tarea.getIdTarea();
           nombreTareaTexto.setText(tarea.getNombreTarea());
           responsableTexto.setText(tarea.getResponsable());
           statusTexto.setText(tarea.getStatus());

        }
}
    private void recolectarDatosForm(Tarea tarea){
        if(idTareaInterno != null)
            tarea.setIdTarea(idTareaInterno);
        tarea.setNombreTarea(nombreTareaTexto.getText());
        tarea.setResponsable(responsableTexto.getText());
        tarea.setStatus(statusTexto.getText());


    }
    public void  modificarTarea(){
        if(idTareaInterno == null){
            mostrarMensaje("Información", "Debe seleccionar una tarea");
            return;
        }
        if(nombreTareaTexto.getText().isEmpty()){
            mostrarMensaje("Error de validación", "Debe proporcionar una tarea");
            nombreTareaTexto.requestFocus();
            return;

        }
        var tarea = new Tarea();
        recolectarDatosForm(tarea);
        tareaServicio.guardarTarea(tarea);
        mostrarMensaje("Información", "Tarea modificada");
        limpiarFormulario();
        listarTareas();

    }

    public void eliminarTarea(){
        var tarea = tareaTabla.getSelectionModel().getSelectedItem();
        if(tarea != null){
            logger.info("Registro a eliminar " + tarea.toString());
            tareaServicio.eliminarTarea(tarea);
            mostrarMensaje("Información", "Tarea eliminada: "+ tarea.getIdTarea());
            limpiarFormulario();
            listarTareas();

        }else{
            mostrarMensaje("Error", "Debe seleccionar una tarea a eliminar");
        }
    }
    public void limpiarFormulario(){
        idTareaInterno=null;
        nombreTareaTexto.clear();
        responsableTexto.clear();
        statusTexto.clear();
    }

    private void mostrarMensaje(String titulo, String mensaje){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }


}
