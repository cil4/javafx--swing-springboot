package gm.tienda_libros.presentacion;

import gm.tienda_libros.modelo.Libro;
import gm.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class LibroForm extends JFrame {
    LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField idTexto;
    private JTextField libroTexto;
    private JTextField autorTexto;
    private JTextField precioTexto;
    private JTextField stockTexto;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tablaModeloLibros;

    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
        agregarButton.addActionListener(e -> agregarLibro());

        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });
        modificarButton.addActionListener(e -> modificarLibro());
        eliminarButton.addActionListener(e ->eliminarLibro());
    }

    private void iniciarForma(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900,700);
       // Toolkit toolkit = Toolkit.getDefaultToolkit();
       // Dimension tamanioPantalla = toolkit.getScreenSize();
        //int x = (tamanioPantalla.width - getWidth()/ 2);
        //int y = (tamanioPantalla.height = getHeight() / 2);
        //setLocation(x, y);
        setLocationRelativeTo(null);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        //Creación de elemento idTexto oculto
        idTexto = new JTextField("");
        idTexto.setVisible(false);


        this.tablaModeloLibros = new DefaultTableModel(0, 5){
            @Override
            public boolean isCellEditable(int row, int column){
                return  false;
            }
        };
        String[] cabeceros = {"Id", "Libro", "Autor", "Precio", "Stock"};
        this.tablaModeloLibros.setColumnIdentifiers(cabeceros);
        // Intanciar el objeto JTable
        this.tablaLibros = new JTable(tablaModeloLibros);

        //Evitar múltiples selecciones
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listarLibros();
    }
private void agregarLibro(){
        //Se leen los valores del formulario
    if(libroTexto.getText().equals("")){
        mostrarMensaje("Proporciona el nombre del Libro");
        libroTexto.requestFocusInWindow();
        return;
    }
    var nombreLibro= libroTexto.getText();
    var autor = autorTexto.getText();
    var precio= Double.parseDouble(precioTexto.getText());
    var stock= Integer.parseInt(stockTexto.getText());

    //Creación de objeto Libro
    var libro = new Libro(null,nombreLibro,autor,precio,stock);
    this.libroServicio.guardarLibro(libro);
    mostrarMensaje("Se agregó el libro...");
    limpiarForm();
    listarLibros();
}

private void cargarLibroSeleccionado(){
        var renglon = tablaLibros.getSelectedRow();
        if(renglon != -1){ //Va a regresar -1 si no se seleccionó algún registro
            String idLibro =tablaLibros.getModel().getValueAt(renglon,0).toString();
            idTexto.setText(idLibro);

            String nombreLibro=tablaLibros.getModel().getValueAt(renglon,1).toString();
            libroTexto.setText(nombreLibro);

            String autorLibro = tablaLibros.getModel().getValueAt(renglon,2).toString();
            autorTexto.setText(autorLibro);

            String precioLibro = tablaLibros.getModel().getValueAt(renglon,3).toString();
            precioTexto.setText(precioLibro);

            String stockLibro = tablaLibros.getModel().getValueAt(renglon,4).toString();
            stockTexto.setText(stockLibro);

        }
}
private void modificarLibro(){
        if(this.idTexto.getText().equals("")){
            mostrarMensaje("Debe seleccionar un registro...");
        }else{
            //Se verifica que el nombre del libro no sea nulo
            if(libroTexto.getText().equals("")){
                mostrarMensaje("Proporciona el nombre del libro...");
                libroTexto.requestFocusInWindow();
                return;
            }
            //Se carga el objeto libro a actualizar
            int idLibro= Integer.parseInt(idTexto.getText());
            var nombreLibro = libroTexto.getText();
            var autor= autorTexto.getText();
            var precio= Double.parseDouble(precioTexto.getText());
            var stock= Integer.parseInt(stockTexto.getText());
            var libro= new Libro(idLibro,nombreLibro,autor,precio,stock);
            this.libroServicio.guardarLibro(libro);
            mostrarMensaje("Se realizó la modificación del libro");
            limpiarForm();
            listarLibros();
        }
}

private void eliminarLibro(){
            var renglon = tablaLibros.getSelectedRow();
            if(renglon != -1) {
                String idLibro =tablaLibros.getModel().getValueAt(renglon,0).toString();
                var libro = new Libro();
                libro.setIdLibro(Integer.parseInt(idLibro));
            libroServicio.eliminarLibro(libro);
            mostrarMensaje("El libro " + idLibro+ " ha sido eliminado");
            limpiarForm();
            listarLibros();
        }else{
                mostrarMensaje("Debes seleccionar el registro a eliminar");
            }
}
private void limpiarForm(){
        libroTexto.setText("");
        autorTexto.setText("");
        precioTexto.setText("");
        stockTexto.setText("");
}

private void mostrarMensaje(String msj){
        JOptionPane.showMessageDialog(this, msj);
}
    private void listarLibros(){
        // Limpiar la tabla
        tablaModeloLibros.setRowCount(0);
        // Obtener los libros
        var libros = libroServicio.listarLibros();
        libros.forEach((libro)->{
            Object[] renglonLibro = {
                    libro.getIdLibro(),
                    libro.getNombreLibro(),
                    libro.getAutor(),
                    libro.getPrecio(),
                    libro.getStock()
            };
            this.tablaModeloLibros.addRow(renglonLibro);
        });
    }

}
