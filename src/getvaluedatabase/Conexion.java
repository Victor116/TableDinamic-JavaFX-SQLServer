package getvaluedatabase;

import static getvaluedatabase.FXMLDocumentController.Alerta;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** @author DELL */
public class Conexion {
    static Connection conn;
    static Statement st;
    static String bd = "yelp";           //Nombre de la base de datos
    static String user = "replicacion";  //Usuario        
    static String pass = "1234";             //Contraseña
    static String port = "1433";
    static String IP = "MEXICANS-PC";
    static String url = "jdbc:sqlserver://"+ IP +":"+ port +"; databaseName="+ bd +";user="+ user +";password="+ pass+"";
    static String urlAuthWindows = "jdbc:sqlserver://"+ IP +"\\inst01;databaseName="+ bd +";integratedSecurity=true";
    
    public static Connection conectar() throws SQLException, ClassNotFoundException{
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
            conn = DriverManager.getConnection(url);

            st = conn.createStatement();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
        return conn;
    }
    
    public Connection getConnection(){
        return conn;
    }
    
    public void desconectar(){
        conn = null;
    }
    
    public ResultSet BuscarEnLaBaseDeDatos(String instruccion) throws SQLException {
        try{            
            if( !conn.isClosed() ){
                ResultSet rs = st.executeQuery(instruccion); //Ejecuta el query de SQL
                if(!rs.next()){
                    Alerta("Hay un error en tu tabla seleccionada.\n\n"
                        + "1.- Puede que este vacia.\n"
                        + "2.- Puede que este mal estructurada.");
                    return null;
                }
                return rs;
            }else{ //Error en la conexión
                System.out.println("La conexión con la Base de Datos está cerrada.");
                return null;
            }
        }catch(SQLException ex){
            System.out.println("Error " + ex.getMessage());
            return null;
        }
    }
}
