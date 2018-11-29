package getvaluedatabase;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

/** @author DELL */
public class FXMLDocumentController implements Initializable {
    
    @FXML private TextArea textSQL;
    @FXML private TableView<ObservableList> TableDataBase = new TableView<>();
    private ObservableList<ObservableList> data;    //Informacion para manejar la tabla
    
    Conexion con = new Conexion();                                             //Conexion a la base de datos
    private ResultSet rs = null;     
//    static ResultSetMetaData rsmd = null;     //Busqueda en dase de datos
    
    @FXML private void DetectionAltEnter(){
        textSQL.setOnKeyReleased( e -> {
            if(e.isAltDown()  &&  e.getCode()==KeyCode.ENTER){
                try {
                    con.BuscarEnLaBaseDeDatos( textSQL.getText() );
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    @FXML private void LlamarDataBaseWithSQL(){
        try{
            System.out.println( textSQL.getText() );
            rs = con.BuscarEnLaBaseDeDatos(textSQL.getText() );                //Busco en la base datosd
            ResultSetMetaData rsmd = rs.getMetaData();
            int ColumnCount = rsmd.getColumnCount();
            data = FXCollections.observableArrayList();
            
            // Añadir columas a la tabla
            for(int i = 0; i < ColumnCount; i++){
                final int finalIdx = i;
                TableColumn column = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(finalIdx).toString());
                    }
                });
                TableDataBase.getColumns().add(column);
            }

//          Añadir informacion a la tabla
            while(rs.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){                    
                    row.add( rs.getString(i) );
                }
                data.add(row);
            }
            TableDataBase.setItems(data);
        } catch (SQLException ex) {
            Alerta("1.- Puede que no tengas tablas en tu base de datos'.\n"
                + "2.- Puede que se haga escrito mal.");
        }
    }
    
    @FXML public static void Alerta(String NotaAlerta){                         //Le pasamos la alerta para notificar
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
        alert.setHeaderText("");
        alert.setContentText(NotaAlerta);
        alert.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {                                                                   //Nos conectamos a la base de datos
            con.conectar();
            System.out.println("Conexion establecida");
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
